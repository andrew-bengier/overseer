package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.libraries.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface LibraryActionRepository extends JpaRepository<LibraryActionEntity, String> {
}
