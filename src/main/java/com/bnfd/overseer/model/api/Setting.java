package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.SettingType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

public class Setting implements Serializable, Comparable<Setting> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Setting Id", format = "UUID")
    private String id;
    @Schema(name = "type", implementation = SettingType.class, description = "Setting Type")
    private SettingType type;
    @Schema(name = "name", example = "schedule", description = "Setting Name")
    private String name;
    @Schema(name = "val", example = "0 0 4 ? * SUN *", description = "Setting Value")
    private String val;
    // endregion - Class Variables -

    // region - Constructors -
    public Setting() {
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SettingType getType() {
        return type;
    }

    public void setType(SettingType type) {
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
    public int compareTo(Setting setting) {
        return Comparator.comparing(Setting::getName, String.CASE_INSENSITIVE_ORDER)
                .compare(this, setting);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Setting) {
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
