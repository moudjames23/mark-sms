package com.moudjames23.marksms.model;

import com.moudjames23.marksms.model.entities.Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class CustomerImportFieldSetMapper implements FieldSetMapper<Customer> {
    @Override
    public Customer mapFieldSet(FieldSet fieldSet) {
        return Customer.builder()
                .name(fieldSet.readString("Nom"))
                .phone(fieldSet.readString("Phone"))
                .email(fieldSet.readString("Email"))
                .build();
    }
}
