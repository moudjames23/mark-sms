package com.moudjames23.marksms.service;

import com.moudjames23.marksms.Utils;
import com.moudjames23.marksms.exception.InvalidUploadFileException;
import com.moudjames23.marksms.exception.ResourceAlreadyExistException;
import com.moudjames23.marksms.exception.ResourceNotFoundException;
import com.moudjames23.marksms.model.entities.Customer;
import com.moudjames23.marksms.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;


    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> index() {
        return customerRepository.findAll();
    }

    public Customer create(Customer customer) {
        if (customer.getEmail() != null && customer.getEmail().isEmpty()) {
            customerRepository.findByEmail(customer.getEmail())
                    .ifPresent(c -> {
                        throw new ResourceAlreadyExistException(getMessage("Cet Email ", customer.getEmail()));
                    });
        }

        customerRepository.findByPhone(customer.getPhone())
                .ifPresent(c -> {
                    throw new ResourceAlreadyExistException(getMessage("Ce numéro de téléphone ", customer.getPhone()));
                });

        return customerRepository.save(customer);
    }


    public Customer update(Customer customer, Long customerId) {
        this.findById(customerId);

        customerRepository.findByEmail(customer.getEmail())
                .ifPresent(c -> {
                    if (!c.getId().equals(customerId))
                        throw new ResourceAlreadyExistException(getMessage("Cet Email ", customer.getEmail()));
                });

        customerRepository.findByPhone(customer.getPhone())
                .ifPresent(c -> {
                    if (!c.getId().equals(customerId))
                        throw new ResourceAlreadyExistException(getMessage("Ce numéro de téléphone ", customer.getPhone()));
                });

        customer.setId(customerId);

        return customerRepository.save(customer);
    }


    public void delete(Long customerId) {
        Customer customer = this.findById(customerId);
        customerRepository.deleteById(customer.getId());
    }

    public Customer show(Long customerId) {
        return this.findById(customerId);
    }

    public String importData(MultipartFile multipartFile) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        String originalFileName = multipartFile.getOriginalFilename();
        String tmp_storage = "/Users/moud/Desktop/uploads/";
        File fileToImport = new File(tmp_storage + originalFileName);
        multipartFile.transferTo(fileToImport);


        if (!Utils.isValidFileExtension(fileToImport.getAbsolutePath() , ".csv"))
            throw new InvalidUploadFileException("Le fichier doit être de type CSV");

        if (!Utils.isValidFileHeader(fileToImport.getAbsolutePath(), new String[]{"Nom", "Phone", "Email", "Groupe"}))
            throw new InvalidUploadFileException("Le fichier doit contenir les entêtes suivantes: Nom, Phone, Email, Groupe");

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", tmp_storage + originalFileName)
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        String exitCode = jobExecution.getExitStatus().getExitCode();

        if (ExitStatus.COMPLETED.equals(exitCode))
            Files.deleteIfExists(Paths.get(tmp_storage + originalFileName));


        return exitCode;
    }

    private Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Ce client " + customerId + " n'existe pas"));
    }

    private String getMessage(String x, String customer) {
        return x + customer + " existe déjà";
    }
}
