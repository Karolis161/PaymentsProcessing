package com.balticamadeus.payments.entities;

public class CancellationDetails {

    public int id;
    public double cancellationFee;

    public CancellationDetails(int id, double cancellationFee) {
        this.id = id;
        this.cancellationFee = cancellationFee;
    }
}
