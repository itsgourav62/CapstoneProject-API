package com.capstone.qwikpay.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name="user_details")
public class User {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
	
	
	private Integer UserId;
	private String UserName;
	private String Password;
	private String Email;
	private String FirstName;
	private String LastName;
	private Integer MobileNumber;
	private LocalDateTime CreatedAt;
	private LocalDateTime UpdatedAt;
	

}
