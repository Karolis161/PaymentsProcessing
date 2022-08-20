package com.balticamadeus.payments.entities;

import javax.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_sequence"
    )

    private int id;
    private int paymentId;
    private String status;

    public Notification() {
    }

    public Notification(int id, String status, int paymentId) {
        this.id = id;
        this.status = status;
        this.paymentId = paymentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
}
