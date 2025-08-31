package com.capstone.qwikpay.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Generated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "payment_details")
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pmtId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "payment_status", length = 50, nullable = false)
    private String paymentStatus;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "bill_id", nullable = false, unique = true)
    @JsonIgnore // Prevent full serialization of the Bill object
    private Bill bill;
    
    // Manual getters and setters to ensure they're available
    public Integer getPmtId() {
        return pmtId;
    }

    public void setPmtId(Integer pmtId) {
        this.pmtId = pmtId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
    
    @JsonProperty("bill_id")
    public Integer getBillId() {
        return bill != null ? bill.getBillId() : null;
    }

    public void setBillId(Integer billId) {
        this.bill = new Bill(billId); // Create Bill object with just billId
    }
}
