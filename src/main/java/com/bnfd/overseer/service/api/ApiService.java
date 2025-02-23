package com.bnfd.overseer.service.api;

import com.bnfd.overseer.model.persistence.libraries.*;
import com.bnfd.overseer.model.persistence.servers.*;

import java.util.*;

public interface ApiService {
    List<LibraryEntity> getLibraries(ServerEntity server);
}
