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

public class Server implements Serializable, Comparable<Server> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Server Id", format = "UUID")
    private String id;
    @Schema(name = "name", example = "Plex - NAS", description = "Server Name")
    private String name;
    @Schema(name = "apiKey", implementation = ApiKey.class, description = "ApiKey")
    private ApiKey apiKey;
    @Schema(name = "settings", implementation = Setting.class, description = "Server Settings")
    private Set<Setting> settings;
    @Schema(name = "actions", implementation = Action.class, description = "Server Actions")
    private Set<Action> actions;
    @Schema(name = "libraries", implementation = Library.class, description = "Server Libraries")
    private Set<Library> libraries;
    // endregion - Class Variables -

    // region - Constructors -
    public Server() {
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApiKey getApiKey() {
        return apiKey;
    }

    public void setApiKey(ApiKey apiKey) {
        this.apiKey = apiKey;
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

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }

    public Set<Library> addLibrary(Library library) {
        if (CollectionUtils.isEmpty(libraries)) {
            libraries = new HashSet<>();
        }

        libraries.add(library);

        return libraries;
    }

    public void removeLibrary(Library library) {
        if (CollectionUtils.isNotEmpty(libraries)) {
            libraries.remove(library);
        }
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(Server server) {
        return Comparator.comparing(Server::getName)
                .thenComparing(Server::getId)
                .compare(this, server);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Server) {
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
