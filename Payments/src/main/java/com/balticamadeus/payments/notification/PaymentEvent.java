package com.balticamadeus.payments.notification;

public class PaymentEvent {

    private int paymentId;

    public PaymentEvent(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPaymentId() {
        return paymentId;
    }
}
