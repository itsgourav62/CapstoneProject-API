package com.capstone.qwikpay.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.*;

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

    private Integer billId;

    @Column(name = "payment_status", length = 50, nullable = false)
    private String paymentStatus;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @OneToOne(mappedBy = "payment")
    @JsonBackReference // Prevent cyclic serialization between Bill and Payment
    private Bill bill;

 
}
