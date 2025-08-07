package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerConflictException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.exception.OverseerNotFoundException;
import com.bnfd.overseer.model.api.ApiKey;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.model.persistence.ApiKeyEntity;
import com.bnfd.overseer.repository.ApiKeyRepository;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

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
        //log.debug("ApiKey added");

        return overseerMapper.map(entity, ApiKey.class);
    }
    // endregion - CREATE -

    // region - READ -
    public ApiKey getApiKeyById(String id) {
        ApiKeyEntity entity = apiKeyRepository.findById(id).orElse(null);

        if (ObjectUtils.isEmpty(entity)) {
            throw new OverseerNotFoundException(String.format("No ApiKey found with id: [%s]", id));
        }

        return overseerMapper.map(entity, ApiKey.class);
    }

    public List<ApiKey> getAllApiKeysByName(ApiKeyType name) {
        List<ApiKeyEntity> entities = apiKeyRepository.findAllByName(name);

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No api keys found matching provided search params");
        }

        return overseerMapper.map(entities, new TypeToken<List<ApiKey>>() {
        }.getType());
    }

    public List<ApiKey> getAllApiKeys() {
        List<ApiKeyEntity> entities = apiKeyRepository.findAll();

        if (CollectionUtils.isEmpty(entities)) {
            throw new OverseerNoContentException("No api keys found");
        }

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
        //log.debug("ApiKey updated");

        return overseerMapper.map(entity, ApiKey.class);
    }
    // endregion - UPDATE -

    // region - DELETE -
    @Transactional
    public void removeApiKey(String id) {
        try {
            apiKeyRepository.deleteById(id);

            // [TEST]
            //log.debug("ApiKey removed");
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No ApiKeys matching provided id found");
        }
    }
    // endregion - DELETE -
}
