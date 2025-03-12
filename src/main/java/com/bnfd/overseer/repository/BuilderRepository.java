package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.BuilderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuilderRepository extends JpaRepository<BuilderEntity, String> {
}
