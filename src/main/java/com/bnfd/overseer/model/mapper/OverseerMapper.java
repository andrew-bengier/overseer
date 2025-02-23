package com.bnfd.overseer.model.mapper;

import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.model.media.plex.*;
import com.bnfd.overseer.model.persistence.apikeys.*;
import com.bnfd.overseer.model.persistence.libraries.*;
import com.bnfd.overseer.model.persistence.servers.*;
import org.modelmapper.*;
import org.modelmapper.spi.*;
import org.springframework.context.annotation.*;
import org.springframework.util.*;

import java.util.*;
import java.util.stream.*;

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

        // region - Setting -
        mapper.addConverter(serverSettingModelToEntity());
        mapper.addConverter(serverSettingEntityToModel());
        mapper.addConverter(librarySettingModelToEntity());
        mapper.addConverter(librarySettingEntityToModel());
        // endregion - Setting -

        // region - Action -
        mapper.addConverter(libraryActionModelToEntity());
        mapper.addConverter(libraryActionEntityToModel());
        // endregion - Action -

        // region - Server -
        mapper.addConverter(serverModelToEntity());
        mapper.addConverter(serverEntityToModel());
        // endregion - Server -

        // region - Library -
        mapper.addConverter(libraryModelToEntity());
        mapper.addConverter(libraryEntityToModel());
        // endregion - Library -

        // region - Plex API -
        mapper.addConverter(plexDirectoryToLibraryEntity());
        // endregion - Plex API -

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

    // region -- Setting --
    // region --- Server ---
    private Converter<Setting, ServerSettingEntity> serverSettingModelToEntity() {
        return new Converter<Setting, ServerSettingEntity>() {
            @Override
            public ServerSettingEntity convert(MappingContext<Setting, ServerSettingEntity> mappingContext) {
                ServerSettingEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new ServerSettingEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setType(mappingContext.getSource().getType().name());
                entity.setName(mappingContext.getSource().getName());
                entity.setVal(mappingContext.getSource().getVal());

                return entity;
            }
        };
    }

    private Converter<ServerSettingEntity, Setting> serverSettingEntityToModel() {
        return new Converter<ServerSettingEntity, Setting>() {
            @Override
            public Setting convert(MappingContext<ServerSettingEntity, Setting> mappingContext) {
                Setting model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Setting();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setType(SettingType.valueOf(mappingContext.getSource().getType().toUpperCase()));
                model.setName(mappingContext.getSource().getName());
                model.setVal(mappingContext.getSource().getVal());

                return model;
            }
        };
    }
    // endregion --- Server ---

    // region --- Library ---
    private Converter<Setting, LibrarySettingEntity> librarySettingModelToEntity() {
        return new Converter<Setting, LibrarySettingEntity>() {
            @Override
            public LibrarySettingEntity convert(MappingContext<Setting, LibrarySettingEntity> mappingContext) {
                LibrarySettingEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new LibrarySettingEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setType(mappingContext.getSource().getType().name());
                entity.setName(mappingContext.getSource().getName());
                entity.setVal(mappingContext.getSource().getVal());

                return entity;
            }
        };
    }

    private Converter<LibrarySettingEntity, Setting> librarySettingEntityToModel() {
        return new Converter<LibrarySettingEntity, Setting>() {
            @Override
            public Setting convert(MappingContext<LibrarySettingEntity, Setting> mappingContext) {
                Setting model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Setting();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setType(SettingType.valueOf(mappingContext.getSource().getType().toUpperCase()));
                model.setName(mappingContext.getSource().getName());
                model.setVal(mappingContext.getSource().getVal());

                return model;
            }
        };
    }
    // endregion --- Library ---
    // endregion -- Setting --

    // region -- Action --
    // region --- Library ---
    private Converter<Action, LibraryActionEntity> libraryActionModelToEntity() {
        return new Converter<Action, LibraryActionEntity>() {
            @Override
            public LibraryActionEntity convert(MappingContext<Action, LibraryActionEntity> mappingContext) {
                LibraryActionEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new LibraryActionEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setType(mappingContext.getSource().getType().name());
                entity.setName(mappingContext.getSource().getName());
                entity.setVal(mappingContext.getSource().getVal());

                return entity;
            }
        };
    }

    private Converter<LibraryActionEntity, Action> libraryActionEntityToModel() {
        return new Converter<LibraryActionEntity, Action>() {
            @Override
            public Action convert(MappingContext<LibraryActionEntity, Action> mappingContext) {
                Action model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Action();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setType(ActionType.valueOf(mappingContext.getSource().getType().toUpperCase()));
                model.setName(mappingContext.getSource().getName());
                model.setVal(mappingContext.getSource().getVal());

                return model;
            }
        };
    }
    // endregion --- Library ---
    // endregion -- Action --

    // region -- Server --
    private Converter<Server, ServerEntity> serverModelToEntity() {
        return new Converter<Server, ServerEntity>() {
            @Override
            public ServerEntity convert(MappingContext<Server, ServerEntity> mappingContext) {
                ServerEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new ServerEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setName(mappingContext.getSource().getName());
                if (!ObjectUtils.isEmpty(mappingContext.getSource().getApiKey())) {
                    entity.setApiKey(map(mappingContext.getSource().getApiKey(), ApiKeyEntity.class));
                }
                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    entity.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, ServerSettingEntity.class)).collect(Collectors.toSet()));
                }
                if (!CollectionUtils.isEmpty(mappingContext.getSource().getLibraries())) {
                    entity.setLibraries(mappingContext.getSource().getLibraries().stream().map(library -> map(library, LibraryEntity.class)).collect(Collectors.toSet()));
                }

                return entity;
            }
        };
    }

    private Converter<ServerEntity, Server> serverEntityToModel() {
        return new Converter<ServerEntity, Server>() {
            @Override
            public Server convert(MappingContext<ServerEntity, Server> mappingContext) {
                Server model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Server();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setName(mappingContext.getSource().getName());
                if (!ObjectUtils.isEmpty(mappingContext.getSource().getApiKey())) {
                    model.setApiKey(map(mappingContext.getSource().getApiKey(), ApiKey.class));
                }
                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    model.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, Setting.class)).collect(Collectors.toCollection(TreeSet::new)));
                }
                if (!CollectionUtils.isEmpty(mappingContext.getSource().getLibraries())) {
                    model.setLibraries(mappingContext.getSource().getLibraries().stream().map(library -> map(library, Library.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                return model;
            }
        };
    }
    // endregion -- Server --

    // region -- Library --
    private Converter<Library, LibraryEntity> libraryModelToEntity() {
        return new Converter<Library, LibraryEntity>() {
            @Override
            public LibraryEntity convert(MappingContext<Library, LibraryEntity> mappingContext) {
                LibraryEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new LibraryEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setReferenceId(mappingContext.getSource().getReferenceId());
                entity.setType(mappingContext.getSource().getType());
                entity.setName(mappingContext.getSource().getName());
                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    entity.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, LibrarySettingEntity.class)).collect(Collectors.toSet()));
                }

                return entity;
            }
        };
    }

    private Converter<LibraryEntity, Library> libraryEntityToModel() {
        return new Converter<LibraryEntity, Library>() {
            @Override
            public Library convert(MappingContext<LibraryEntity, Library> mappingContext) {
                Library model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Library();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setReferenceId(mappingContext.getSource().getReferenceId());
                model.setType(mappingContext.getSource().getType());
                model.setName(mappingContext.getSource().getName());
                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    model.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, Setting.class)).collect(Collectors.toSet()));
                }

                return model;
            }
        };
    }
    // endregion -- Library --

    // region - Plex API -
    // Directory - Library
    private Converter<Directory, LibraryEntity> plexDirectoryToLibraryEntity() {
        return new Converter<Directory, LibraryEntity>() {
            @Override
            public LibraryEntity convert(MappingContext<Directory, LibraryEntity> mappingContext) {
                LibraryEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new LibraryEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setReferenceId(mappingContext.getSource().getKey());
                entity.setType(mappingContext.getSource().getType());
                entity.setName(mappingContext.getSource().getTitle());

                return entity;
            }
        };
    }
    // endregion - Plex API -
    // endregion - Converters -
}
