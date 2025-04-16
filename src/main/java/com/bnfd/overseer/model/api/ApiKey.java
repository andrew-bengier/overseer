package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.ApiKeyType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

public class ApiKey implements Serializable, Comparable<ApiKey> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "ApiKey Id", format = "UUID")
    private String id;
    @Schema(name = "name", implementation = ApiKeyType.class, description = "ApiKey Type")
    private ApiKeyType name;
    @Schema(name = "key", example = "PJ&#8!m}E0a+U*PAPLH.KCe=", description = "API Token")
    private String key;
    @Schema(name = "url", example = "https://127.0.0.0.1:32400", description = "Api Url")
    private String url;
    // endregion - Class Variables -

    // region - Constructors -
    public ApiKey() {
    }

    public ApiKey(String id, ApiKeyType name, String key, String url) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.url = url;
    }
// endregion - Constructors -

    // region - Accessor Methods -
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApiKeyType getName() {
        return name;
    }

    public void setName(ApiKeyType name) {
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
