package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.apikeys.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, String> {
    List<ApiKeyEntity> findAllByName(String name);
}
