package com.capstone.qwikpay.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;
import lombok.Generated;

@Generated
@Entity
@Table(name = "bill_details")
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from serialization
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer billId;

    private Integer amount;

    private String billType;

    @Column(name = "bill_status", length = 50)
    private String billStatus;

    @Column(length = 255)
    private String description;

    @Column(name = "due_date")
    @JsonProperty("due_date") // Rename for JSON output
    private LocalDateTime dueDate;

    @Column(name = "created_at", updatable = false)
    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pmt_id", referencedColumnName = "pmtId", unique = true)
    @JsonIgnore
    private Payment payment;

    // Default constructor for JPA and tests
    public Bill() {
    }

    // Custom constructor for deserialization
    public Bill(Integer billId) {
        this.billId = billId;
    }

    // Manual getters and setters to ensure they're available
    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
