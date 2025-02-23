package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.*;
import com.google.gson.*;
import org.apache.commons.lang3.builder.*;

import java.io.*;
import java.util.*;

public class Setting implements Serializable, Comparable<Setting> {
    // region - Class Variables -
    private String id;
    private SettingType type;
    private String name;
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
