package com.moudjames23.marksms.repository;

import com.moudjames23.marksms.model.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void itShouldFindCustomerByPhone()  {
        // Given
        Customer moud = Customer.builder()
                .name("Moud")
                .phone("237690000000")
                .email("moud@gmail.com")
                .build();
        Customer customer = customerRepository.save(moud);

        // When
        Optional<Customer> excepted = customerRepository.findByPhone(moud.getPhone());

        // Then
        assertThat(excepted).isNotNull();
        assertThat(excepted.isPresent()).isTrue();
        assertThat(excepted.get().getPhone()).isEqualTo(moud.getPhone());
    }

    @Test
    void itShouldNotFindCustomerByPhone() {
        // Given
        String phone = "237690000001";

        // When
        Optional<Customer> excepted = customerRepository.findByPhone(phone);

        // Then
        assertThat(excepted).isNotNull();
    }

    @Test
    void itShouldShoudFindCustomerByEmail() {
        // Given
        Customer james = Customer.builder()
                .name("James")
                .phone("237690000023")
                .email("james@gmail.com")
                .build();
        Customer customer = customerRepository.save(james);

        // When
        Optional<Customer> excepted = customerRepository.findByEmail(james.getEmail());

        // Then
        assertThat(excepted).isNotNull();
        assertThat(excepted.isPresent()).isTrue();
        assertThat(excepted.get().getEmail()).isEqualTo(james.getEmail());
    }

    @Test
    void itShouldNotFindCustomerByEmail() {
        // Given
        String email = "hello@gmail.com";

        // When
        Optional<Customer> excepted = customerRepository.findByEmail(email);

        // Then
        assertThat(excepted).isNotNull();
    }
}
