package org.example.airbnbbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.airbnbbackend.models.enums.PaymentStatus;
import org.hibernate.annotations.SecondaryRow;

import java.math.BigDecimal;

@Getter
@Setter
@Entity

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal amount;
}
