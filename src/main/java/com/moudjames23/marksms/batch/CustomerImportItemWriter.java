package com.moudjames23.marksms.batch;

import com.moudjames23.marksms.model.entities.Customer;
import com.moudjames23.marksms.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class CustomerImportItemWriter implements ItemWriter<Customer> {

    private final CustomerRepository customerRepository;

    public CustomerImportItemWriter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void write(List<? extends Customer> customers) {
        customers
                .forEach(customer -> {
                    Optional<Customer> byPhone = customerRepository.findByPhone(customer.getPhone());

                    if (byPhone.isEmpty())
                        customerRepository.save(customer);


                });
    }
}
