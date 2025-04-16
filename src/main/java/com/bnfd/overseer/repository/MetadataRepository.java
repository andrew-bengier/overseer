package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.MetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<MetadataEntity, String> {
}
