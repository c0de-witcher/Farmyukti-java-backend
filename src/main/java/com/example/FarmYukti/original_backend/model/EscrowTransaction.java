package com.example.FarmYukti.original_backend.model;

import com.example.FarmYukti.original_backend.model.enums.EscrowStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "escrow_transactions")
@Data
@NoArgsConstructor
public class EscrowTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false) private String providerTxnId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EscrowStatus status;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}