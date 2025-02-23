package com.bnfd.overseer.model.api;

import com.google.gson.*;
import org.apache.commons.lang3.builder.*;

import java.io.*;
import java.util.*;

public class Server implements Serializable, Comparable<Server> {
    // region - Class Variables -
    private String id;

    private String name;

    private ApiKey apiKey;

    private Set<Setting> settings;

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
        if (settings.isEmpty()) {
            settings = new HashSet<>();
        }

        settings.add(setting);

        return settings;
    }

    public void removeSetting(Setting setting) {
        if (!settings.isEmpty()) {
            settings.remove(setting);
        }
    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }

    public Set<Library> addLibrary(Library library) {
        if (libraries.isEmpty()) {
            libraries = new HashSet<>();
        }

        libraries.add(library);

        return libraries;
    }

    public void removeLibrary(Library library) {
        if (!libraries.isEmpty()) {
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
