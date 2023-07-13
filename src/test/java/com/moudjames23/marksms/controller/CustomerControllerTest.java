package com.moudjames23.marksms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moudjames23.marksms.model.entities.Customer;
import com.moudjames23.marksms.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    Customer moud = Customer.builder()
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

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @BeforeAll
    @AfterAll
    void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    void itShouldListEmptyCustomer() throws Exception {
        // Given
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/customers")
                                .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Liste des clients")))
                .andExpect(jsonPath("$.data.clients", hasSize(0)));


    }

    @Test
    @Order(2)
    void itShouldListCustomers() throws Exception {
        // Given
        this.customerRepository.save(moud);

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/customers")
                                .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Liste des clients")))
                .andExpect(jsonPath("$.data.clients", hasSize(1)))
                .andExpect(jsonPath("$.data.clients[0].name", is("Moudjames")));

    }

    @Test
    @Order(3)
    void itShouldSaveNewCustomer() throws Exception {
        // Given
        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dalanda))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(201)))
                .andExpect(jsonPath("$.message", is("Client cree avec succes")))
                .andExpect(jsonPath("$.data.client.name", is(dalanda.getName())));

    }

    @Test
    @Order(4)
    void itShouldNotSaveNewCustomerWhenPhoneExist() throws Exception {
        // Given
        this.customerRepository.save(oumar);

        // When
        // Then
        Customer customer = Customer.builder()
                .name("Oumar")
                .phone(this.oumar.getPhone())
                .email("oumar13@gmail.com")
                .build();

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(customer))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.message", is("Ce numéro de téléphone " + customer.getPhone()+" existe déjà")));
    }

    @Test
    @Order(5)
    void itShouldNotSaveNewCustomerWhenEmailExist() throws Exception {
        // Given
        Customer imrane = Customer.builder()
                .name("Imrane")
                .phone("620000001")
                .email("imrane@gmail.com")
                .build();

        this.customerRepository.save(imrane);

        // When
        // Then
        Customer customer = Customer.builder()
                .name("Oumar")
                .phone("620000002")
                .email(imrane.getEmail())
                .build();

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(customer))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.message", is("Cet Email " +customer.getEmail()+" existe déjà")));
    }

    @Test
    @Order(6)
    void itShouldDisplayCustomerById() throws Exception {
        // Given
        Customer mohamed = this.customerRepository.save(Customer.builder()
                .id(4L)
                .name("Mohamed")
                .phone("623000000")
                .email("imrane@gmail.com")
                .build());

        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/customers/"+mohamed.getId())
                        .contentType("application/json")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Information sur le client")))
                .andExpect(jsonPath("$.data.client.name", is(mohamed.getName())));
    }

    @Test
    @Order(7)
    void itShouldNotDisplayCustomerById() throws Exception {
        // Given
        Customer amadou = Customer.builder()
                .id(23L)
                .name("Amadou")
                .phone("623000001")
                .email("Amadou@gmail.com")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/customers/"+amadou.getId())
                                .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Ce client "+amadou.getId()+" n'existe pas")));
    }

    @Test
    @Order(8)
    void itShouldUpdateCustomer() throws Exception {
        // Given

        Customer aissatou = this.customerRepository.save(Customer.builder()
                .name("Aissatou")
                .phone("623000011")
                .email("aissatou@gmail.com")
                .build());

        Customer aissatouDiallo = Customer.builder()
                .name("Aissatou Diallo")
                .phone("623000011")
                .email("aissatou@gmail.com")
                .build();

        // When
        // Then

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/customers/" + aissatou.getId())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(aissatouDiallo))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.message", is("Mise à jour du client")))
                .andExpect(jsonPath("$.data.client.name", is(aissatouDiallo.getName())));

    }

    @Test
    @Order(9)
    void itShouldNotUpdateCustomerWhenEmailExist() throws Exception {
        // Given
        Customer mariam = customerRepository.save(
                Customer.builder()
                        .name("Mariam")
                        .phone("623000012")
                        .email("mariama@gmail.com")
                        .build()
        );

        Customer soul = customerRepository.save(
                Customer.builder()
                        .name("Soul")
                        .phone("623000013")
                        .email("soul@gmail.com")
                        .build()
        );


        Customer updateMariam = Customer.builder()
                .name("Mariam")
                .phone("623000012")
                .email(soul.getEmail())
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/customers/" + mariam.getId())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateMariam))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.message", is( "Cet Email "+updateMariam.getEmail()+" existe déjà")));
    }

    @Test
    @Order(10)
    void itShouldNotUpdateCustomerWhenPhoneExist() throws Exception {
        // Given
        Customer cire = customerRepository.save(
                Customer.builder()
                        .name("Cire Diallo")
                        .phone("623000015")
                        .email("cire@gmail.com")
                        .build()
        );

        Customer oury = customerRepository.save(
                Customer.builder()
                        .name("Oury Diallo")
                        .phone("623000016")
                        .email("oury@gmail.com")
                        .build()
        );


        Customer updateCire = Customer.builder()
                .name("Cire Diallo")
                .phone(oury.getPhone())
                .email("cire@gmail.com")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/customers/" + cire.getId())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateCire))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.message", is( "Ce numéro de téléphone " + updateCire.getPhone()+" existe déjà")));
    }

    @Test
    @Order(11)
    void itShouldDeleteCustomerById() throws Exception {
        // Given
        Customer ousmane = this.customerRepository.save(Customer.builder()
                .name("Ousmane")
                .phone("623000045")
                .email("ousmane@gmail.com")
                .build());

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/customers/"+ousmane.getId())
                                .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("Client supprime avec succes")));
    }

    @Test
    @Order(11)
    void itShouldNotDeleteCustomerById() throws Exception {
        // Given
        Customer ousmane = Customer.builder()
                .id(2323L)
                .name("Ousmane")
                .phone("623000045")
                .email("ousmane@gmail.com")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/customers/"+ousmane.getId())
                                .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Ce client "+ousmane.getId()+" n'existe pas")));
    }

    @Test
    @Order(12)
    void itShouldNotSaveCustomerWhenNameIsMissing() throws Exception {
        // Given
        Customer ousmane = Customer.builder()
                //.name("Ousmane")
                .phone("623000045")
                .email("ousmane@gmail.com")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers/")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(ousmane))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Erreurs")))
                .andExpect(jsonPath("$.errors.name", is("Le nom est obligatoire")));
    }

    @Test
    @Order(13)
    void itShouldNotSaveWhenCustomerNameLowerThan5Characters() throws Exception {
        // Given
        Customer customer = Customer.builder()
                .name("Moud")
                .phone("623000045")
                .email("ousmane@gmail.com")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers/")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(customer))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Erreurs")))
                .andExpect(jsonPath("$.errors.name", is("Le nom doit avoir au moins 5 caractères")));
    }

    @Test
    @Order(14)
    void itShouldNotSaveWhenCustomerPhoneIsMissing() throws Exception {
        // Given
        Customer customer = Customer.builder()
                .name("Moudjames23")
                //.phone("623000045")
                .email("ousmane@gmail.com")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers/")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(customer))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Erreurs")))
                .andExpect(jsonPath("$.errors.phone", is("Le numéro de téléphone est obligatoire")));
    }

    @Test
    @Order(15)
    void itShouldNotSaveWhenCustomerPhoneLowerThan9Digits() throws Exception {
        // Given
        Customer customer = Customer.builder()
                .name("Moudjames23")
                .phone("6230000")
                .email("ousmane@gmail.com")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers/")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(customer))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("Erreurs")))
                .andExpect(jsonPath("$.errors.phone", is("Le numéro de téléphone doit avoir 9 chiffres")));
    }

}
