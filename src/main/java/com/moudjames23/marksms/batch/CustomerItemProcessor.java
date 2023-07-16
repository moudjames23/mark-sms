package com.moudjames23.marksms.batch;

import com.moudjames23.marksms.model.entities.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) {
        return Customer.builder()
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail().isEmpty() ? null : customer.getEmail())
                .build();
    }
}
