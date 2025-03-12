package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.model.api.Builder;
import com.bnfd.overseer.model.persistence.BuilderEntity;
import com.bnfd.overseer.repository.BuilderRepository;
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

    private final BuilderRepository builderRepository;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public LookupService(@Qualifier("overseer-mapper") ModelMapper overseerMapper, BuilderRepository builderRepository) {
        this.overseerMapper = overseerMapper;
        this.builderRepository = builderRepository;
    }
    // endregion - Constructors -

    // region - READ -
    public List<Builder> getAllBuilders() {
        List<BuilderEntity> entities = builderRepository.findAll();

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No builders found");
        }

        return overseerMapper.map(entities, new TypeToken<List<Builder>>() {
        }.getType());
    }
    // endregion - READ -
}
