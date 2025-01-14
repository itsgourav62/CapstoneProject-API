package com.capstone.qwikpay.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "bill_details")
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from serialization
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer billId;

    private Integer amount;

    @Column(name = "bill_status", length = 50)
    private String billStatus;

    @Column(length = 255)
    private String description;

    @Column(name = "due_date")
    @JsonProperty("due_date") // Rename for JSON output
    private LocalDateTime dueDate;

    @Column(name = "created_at", updatable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @JsonIgnore // Prevent serialization of user
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore // Prevent serialization of payment if not needed
    private Payment payment;
}
