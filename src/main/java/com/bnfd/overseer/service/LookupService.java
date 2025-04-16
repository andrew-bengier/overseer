package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.model.api.BuilderOption;
import com.bnfd.overseer.model.persistence.BuilderOptionEntity;
import com.bnfd.overseer.repository.BuilderOptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class LookupService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final BuilderOptionRepository builderRepository;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public LookupService(@Qualifier("overseer-mapper") ModelMapper overseerMapper, BuilderOptionRepository builderRepository) {
        this.overseerMapper = overseerMapper;
        this.builderRepository = builderRepository;
    }
    // endregion - Constructors -

    // region - READ -
    public List<BuilderOption> getAllBuilderOptions() {
        List<BuilderOptionEntity> entities = builderRepository.findAll();

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No builder options found");
        }

        return overseerMapper.map(entities, new TypeToken<List<BuilderOption>>() {
        }.getType());
    }
    // endregion - READ -
}
