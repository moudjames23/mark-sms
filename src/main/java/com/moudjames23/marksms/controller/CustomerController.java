package com.moudjames23.marksms.controller;

import com.moudjames23.marksms.model.HttpResponse;
import com.moudjames23.marksms.model.entities.Customer;
import com.moudjames23.marksms.model.requests.CustomerRequest;
import com.moudjames23.marksms.model.responses.CustomerResponse;
import com.moudjames23.marksms.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<HttpResponse> list() {

        List<CustomerResponse> responses = customerService.index().stream()
                .map(customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .email(customer.getEmail())
                        .phone(customer.getPhone())
                        .build())
                .collect(Collectors.toList());

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Liste des clients")
                .data((Map.of("clients", responses)))
                .build();

        return ResponseEntity.ok(httpResponse);
    }

    @PostMapping
    public ResponseEntity<HttpResponse> create(@Valid @RequestBody CustomerRequest customerRequest) {

        Customer customer = this.customerService.create(modelMapper.map(customerRequest, Customer.class));

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Client cree avec succes")
                .data((Map.of("client", modelMapper.map(customer, CustomerResponse.class))))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(httpResponse);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<HttpResponse> show(@PathVariable("customerId") Long customerId)
    {
        Customer customer = this.customerService.show(customerId);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Information sur le client")
                .data((Map.of("client", modelMapper.map(customer, CustomerResponse.class))))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<HttpResponse> update(@Valid @RequestBody CustomerRequest request, @PathVariable("customerId") Long customerId)
    {
        Customer customer = this.customerService.update(modelMapper.map(request, Customer.class), customerId);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Mise à jour du client")
                .data((Map.of("client", modelMapper.map(customer, CustomerResponse.class))))
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<HttpResponse> delete(@PathVariable("customerId") Long customerId)
    {
        this.customerService.delete(customerId);

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Client supprime avec succes")
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);
    }
}
