package com.bnfd.overseer.model.api;

import com.google.gson.*;
import org.apache.commons.lang3.builder.*;

import java.io.*;
import java.util.*;

public class ApiKey implements Serializable, Comparable<ApiKey> {
    // region - Class Variables -
    private Integer id;

    private String name;
    private String key;
    private String url;
    // endregion - Class Variables -

    // region - Constructors -
    public ApiKey() {
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(ApiKey apiKey) {
        return Comparator.comparing(ApiKey::getName)
                .thenComparing(ApiKey::getId)
                .compare(this, apiKey);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ApiKey) {
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
