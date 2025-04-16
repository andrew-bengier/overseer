package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.ActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, String> {
}
