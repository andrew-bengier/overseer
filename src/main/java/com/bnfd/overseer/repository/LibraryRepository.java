package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.LibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, String> {
    List<LibraryEntity> findAllByServerId(String serverId);
}
