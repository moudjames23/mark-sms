package com.moudjames23.marksms.repository;

import com.moudjames23.marksms.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByEmail(String email);

}
