package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.model.persistence.libraries.*;
import com.bnfd.overseer.repository.*;
import lombok.extern.slf4j.*;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.util.*;

@Slf4j
@Service
public class LookupService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final LibraryActionRepository libraryActionRepository;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public LookupService(@Qualifier("overseer-mapper") ModelMapper overseerMapper, LibraryActionRepository libraryActionRepository) {
        this.overseerMapper = overseerMapper;
        this.libraryActionRepository = libraryActionRepository;
    }
    // endregion - Constructors -

    public List<Action> getActionsByCategory(String category) {
        switch (ActionCategory.valueOf(category.toUpperCase())) {
            case ActionCategory.LIBRARY:
                List<LibraryActionEntity> entities = libraryActionRepository.findAll();

                if (CollectionUtils.isEmpty(entities)) {
                    throw new OverseerNoContentException("No action found matching provided search params");
                }

                // [TEST]
                log.debug("Actions: ", entities.size());

                return overseerMapper.map(entities, new TypeToken<List<Action>>() {
                }.getType());
            default:
                throw new OverseerPreConditionRequiredException("Error - action category not currently supported");
        }
    }
}
