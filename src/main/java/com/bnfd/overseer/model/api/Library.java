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

public class Library implements Serializable, Comparable<Library> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Library Id", format = "UUID")
    private String id;
    @Schema(name = "referenceId", example = "1", description = "Reference Id - id used in media server")
    private String referenceId;
    @Schema(name = "serverId", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Server Id", format = "UUID")
    private String serverId;
    @Schema(name = "type", example = "Movie", description = "Library Type")
    private String type;
    @Schema(name = "name", example = "Movies", description = "Library Name")
    private String name;
    @Schema(name = "settings", implementation = Setting.class, description = "Library Settings")
    private Set<Setting> settings;
    @Schema(name = "actions", implementation = Action.class, description = "Library Actions")
    private Set<Action> actions;
    @Schema(name = "collections", implementation = Collection.class, description = "Library Collections")
    private Set<Collection> collections;
    // endregion - Class Variables -

    // region - Constructors -
    public Library() {
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

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }

    public Set<Collection> addCollection(Collection collection) {
        if (CollectionUtils.isEmpty(collections)) {
            collections = new HashSet<>();
        }

        collections.add(collection);

        return collections;
    }

    public void removeCollection(Collection collection) {
        if (CollectionUtils.isNotEmpty(collections)) {
            collections.remove(collection);
        }
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(Library library) {
        return Comparator.comparing(Library::getServerId)
                .thenComparing(Library::getType)
                .thenComparing(Library::getName)
                .compare(this, library);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Library) {
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
