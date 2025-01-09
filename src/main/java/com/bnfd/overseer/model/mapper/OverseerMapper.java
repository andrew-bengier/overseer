package com.bnfd.overseer.model.mapper;

import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.persistence.*;
import org.modelmapper.*;
import org.modelmapper.spi.*;
import org.springframework.context.annotation.*;
import org.springframework.util.*;

@Configuration
public class OverseerMapper extends ModelMapper {
    // region - Beans -
    @Bean("overseer-mapper")
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();

        // region - ApiKey -
        mapper.addConverter(apiKeyModelToEntity());
        mapper.addConverter(apiKeyEntityToModel());
        // endregion - ApiKey -

        return mapper;
    }
    // endregion - Beans -

    // region - Converters -
    // region -- ApiKey --
    private Converter<ApiKey, ApiKeyEntity> apiKeyModelToEntity() {
        return new Converter<ApiKey, ApiKeyEntity>() {
            @Override
            public ApiKeyEntity convert(MappingContext<ApiKey, ApiKeyEntity> mappingContext) {
                ApiKeyEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new ApiKeyEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setName(mappingContext.getSource().getName());
                entity.setKey(mappingContext.getSource().getKey());
                entity.setUrl(mappingContext.getSource().getUrl());

                return entity;
            }
        };
    }

    private Converter<ApiKeyEntity, ApiKey> apiKeyEntityToModel() {
        return new Converter<ApiKeyEntity, ApiKey>() {
            @Override
            public ApiKey convert(MappingContext<ApiKeyEntity, ApiKey> mappingContext) {
                ApiKey model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new ApiKey();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setName(mappingContext.getSource().getName());
                model.setKey(mappingContext.getSource().getKey());
                model.setUrl(mappingContext.getSource().getUrl());

                return model;
            }
        };
    }
    // endregion -- ApiKey --
    // endregion - Converters -
}
