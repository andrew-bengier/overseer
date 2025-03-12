package com.bnfd.overseer.service.api;

import com.bnfd.overseer.model.persistence.LibraryEntity;
import com.bnfd.overseer.model.persistence.ServerEntity;

import java.util.List;

public interface ApiService {
    List<LibraryEntity> getLibraries(ServerEntity server);
}
