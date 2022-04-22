package com.qtelecom.call_recorder.repositories;

import com.qtelecom.call_recorder.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
