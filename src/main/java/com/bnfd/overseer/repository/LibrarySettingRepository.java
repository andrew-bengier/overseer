package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.libraries.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface LibrarySettingRepository extends JpaRepository<LibrarySettingEntity, String> {
}
