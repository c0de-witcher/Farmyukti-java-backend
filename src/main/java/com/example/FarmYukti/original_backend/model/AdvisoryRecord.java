package com.example.FarmYukti.original_backend.model;

import com.example.FarmYukti.original_backend.model.enums.RecommendationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "advisory_records")
@Data
@NoArgsConstructor
public class AdvisoryRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "land_parcel_id")
    private LandParcel landParcel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RecommendationType recommendationType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> recommendationData;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}