package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.BuilderCategory;
import com.bnfd.overseer.model.constants.BuilderType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Builder implements Serializable, Comparable<Builder> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Builder Id", format = "UUID")
    private String id;
    @Schema(name = "referenceId", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Reference Id - collection, playlist, etc.", format = "UUID")
    private String referenceId;
    @Schema(name = "type", implementation = BuilderType.class, description = "Builder Type")
    private BuilderType type;
    @Schema(name = "category", implementation = BuilderCategory.class, description = "Builder Category")
    private BuilderCategory category;
    @Schema(name = "name", example = "Movie", description = "Builder Name")
    private String name;
    @Schema(name = "version", example = "0.0.1", description = "Version - references when builders supported")
    private String version;
    @Schema(name = "attributes", example = "[645]", description = "Builder Attributes - comma separated list")
    private List<String> attributes;
    // endregion - Class Variables -

    // region - Constructors -
    public Builder() {
    }

    public Builder(String id, String referenceId, BuilderType type, BuilderCategory category, String name, String version, List<String> attributes) {
        this.id = id;
        this.referenceId = referenceId;
        this.type = type;
        this.category = category;
        this.name = name;
        this.version = version;
        this.attributes = attributes;
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

    public BuilderType getType() {
        return type;
    }

    public void setType(BuilderType type) {
        this.type = type;
    }

    public BuilderCategory getCategory() {
        return category;
    }

    public void setCategory(BuilderCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<String> addAttribute(String attribute) {
        if (attributes.isEmpty()) {
            attributes = new ArrayList<>();
        }

        attributes.add(attribute);

        return attributes;
    }

    public void removeAttribute(String attribute) {
        if (!attributes.isEmpty()) {
            attributes.remove(attribute);
        }
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(Builder builder) {
        return Comparator.comparing(Builder::getType)
                .thenComparing(Builder::getCategory)
                .thenComparing(Builder::getName)
                .compare(this, builder);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Builder) {
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
