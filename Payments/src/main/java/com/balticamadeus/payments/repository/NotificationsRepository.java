package com.balticamadeus.payments.repository;

import com.balticamadeus.payments.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, Integer> {
}
