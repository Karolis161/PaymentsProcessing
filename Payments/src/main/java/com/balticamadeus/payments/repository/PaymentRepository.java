package com.balticamadeus.payments.repository;

import com.balticamadeus.payments.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findPaymentDateById(int id);

    List<Payment> findByPaymentValidityAndPaymentType(String paymentValidity, String paymentType);
}