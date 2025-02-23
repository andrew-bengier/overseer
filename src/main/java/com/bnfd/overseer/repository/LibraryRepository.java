package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.libraries.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, String> {
    List<LibraryEntity> findAllByServerId(String id);
}
