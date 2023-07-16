package com.moudjames23.marksms.service;

import com.moudjames23.marksms.exception.ResourceAlreadyExistException;
import com.moudjames23.marksms.exception.ResourceNotFoundException;
import com.moudjames23.marksms.model.entities.Customer;
import com.moudjames23.marksms.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository);
    }

    Customer moud =  Customer.builder()
            .id(1L)
            .name("Moudjames")
            .email("moud@gmail.com")
            .phone("620029489")
            .build();

    Customer dalanda = Customer.builder()
            .id(2L)
            .name("Dalanda")
            .email("dalanda@gmail.com")
            .phone("626656820")
            .build();

    Customer oumar = Customer.builder()
            .id(3L)
            .name("Oumar")
            .email("oumar@gmail.com")
            .phone("620000000")
            .build();

    @Test
    void itShouldShouldSaveNewCustomer() {
        // Given
        // When
        this.customerService.create(moud);

        // Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(argumentCaptor.capture());

        Customer excepted = argumentCaptor.getValue();

        assertThat(excepted).isEqualTo(moud);

    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneExist() {
        // Given
        // When
        when(customerRepository.findByPhone(dalanda.getPhone())).thenReturn(Optional.of(dalanda));

        // Then
        assertThatThrownBy(() -> customerService.create(dalanda))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("Ce numéro de téléphone " +dalanda.getPhone()+" existe déjà");
    }

    /*@Test
    void itShouldNotSaveCustomerWhenEmailExist() {
        // Given
        // When
        when(customerRepository.findByEmail(oumar.getEmail())).thenReturn(Optional.of(oumar));

        // Then
        assertThatThrownBy(() -> customerService.create(oumar))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("Cet Email " +oumar.getEmail()+" existe déjà");
    }*/


    @Test
    void itShouldShowCustomerById() {
        // Given
        // When
        when(customerRepository.findById(moud.getId())).thenReturn(Optional.of(moud));

        // Then
        this.customerService.show(moud.getId());

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(customerRepository).findById(argumentCaptor.capture());

        Long excepted = argumentCaptor.getValue();

        assertThat(excepted).isEqualTo(moud.getId());
    }

    @Test
    void itShouldNotShowCustomerById() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> customerService.show(oumar.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ce client "+oumar.getId()+" n'existe pas");
    }

    @Test
    void itShouldUpdateCustomer() {
        // Given
        Customer updateMoud =  Customer.builder()
                .id(1L)
                .name("Moudjames23")
                .email("moud@gmail.com")
                .phone("620029489")
                .build();

        // When
        when(customerRepository.findById(moud.getId())).thenReturn(Optional.of(moud));

        this.customerService.update(updateMoud, moud.getId());

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        // Then
        verify(customerRepository).save(argumentCaptor.capture());

        Customer excepted = argumentCaptor.getValue();

        assertThat(excepted).isEqualTo(updateMoud);
    }

    @Test
    void itShouldNotUpdateCustomerWhereIdIsNotExist() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> customerService.update(moud, moud.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ce client "+moud.getId()+" n'existe pas");
    }

    @Test
    void itShouldDeleteCustomerById() {
        // Given
        // When
        when(customerRepository.findById(moud.getId())).thenReturn(Optional.of(moud));
        this.customerService.delete(moud.getId());

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        // Then
        verify(customerRepository).deleteById(argumentCaptor.capture());
    }

    @Test
    void itShouldDeleteWhenCustomerIdDoesNotExist() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> customerService.delete(oumar.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ce client "+oumar.getId()+" n'existe pas");
    }
}
