package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.persistence.apikeys.*;
import com.bnfd.overseer.repository.*;
import jakarta.persistence.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import java.util.*;

@Slf4j
@Service
public class ApiKeyService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;

    private final ApiKeyRepository apiKeyRepository;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public ApiKeyService(@Qualifier("overseer-mapper") ModelMapper overseerMapper, ApiKeyRepository apiKeyRepository) {
        this.overseerMapper = overseerMapper;
        this.apiKeyRepository = apiKeyRepository;
    }
    // endregion - Constructors -

    // region - CREATE -
    @Transactional
    public ApiKey addApiKey(ApiKey apiKey) {
        ApiKeyEntity entity = overseerMapper.map(apiKey, ApiKeyEntity.class);
        entity.setId(UUID.randomUUID().toString());

        try {
            entity = apiKeyRepository.save(entity);
        } catch (PersistenceException | DataIntegrityViolationException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        // [TEST]
        log.debug("ApiKey added");

        return overseerMapper.map(entity, ApiKey.class);
    }
    // endregion - CREATE -

    // region - READ -
    public ApiKey getApiKeyById(String id) {
        ApiKeyEntity entity = apiKeyRepository.findById(id).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No ApiKey found with id: [%s]", id));
        }

        // [TEST]
        log.debug("ApiKey: ", entity);

        return overseerMapper.map(entity, ApiKey.class);
    }

    // TODO: convert to include all search params (repository needs specs)
    public List<ApiKey> getAllApiKeysByName(String name) {
        List<ApiKeyEntity> entities = apiKeyRepository.findAllByName(name);

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No api keys found matching provided search params");
        }

        // [TEST]
        log.debug("ApiKeys: ", entities.size());

        return overseerMapper.map(entities, new TypeToken<List<ApiKey>>() {
        }.getType());
    }

    public List<ApiKey> getAllApiKeys() {
        List<ApiKeyEntity> entities = apiKeyRepository.findAll();

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No api keys found");
        }

        // [TEST]
        log.debug("ApiKeys: ", entities.size());

        return overseerMapper.map(entities, new TypeToken<List<ApiKey>>() {
        }.getType());
    }
    // endregion - READ -

    // region - UPDATE -
    @Transactional
    public ApiKey updateApiKey(ApiKey apiKey) {
        ApiKeyEntity entity = apiKeyRepository.findById(apiKey.getId()).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No ApiKey found with id: [%s]", apiKey.getId()));
        }

        entity = overseerMapper.map(apiKey, ApiKeyEntity.class);

        try {
            entity = apiKeyRepository.save(entity);
        } catch (PersistenceException | DataIntegrityViolationException exception) {
            throw new OverseerConflictException(exception.getMessage());
        }

        // [TEST]
        log.debug("ApiKey updated");

        return overseerMapper.map(entity, ApiKey.class);
    }
    // endregion - UPDATE -

    // region - DELETE -
    @Transactional
    public void removeApiKey(String id) {
        try {
            apiKeyRepository.deleteById(id);

            // [TEST]
            log.debug("ApiKey removed");
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No ApiKeys matching provided id found");
        }
    }
    // endregion - DELETE -
}
