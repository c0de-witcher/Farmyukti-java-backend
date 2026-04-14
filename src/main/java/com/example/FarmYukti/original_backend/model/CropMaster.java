package com.example.FarmYukti.original_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "crop_master")
@Data
@NoArgsConstructor
public class CropMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100) private String name;
    @Column(length = 100) private String variety;
    private String defaultImageUrl;
}
