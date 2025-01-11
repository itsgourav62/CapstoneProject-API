package com.capstone.qwikpay.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="bill_details")

public class Bill {
	//UserID
	//StatusofBill
	//Payment
	@Id
	private Integer Id;
	private Integer UserId;
	private Integer Amount;
	private String BillStatus;
	private String Description;
	private LocalDateTime DueDate;
	private LocalDateTime PaymentDate;
	private LocalDateTime CreatedAt;
	private LocalDateTime UpdatedAt;
	
}
