package com.bnfd.overseer.model.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Collection implements Serializable, Comparable<Collection> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Collection Id", format = "UUID")
    private String id;
    @Schema(name = "referenceId", example = "1", description = "Reference Id - id used in media server")
    private String referenceId;
    @Schema(name = "libraryId", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Library Id", format = "UUID")
    private String libraryId;
    @Schema(name = "name", example = "007 - James Bond", description = "Collection Name")
    private String name;
    @Schema(name = "builders", implementation = Builder.class, description = "Collection Builders")
    private Set<Builder> builders;
    @Schema(name = "settings", implementation = Setting.class, description = "Collection Settings")
    private Set<Setting> settings;
    @Schema(name = "actions", implementation = Action.class, description = "Library Actions")
    private Set<Action> actions;
    // endregion - Class Variables -

    // region - Constructors -
    public Collection() {
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Builder> getBuilders() {
        return builders;
    }

    public void setBuilders(Set<Builder> builders) {
        this.builders = builders;
    }

    public Set<Builder> addBuilder(Builder builder) {
        if (CollectionUtils.isEmpty(builders)) {
            builders = new HashSet<>();
        }

        builders.add(builder);

        return builders;
    }

    public void removeBuilder(Builder builder) {
        if (CollectionUtils.isNotEmpty(builders)) {
            builders.remove(builder);
        }
    }

    public Set<Setting> getSettings() {
        return settings;
    }

    public void setSettings(Set<Setting> settings) {
        this.settings = settings;
    }

    public Set<Setting> addSetting(Setting setting) {
        if (CollectionUtils.isEmpty(settings)) {
            settings = new HashSet<>();
        }

        settings.add(setting);

        return settings;
    }

    public void removeSetting(Setting setting) {
        if (CollectionUtils.isNotEmpty(settings)) {
            settings.remove(setting);
        }
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

    public Set<Action> addAction(Action action) {
        if (CollectionUtils.isEmpty(actions)) {
            actions = new HashSet<>();
        }

        actions.add(action);

        return actions;
    }

    public void removeAction(Action action) {
        if (CollectionUtils.isNotEmpty(actions)) {
            actions.remove(action);
        }
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(Collection collection) {
        return Comparator.comparing(Collection::getLibraryId)
                .thenComparing(Collection::getName)
                .compare(this, collection);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Collection) {
            return EqualsBuilder.reflectionEquals(obj, this);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }
    // endregion - Overridden Methods -
}
