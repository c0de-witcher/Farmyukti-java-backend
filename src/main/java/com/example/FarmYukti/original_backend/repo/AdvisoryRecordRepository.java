package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.AdvisoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdvisoryRecordRepository extends JpaRepository<AdvisoryRecord, Long> {
    List<AdvisoryRecord> findByFarmerId(UUID farmerId);
}
