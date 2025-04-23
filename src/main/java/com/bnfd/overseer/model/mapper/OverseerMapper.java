package com.bnfd.overseer.model.mapper;

import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.MediaIdType;
import com.bnfd.overseer.model.constants.MediaType;
import com.bnfd.overseer.model.constants.MetadataType;
import com.bnfd.overseer.model.constants.SettingType;
import com.bnfd.overseer.model.media.plex.Directory;
import com.bnfd.overseer.model.media.plex.Video;
import com.bnfd.overseer.model.persistence.*;
import info.movito.themoviedbapi.model.collections.Part;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.tv.season.TvSeasonDb;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        mapper.getConfiguration().setAmbiguityIgnored(true);

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

        // region - Builder -
        mapper.addConverter(builderModelToEntity());
        mapper.addConverter(builderEntityToModel());
        // endregion - Builder -

        // region - Builder Option -
        mapper.addConverter(builderOptionModelToEntity());
        mapper.addConverter(builderOptionEntityToModel());
        // endregion - Builder Option -

        // region - Plex API -
        mapper.addConverter(plexDirectoryToLibraryEntity());
        mapper.addConverter(plexVideoToMedia());

        mapper.addConverter(plexDirectoryToCollectionEntity());
        mapper.addConverter(plexMetadataToCollectionEntity());
        mapper.addConverter(plexMetadataToMediaEntity());
        // endregion - Plex API -

        // region - Tmdb API -
        mapper.addConverter(tmdbPartToMedia());
        mapper.addConverter(tmdbMovieDbToMedia());
        // endregion - Tmdb API -

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

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getSettings())) {
                    Set<SettingEntity> settings = mappingContext.getSource().getSettings().stream().map(setting -> map(setting, SettingEntity.class)).collect(Collectors.toSet());
                    settings.forEach(setting -> setting.setReferenceId(entity.getId()));
                    entity.setSettings(settings);
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getActions())) {
                    Set<ActionEntity> actions = mappingContext.getSource().getActions().stream().map(action -> map(action, ActionEntity.class)).collect(Collectors.toSet());
                    actions.forEach(action -> action.setReferenceId(entity.getId()));
                    entity.setActions(actions);
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getLibraries())) {
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

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getSettings())) {
                    model.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, Setting.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getActions())) {
                    model.setActions(mappingContext.getSource().getActions().stream().map(action -> map(action, Action.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getLibraries())) {
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

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getSettings())) {
                    Set<SettingEntity> settings = mappingContext.getSource().getSettings().stream().map(setting -> map(setting, SettingEntity.class)).collect(Collectors.toSet());
                    settings.forEach(setting -> setting.setReferenceId(entity.getId()));
                    entity.setSettings(settings);
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getActions())) {
                    Set<ActionEntity> actions = mappingContext.getSource().getActions().stream().map(action -> map(action, ActionEntity.class)).collect(Collectors.toSet());
                    actions.forEach(action -> action.setReferenceId(entity.getId()));
                    entity.setActions(actions);
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getCollections())) {
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

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getSettings())) {
                    model.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, Setting.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getActions())) {
                    model.setActions(mappingContext.getSource().getActions().stream().map(action -> map(action, Action.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getCollections())) {
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

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getBuilders())) {
                    entity.setBuilders(mappingContext.getSource().getBuilders().stream().map(builder -> map(builder, CollectionBuilderEntity.class)).collect(Collectors.toSet()));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getSettings())) {
                    Set<SettingEntity> settings = mappingContext.getSource().getSettings().stream().map(setting -> map(setting, SettingEntity.class)).collect(Collectors.toSet());
                    settings.forEach(setting -> setting.setReferenceId(entity.getId()));
                    entity.setSettings(settings);
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getActions())) {
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

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getBuilders())) {
                    model.setBuilders(mappingContext.getSource().getBuilders().stream().map(builder -> map(builder, Builder.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getSettings())) {
                    model.setSettings(mappingContext.getSource().getSettings().stream().map(setting -> map(setting, Setting.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getActions())) {
                    model.setActions(mappingContext.getSource().getActions().stream().map(action -> map(action, Action.class)).collect(Collectors.toCollection(TreeSet::new)));
                }

                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getMedia())) {
                    model.setMedia(new TreeSet<>(mappingContext.getSource().getMedia()));
                }

                return model;
            }
        };
    }
    // endregion - Collection -

    // region - Builder -
    public Converter<CollectionBuilderEntity, Builder> builderEntityToModel() {
        return new Converter<CollectionBuilderEntity, Builder>() {
            @Override
            public Builder convert(MappingContext<CollectionBuilderEntity, Builder> mappingContext) {
                Builder model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new Builder();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setTemplateId(mappingContext.getSource().getTemplateId());
                model.setType(mappingContext.getSource().getType());
                model.setCategory(mappingContext.getSource().getCategory());
                model.setName(mappingContext.getSource().getName());
                model.setAttributes(mappingContext.getSource().getBuilderAttributes());

                return model;
            }
        };
    }

    public Converter<Builder, CollectionBuilderEntity> builderModelToEntity() {
        return new Converter<Builder, CollectionBuilderEntity>() {
            @Override
            public CollectionBuilderEntity convert(MappingContext<Builder, CollectionBuilderEntity> mappingContext) {
                CollectionBuilderEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new CollectionBuilderEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setTemplateId(mappingContext.getSource().getTemplateId());
                entity.setType(mappingContext.getSource().getType());
                entity.setCategory(mappingContext.getSource().getCategory());
                entity.setName(mappingContext.getSource().getName());
                entity.setBuilderAttributes(mappingContext.getSource().getAttributes());

                return entity;
            }
        };
    }
    // endregion - Builder -

    // region - Builder Option -
    public Converter<BuilderOptionEntity, BuilderOption> builderOptionEntityToModel() {
        return new Converter<BuilderOptionEntity, BuilderOption>() {
            @Override
            public BuilderOption convert(MappingContext<BuilderOptionEntity, BuilderOption> mappingContext) {
                BuilderOption model;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    model = new BuilderOption();
                } else {
                    model = mappingContext.getDestination();
                }

                model.setId(mappingContext.getSource().getId());
                model.setType(mappingContext.getSource().getType());
                model.setCategory(mappingContext.getSource().getCategory());
                model.setName(mappingContext.getSource().getName());
                model.setVersion(mappingContext.getSource().getVersion());

                return model;
            }
        };
    }

    public Converter<BuilderOption, BuilderOptionEntity> builderOptionModelToEntity() {
        return new Converter<BuilderOption, BuilderOptionEntity>() {
            @Override
            public BuilderOptionEntity convert(MappingContext<BuilderOption, BuilderOptionEntity> mappingContext) {
                BuilderOptionEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new BuilderOptionEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setId(mappingContext.getSource().getId());
                entity.setType(mappingContext.getSource().getType());
                entity.setCategory(mappingContext.getSource().getCategory());
                entity.setName(mappingContext.getSource().getName());
                entity.setVersion(mappingContext.getSource().getVersion());

                return entity;
            }
        };
    }
    // endregion - Builder Option -

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

    private Converter<Directory, CollectionEntity> plexDirectoryToCollectionEntity() {
        return new Converter<Directory, CollectionEntity>() {
            @Override
            public CollectionEntity convert(MappingContext<Directory, CollectionEntity> mappingContext) {
                CollectionEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new CollectionEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setExternalId(mappingContext.getSource().getRatingKey());
                entity.setName(mappingContext.getSource().getTitle());

                Set<SettingEntity> settings = new HashSet<>();
                if (StringUtils.isNotBlank(mappingContext.getSource().getSummary())) {
                    settings.add(new SettingEntity(null, null, SettingType.STRING, "summary", mappingContext.getSource().getSummary()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getThumb())) {
                    settings.add(new SettingEntity(null, null, SettingType.STRING, "thumb", mappingContext.getSource().getThumb()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getYear())) {
                    settings.add(new SettingEntity(null, null, SettingType.STRING, "year", mappingContext.getSource().getYear()));
                }

                entity.setSettings(settings);

                return entity;
            }
        };
    }

    private Converter<com.bnfd.overseer.model.media.plex.components.Metadata, CollectionEntity> plexMetadataToCollectionEntity() {
        return new Converter<com.bnfd.overseer.model.media.plex.components.Metadata, CollectionEntity>() {
            @Override
            public CollectionEntity convert(MappingContext<com.bnfd.overseer.model.media.plex.components.Metadata, CollectionEntity> mappingContext) {
                CollectionEntity entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new CollectionEntity();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setExternalId(mappingContext.getSource().getRatingKey());
                entity.setName(mappingContext.getSource().getTitle());

                return entity;
            }
        };
    }

    private Converter<com.bnfd.overseer.model.media.plex.components.Metadata, Media> plexMetadataToMediaEntity() {
        return new Converter<com.bnfd.overseer.model.media.plex.components.Metadata, Media>() {
            @Override
            public Media convert(MappingContext<com.bnfd.overseer.model.media.plex.components.Metadata, Media> mappingContext) {
                Media entity;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    entity = new Media();
                } else {
                    entity = mappingContext.getDestination();
                }

                entity.setType(MediaType.valueOf(mappingContext.getSource().getType().toUpperCase()));
                entity.setExternalId(mappingContext.getSource().getRatingKey());
                entity.setAcquired(true);

                Set<Metadata> metadata = new HashSet<>();
                if (StringUtils.isNotBlank(mappingContext.getSource().getKey())) {
                    metadata.add(new Metadata(null, "key", mappingContext.getSource().getKey()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getRatingKey())) {
                    metadata.add(new Metadata(null, "ratingKey", mappingContext.getSource().getRatingKey()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getStudio())) {
                    metadata.add(new Metadata(null, "studio", mappingContext.getSource().getStudio()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getType())) {
                    metadata.add(new Metadata(null, "type", mappingContext.getSource().getType()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getTitle())) {
                    metadata.add(new Metadata(null, "title", mappingContext.getSource().getTitle()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getContentRating())) {
                    metadata.add(new Metadata(null, "contentRating", mappingContext.getSource().getContentRating()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getSummary())) {
                    metadata.add(new Metadata(null, "summary", mappingContext.getSource().getSummary()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getRating())) {
                    metadata.add(new Metadata(null, "rating", mappingContext.getSource().getRating()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getYear())) {
                    metadata.add(new Metadata(null, "year", mappingContext.getSource().getYear()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getThumb())) {
                    metadata.add(new Metadata(null, "thumb", mappingContext.getSource().getThumb()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getDuration())) {
                    metadata.add(new Metadata(null, "duration", mappingContext.getSource().getDuration()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOriginallyAvailableAt())) {
                    metadata.add(new Metadata(null, "originallyAvailableAt", mappingContext.getSource().getOriginallyAvailableAt()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOriginallyAvailableAt())) {
                    metadata.add(new Metadata(null, "originallyAvailableAt", mappingContext.getSource().getOriginallyAvailableAt()));
                }

                entity.setMetadata(metadata);

                return entity;
            }
        };
    }

    private Converter<Video, Media> plexVideoToMedia() {
        return new Converter<Video, Media>() {
            @Override
            public Media convert(MappingContext<Video, Media> mappingContext) {
                Media media;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    media = new Media();
                } else {
                    media = mappingContext.getDestination();
                }

                media.setExternalId(mappingContext.getSource().getRatingKey());
                media.setAcquired(true);

                if (StringUtils.isNotBlank(mappingContext.getSource().getTitle())) {
                    media.addMetadata(new Metadata(null, MetadataType.TITLE.name(), mappingContext.getSource().getTitle()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getTagline())) {
                    media.addMetadata(new Metadata(null, MetadataType.TAGLINE.name(), mappingContext.getSource().getTagline()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getYear())) {
                    media.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), mappingContext.getSource().getYear()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getSummary())) {
                    media.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), mappingContext.getSource().getSummary()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getThumb())) {
                    media.addMetadata(new Metadata(null, MetadataType.POSTER.name(), mappingContext.getSource().getThumb()));
                }

                return media;
            }
        };
    }
    // endregion - Plex API -

    // region - Tmdb API -
    public Converter<Part, Media> tmdbPartToMedia() {
        return new Converter<Part, Media>() {
            @Override
            public Media convert(MappingContext<Part, Media> mappingContext) {
                Media media;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    media = new Media();
                } else {
                    media = mappingContext.getDestination();
                }

                if (StringUtils.isNotBlank(mappingContext.getSource().getTitle())) {
                    media.addMetadata(new Metadata(null, MetadataType.TITLE.name(), mappingContext.getSource().getTitle()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getReleaseDate())) {
                    media.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), mappingContext.getSource().getReleaseDate()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOverview())) {
                    media.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), mappingContext.getSource().getOverview()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getPosterPath())) {
                    media.addMetadata(new Metadata(null, MetadataType.POSTER.name(), mappingContext.getSource().getPosterPath()));
                }

                return media;
            }
        };
    }

    public Converter<MovieDb, Media> tmdbMovieDbToMedia() {
        return new Converter<MovieDb, Media>() {
            @Override
            public Media convert(MappingContext<MovieDb, Media> mappingContext) {
                Media media;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    media = new Media();
                } else {
                    media = mappingContext.getDestination();
                }

                media.setType(MediaType.MOVIE);
                if (StringUtils.isNotBlank(mappingContext.getSource().getTitle())) {
                    media.addMetadata(new Metadata(null, MetadataType.TITLE.name(), mappingContext.getSource().getTitle()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOriginalTitle())) {
                    media.addMetadata(new Metadata(null, MetadataType.ORIGINAL_TITLE.name(), mappingContext.getSource().getOriginalTitle()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getTagline())) {
                    media.addMetadata(new Metadata(null, MetadataType.TAGLINE.name(), mappingContext.getSource().getTagline()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getReleaseDate())) {
                    media.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), mappingContext.getSource().getReleaseDate()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOverview())) {
                    media.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), mappingContext.getSource().getOverview()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getPosterPath())) {
                    media.addMetadata(new Metadata(null, MetadataType.POSTER.name(), mappingContext.getSource().getPosterPath()));
                }

                return media;
            }
        };
    }

    public Converter<TvSeriesDb, Media> tmdbSeriesToMedia() {
        return new Converter<TvSeriesDb, Media>() {
            @Override
            public Media convert(MappingContext<TvSeriesDb, Media> mappingContext) {
                Media media;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    media = new Media();
                } else {
                    media = mappingContext.getDestination();
                }

                media.setType(MediaType.SERIES);
                if (StringUtils.isNotBlank(mappingContext.getSource().getName())) {
                    media.addMetadata(new Metadata(null, MetadataType.TITLE.name(), mappingContext.getSource().getName()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOriginalName())) {
                    media.addMetadata(new Metadata(null, MetadataType.ORIGINAL_TITLE.name(), mappingContext.getSource().getOriginalName()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOverview())) {
                    media.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), mappingContext.getSource().getOverview()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getTagline())) {
                    media.addMetadata(new Metadata(null, MetadataType.TAGLINE.name(), mappingContext.getSource().getTagline()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getFirstAirDate())) {
                    media.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), mappingContext.getSource().getFirstAirDate()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getPosterPath())) {
                    media.addMetadata(new Metadata(null, MetadataType.POSTER.name(), mappingContext.getSource().getPosterPath()));
                }
                if (!ObjectUtils.isEmpty(mappingContext.getSource().getExternalIds())) {
                    if (StringUtils.isNotBlank(mappingContext.getSource().getExternalIds().getImdbId())) {
                        media.addMetadata(new Metadata(null, MetadataType.EXTERNAL_ID.name(), MediaIdType.IMDB.name() + "_" + mappingContext.getSource().getExternalIds().getImdbId()));
                    }
                    if (StringUtils.isNotBlank(mappingContext.getSource().getExternalIds().getTvdbId())) {
                        media.addMetadata(new Metadata(null, MetadataType.EXTERNAL_ID.name(), MediaIdType.TVDB.name() + "_" + mappingContext.getSource().getExternalIds().getTvdbId()));
                    }
                }

//                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getSeasons())) {
//                    media.setChildren(mappingContext.getSource().getSeasons().stream().map(season -> map(season, Media.class)).collect(Collectors.toCollection(TreeSet::new)));
//                }

                return media;
            }
        };
    }

    public Converter<TvSeasonDb, Media> tmdbTvSeasonToMedia() {
        return new Converter<TvSeasonDb, Media>() {
            @Override
            public Media convert(MappingContext<TvSeasonDb, Media> mappingContext) {
                Media media;
                if (ObjectUtils.isEmpty(mappingContext.getDestination())) {
                    media = new Media();
                } else {
                    media = mappingContext.getDestination();
                }

                media.setType(MediaType.SEASON);
                if (StringUtils.isNotBlank(mappingContext.getSource().getName())) {
                    media.addMetadata(new Metadata(null, MetadataType.TITLE.name(), mappingContext.getSource().getName()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getOverview())) {
                    media.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), mappingContext.getSource().getOverview()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getAirDate())) {
                    media.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), mappingContext.getSource().getAirDate()));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getPosterPath())) {
                    media.addMetadata(new Metadata(null, MetadataType.POSTER.name(), mappingContext.getSource().getPosterPath()));
                }
                if (!ObjectUtils.isEmpty(mappingContext.getSource().getSeasonNumber())) {
                    media.addMetadata(new Metadata(null, MetadataType.NUMBER.name(), mappingContext.getSource().getSeasonNumber().toString()));
                }
                if (CollectionUtils.isNotEmpty(mappingContext.getSource().getEpisodes())) {
                    media.addMetadata(new Metadata(null, MetadataType.COUNT.name(), String.valueOf(mappingContext.getSource().getEpisodes().size())));
                }
                if (StringUtils.isNotBlank(mappingContext.getSource().getPosterPath())) {
                    media.addMetadata(new Metadata(null, MetadataType.POSTER.name(), mappingContext.getSource().getPosterPath()));
                }

                return media;
            }
        };
    }
    // endregion - Tmdb API -
    // endregion - Converters -
}
