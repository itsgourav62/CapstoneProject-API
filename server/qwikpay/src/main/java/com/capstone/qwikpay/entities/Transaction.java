/*
 * package com.capstone.qwikpay.entities;
 * 
 * import java.time.LocalDateTime;
 * 
 * import jakarta.persistence.Entity; import jakarta.persistence.GeneratedValue;
 * import jakarta.persistence.GenerationType; import jakarta.persistence.Id;
 * import jakarta.persistence.Table; import lombok.AllArgsConstructor; import
 * lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter; import
 * lombok.ToString;
 * 
 * // Retrieve all the details and any payment references randomly generated //
 * Validation // TimeStamp
 * 
 * 
 * @AllArgsConstructor
 * 
 * @NoArgsConstructor
 * 
 * @Setter
 * 
 * @Getter
 * 
 * @ToString
 * 
 * @Entity
 * 
 * @Table(name="transaction_details") public class Transaction {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO) private Integer Id; private
 * Integer PaymentId; private LocalDateTime TimeStamp; private String Status;
 * 
 * 
 * 
 * 
 * }
 */

package com.capstone.qwikpay.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer getBillAccountId() {
		return billAccountId;
	}

	public void setBillAccountId(Integer billAccountId) {
		this.billAccountId = billAccountId;
	}

	private Long userId;  // The user paying the bill (payer)
    
    private String billType;  // Type of bill (e.g., "Water", "Electricity")
    
    private Integer billAccountId;  // Unique identifier for the bill (e.g., account number, customer ID)
    
    @Column(nullable = false)
    private BigDecimal amount;  // The amount to be paid
    
    @Column(unique = true, nullable = false)
    private Long paymentId;  // Unique payment identifier for the transaction

    private String status;  // Status of the transaction (e.g., "PENDING", "COMPLETED", "FAILED")

    private LocalDateTime createdAt;  // Transaction creation time
    private LocalDateTime updatedAt;  // Last update time for transaction

    public Transaction() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Transaction(Long payerId, String billType, String billAccountId, BigDecimal amount, Long paymentId, String status) {
        this.userId = userId;
        this.billType = billType;
        this.amount = amount;
        this.paymentId = paymentId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setuUserId(Long userId) {
        this.userId = userId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}