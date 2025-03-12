package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<SettingEntity, String> {
}
