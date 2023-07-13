package com.moudjames23.marksms.service;

import com.moudjames23.marksms.exception.ResourceAlreadyExistException;
import com.moudjames23.marksms.exception.ResourceNotFoundException;
import com.moudjames23.marksms.model.entities.Customer;
import com.moudjames23.marksms.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> index() {
        return customerRepository.findAll();
    }

    public Customer create(Customer customer) {
        customerRepository.findByEmail(customer.getEmail())
                .ifPresent(c -> {
                    throw new ResourceAlreadyExistException("Cet Email " + customer.getEmail()+" existe déjà");
                });

        customerRepository.findByPhone(customer.getPhone())
                .ifPresent(c -> {
                    throw new ResourceAlreadyExistException("Ce numéro de téléphone " + customer.getPhone()+" existe déjà");
                });

        return customerRepository.save(customer);
    }

    public Customer update(Customer customer, Long customerId) {
        this.findById(customerId);

        customerRepository.findByEmail(customer.getEmail())
                .ifPresent(c -> {
                    if (!c.getId().equals(customerId))
                        throw new ResourceAlreadyExistException("Cet Email " + customer.getEmail()+" existe déjà");
                });

        customerRepository.findByPhone(customer.getPhone())
                .ifPresent(c -> {
                    if (!c.getId().equals(customerId))
                        throw new ResourceAlreadyExistException("Ce numéro de téléphone " + customer.getPhone()+" existe déjà");
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

    private Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Ce client " +customerId+" n'existe pas"));
    }
}
