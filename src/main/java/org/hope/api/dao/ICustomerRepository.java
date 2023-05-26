package org.hope.api.dao;

import org.hope.api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(@Param("email") String email);
}
