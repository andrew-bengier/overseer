package com.bnfd.overseer.model.api;

import com.google.gson.*;
import org.apache.commons.lang3.builder.*;

import java.io.*;
import java.util.*;

public class Library implements Serializable, Comparable<Library> {
    // region - Class Variables -
    private String id;

    private String referenceId;

    private String serverId;

    private String type;

    private String name;

    private Set<Setting> settings;

    private Set<Action> actions;
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

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

    public Set<Action> addAction(Action action) {
        if (actions.isEmpty()) {
            actions = new HashSet<>();
        }

        actions.add(action);

        return actions;
    }

    public void removeAction(Action action) {
        if (!actions.isEmpty()) {
            actions.remove(action);
        }
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(Library library) {
        return Comparator.comparing(Library::getName)
                .thenComparing(Library::getId)
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
