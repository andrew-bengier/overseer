package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.options.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface SettingOptionRepository extends JpaRepository<SettingOptionEntity, String> {
}
