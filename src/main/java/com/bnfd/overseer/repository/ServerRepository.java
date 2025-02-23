package com.bnfd.overseer.repository;

import com.bnfd.overseer.model.persistence.servers.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface ServerRepository extends JpaRepository<ServerEntity, String> {
}
