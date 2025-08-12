package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.model.persistence.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, String> {
    List<ApiKeyEntity> findAllByType(ApiKeyType type);
}
