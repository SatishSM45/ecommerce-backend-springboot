package org.example.ecommerce.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.example.ecommerce.enums.PaymentStatus;

@Entity
@Data
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String paymentMode; // CARD, UPI, NET_BANKING
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionId;

}

