package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.BuilderOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuilderOptionRepository extends JpaRepository<BuilderOptionEntity, String> {
}
