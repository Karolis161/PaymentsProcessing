package com.balticamadeus.payments.entities;

import com.balticamadeus.payments.validation.FirstValidation;
import com.balticamadeus.payments.validation.SecondValidation;
import com.balticamadeus.payments.validation.ThirdValidation;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @SequenceGenerator(
            name = "payment_sequence",
            sequenceName = "payment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payment_sequence"
    )

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String paymentType;

    @NotNull(groups = {FirstValidation.class, SecondValidation.class, ThirdValidation.class})
    @DecimalMin("0.0")
    private double paymentAmount;

    @NotBlank(groups = {ThirdValidation.class})
    private String paymentCurrency;

    @NotBlank(groups = {FirstValidation.class, SecondValidation.class, ThirdValidation.class})
    private String debtor_iban;

    @NotBlank(groups = {FirstValidation.class, SecondValidation.class, ThirdValidation.class})
    private String creditor_iban;

    @NotBlank(groups = {FirstValidation.class})
    private String paymentDetails;

    @NotBlank(groups = {ThirdValidation.class})
    private String creditorBankBIC;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String paymentValidity;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime paymentDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private double paymentCancellationFee;

    public Payment() {
    }

    public Payment(int id, String paymentType, double paymentAmount, String paymentCurrency,
                   String debtor_iban, String creditor_iban, String paymentDetails, String creditorBankBIC,
                   String paymentValidity, LocalDateTime paymentDate, double paymentCancellationFee) {
        this.id = id;
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
        this.paymentCurrency = paymentCurrency;
        this.debtor_iban = debtor_iban;
        this.creditor_iban = creditor_iban;
        this.paymentDetails = paymentDetails;
        this.creditorBankBIC = creditorBankBIC;
        this.paymentValidity = paymentValidity;
        this.paymentDate = paymentDate;
        this.paymentCancellationFee = paymentCancellationFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getDebtor_iban() {
        return debtor_iban;
    }

    public void setDebtor_iban(String debtor_iban) {
        this.debtor_iban = debtor_iban;
    }

    public String getCreditor_iban() {
        return creditor_iban;
    }

    public void setCreditor_iban(String creditor_iban) {
        this.creditor_iban = creditor_iban;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getCreditorBankBIC() {
        return creditorBankBIC;
    }

    public void setCreditorBankBIC(String creditorBankBIC) {
        this.creditorBankBIC = creditorBankBIC;
    }

    public String getPaymentValidity() {
        return paymentValidity;
    }

    public void setPaymentValidity(String paymentValidity) {
        this.paymentValidity = paymentValidity;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPaymentCancellationFee() {
        return paymentCancellationFee;
    }

    public void setPaymentCancellationFee(double paymentCancellationFee) {
        this.paymentCancellationFee = paymentCancellationFee;
    }
}
