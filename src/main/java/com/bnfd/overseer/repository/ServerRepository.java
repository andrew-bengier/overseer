package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.ServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<ServerEntity, String> {
}
