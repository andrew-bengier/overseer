package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.CollectionBuilderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionBuilderRepository extends JpaRepository<CollectionBuilderEntity, String> {
}
