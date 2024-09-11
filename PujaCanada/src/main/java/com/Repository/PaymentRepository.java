package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}