package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.ActionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

public class Action implements Serializable, Comparable<Action> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Action Id", format = "UUID")
    private String id;
    @Schema(name = "type", implementation = ActionType.class, description = "Action Type")
    private ActionType type;
    @Schema(name = "name", example = "asset.backup", description = "Action Name")
    private String name;
    @Schema(name = "val", example = "true", description = "Action Value")
    private String val;
    // endregion - Class Variables -

    // region - Constructors -
    public Action() {
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods
    @Override
    public int compareTo(Action action) {
        return Comparator.comparing(Action::getName, String.CASE_INSENSITIVE_ORDER)
                .compare(this, action);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Action) {
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
    // endregion - Overridden Methods
}
