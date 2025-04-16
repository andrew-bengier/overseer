package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, String> {
}
