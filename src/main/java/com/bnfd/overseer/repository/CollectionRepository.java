package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionEntity, String> {
    List<CollectionEntity> findAllByLibraryId(String libraryId);
}
