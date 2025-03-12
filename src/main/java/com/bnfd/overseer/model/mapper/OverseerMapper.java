package com.bnfd.overseer.model.mapper;

import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.media.plex.Directory;
import com.bnfd.overseer.model.persistence.*;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
        mapper.addConverter(settingModelToEntity());
        mapper.addConverter(settingEntityToModel());
        // endregion - Setting -

        // region - Action -
        mapper.addConverter(actionModelToEntity());
        mapper.addConverter(actionEntityToModel());
        // endregion - Action -

        // region - Server -
        mapper.addConverter(serverModelToEntity());
        mapper.addConverter(serverEntityToModel());
        // endregion - Server -

        // region - Library -
        mapper.addConverter(libraryModelToEntity());
        mapper.addConverter(libraryEntityToModel());
        // endregion - Library -

        // region - Collection -
        mapper.addConverter(collectionModelToEntity());
        mapper.addConverter(collectionEntityToModel());
        // endregion - Collection -

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
    private Converter<Setting, SettingEntity> settingModelToEntity() {
        return new Converter<Setting, SettingEntity>() {
            @Override
            public SettingEntity convert(MappingContext<Setting, SettingEntity> mappingContext) {
                SettingEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new SettingEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setType(mappingContext.getSource().getType());
                entity.setName(mappingContext.getSource().getName());
                entity.setVal(mappingContext.getSource().getVal());

                return entity;
            }
        };
    }

    private Converter<SettingEntity, Setting> settingEntityToModel() {
        return new Converter<SettingEntity, Setting>() {
            @Override
            public Setting convert(MappingContext<SettingEntity, Setting> mappingContext) {
                Setting model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Setting();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setType(mappingContext.getSource().getType());
                model.setName(mappingContext.getSource().getName());
                model.setVal(mappingContext.getSource().getVal());

                return model;
            }
        };
    }
    // endregion -- Setting --

    // region -- Action --
    private Converter<Action, ActionEntity> actionModelToEntity() {
        return new Converter<Action, ActionEntity>() {
            @Override
            public ActionEntity convert(MappingContext<Action, ActionEntity> mappingContext) {
                ActionEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new ActionEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setType(mappingContext.getSource().getType());
                entity.setName(mappingContext.getSource().getName());
                entity.setVal(mappingContext.getSource().getVal());

                return entity;
            }
        };
    }

    private Converter<ActionEntity, Action> actionEntityToModel() {
        return new Converter<ActionEntity, Action>() {
            @Override
            public Action convert(MappingContext<ActionEntity, Action> mappingContext) {
                Action model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Action();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setType(mappingContext.getSource().getType());
                model.setName(mappingContext.getSource().getName());
                model.setVal(mappingContext.getSource().getVal());

                return model;
            }
        };
    }
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
                    Set<SettingEntity> settings = mappingContext.getSource().getSettings().stream().map(setting -> map(setting, SettingEntity.class)).collect(Collectors.toSet());
                    settings.forEach(setting -> setting.setReferenceId(entity.getId()));
                    entity.setSettings(settings);
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getActions())) {
                    Set<ActionEntity> actions = mappingContext.getSource().getActions().stream().map(action -> map(action, ActionEntity.class)).collect(Collectors.toSet());
                    actions.forEach(action -> action.setReferenceId(entity.getId()));
                    entity.setActions(actions);
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getLibraries())) {
                    Set<LibraryEntity> libraries = mappingContext.getSource().getLibraries().stream().map(library -> map(library, LibraryEntity.class)).collect(Collectors.toSet());
                    libraries.forEach(library -> library.setServerId(entity.getId()));
                    entity.setLibraries(libraries);
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

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getActions())) {
                    model.setActions(mappingContext.getSource().getActions().stream().map(action -> map(action, Action.class)).collect(Collectors.toCollection(TreeSet::new)));
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
                entity.setServerId(mappingContext.getSource().getServerId());
                entity.setExternalId(mappingContext.getSource().getReferenceId());
                entity.setType(mappingContext.getSource().getType());
                entity.setName(mappingContext.getSource().getName());

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    Set<SettingEntity> settings = mappingContext.getSource().getSettings().stream().map(setting -> map(setting, SettingEntity.class)).collect(Collectors.toSet());
                    settings.forEach(setting -> setting.setReferenceId(entity.getId()));
                    entity.setSettings(settings);
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getActions())) {
                    Set<ActionEntity> actions = mappingContext.getSource().getActions().stream().map(action -> map(action, ActionEntity.class)).collect(Collectors.toSet());
                    actions.forEach(action -> action.setReferenceId(entity.getId()));
                    entity.setActions(actions);
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getCollections())) {
                    Set<CollectionEntity> collections = mappingContext.getSource().getCollections().stream().map(collection -> map(collection, CollectionEntity.class)).collect(Collectors.toSet());
                    collections.forEach(collection -> collection.setLibraryId(entity.getId()));
                    entity.setCollections(collections);
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
                model.setServerId(mappingContext.getSource().getServerId());
                model.setReferenceId(mappingContext.getSource().getExternalId());
                model.setType(mappingContext.getSource().getType());
                model.setName(mappingContext.getSource().getName());

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    model.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, Setting.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getActions())) {
                    model.setActions(mappingContext.getSource().getActions().stream().map(action -> map(action, Action.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getCollections())) {
                    model.setCollections(mappingContext.getSource().getCollections().stream().map(collection -> map(collection, Collection.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                return model;
            }
        };
    }
    // endregion -- Library --

    // region - Collection -
    public Converter<Collection, CollectionEntity> collectionModelToEntity() {
        return new Converter<Collection, CollectionEntity>() {
            @Override
            public CollectionEntity convert(MappingContext<Collection, CollectionEntity> mappingContext) {
                CollectionEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new CollectionEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setLibraryId(mappingContext.getSource().getLibraryId());
                entity.setExternalId(mappingContext.getSource().getReferenceId());
                entity.setName(mappingContext.getSource().getName());

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getBuilders())) {
                    Set<CollectionBuilderEntity> collectionBuilderEntities = new HashSet<>();

                    mappingContext.getSource().getBuilders().forEach(builder -> {
                        collectionBuilderEntities.add(
                                new CollectionBuilderEntity(
                                        builder.getReferenceId(),
                                        entity,
                                        new BuilderEntity(builder.getId(), builder.getType(), builder.getCategory(), builder.getName(), builder.getVersion()),
                                        builder.getAttributes()
                                )
                        );
                    });

                    entity.setBuilders(collectionBuilderEntities);
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    Set<SettingEntity> settings = mappingContext.getSource().getSettings().stream().map(setting -> map(setting, SettingEntity.class)).collect(Collectors.toSet());
                    settings.forEach(setting -> setting.setReferenceId(entity.getId()));
                    entity.setSettings(settings);
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getActions())) {
                    Set<ActionEntity> actions = mappingContext.getSource().getActions().stream().map(action -> map(action, ActionEntity.class)).collect(Collectors.toSet());
                    actions.forEach(action -> action.setReferenceId(entity.getId()));
                    entity.setActions(actions);
                }

                return entity;
            }
        };
    }

    public Converter<CollectionEntity, Collection> collectionEntityToModel() {
        return new Converter<CollectionEntity, Collection>() {
            @Override
            public Collection convert(MappingContext<CollectionEntity, Collection> mappingContext) {
                Collection model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Collection();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setLibraryId(mappingContext.getSource().getLibraryId());
                model.setReferenceId(mappingContext.getSource().getExternalId());
                model.setName(mappingContext.getSource().getName());

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getBuilders())) {
                    Set<Builder> builders = new HashSet<>();

                    mappingContext.getSource().getBuilders().forEach(builder -> {
                        builders.add(
                                new Builder(
                                        builder.getBuilder().getId(),
                                        builder.getId(),
                                        builder.getBuilder().getType(),
                                        builder.getBuilder().getCategory(),
                                        builder.getBuilder().getName(),
                                        builder.getBuilder().getVersion(),
                                        builder.getBuilderAttributes()
                                )
                        );
                    });

                    model.setBuilders(builders);
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getSettings())) {
                    model.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, Setting.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (!CollectionUtils.isEmpty(mappingContext.getSource().getActions())) {
                    model.setActions(mappingContext.getSource().getActions().stream().map(action -> map(action, Action.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                return model;
            }
        };
    }
    // endregion - Collection -

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

                entity.setExternalId(mappingContext.getSource().getKey());
                entity.setType(mappingContext.getSource().getType());
                entity.setName(mappingContext.getSource().getTitle());

                return entity;
            }
        };
    }
    // endregion - Plex API -
    // endregion - Converters -
}
