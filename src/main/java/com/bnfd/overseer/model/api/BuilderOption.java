package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.BuilderCategory;
import com.bnfd.overseer.model.constants.BuilderType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

public class BuilderOption implements Serializable, Comparable<BuilderOption> {
    // region - Class Variables -
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "id", example = "01955a48-58fa-75ae-bdb1-5287fc4341dd", description = "Builder Id", format = "UUID")
    private String id;
    @Schema(name = "type", implementation = BuilderType.class, description = "Builder Type")
    private BuilderType type;
    @Schema(name = "category", implementation = BuilderCategory.class, description = "Builder Category")
    private BuilderCategory category;
    @Schema(name = "name", example = "Movie", description = "Builder Name")
    private String name;
    @Schema(name = "version", example = "0.0.1", description = "Version - references when builders supported")
    private String version;
    // endregion - Class Variables -

    // region - Constructors -
    public BuilderOption() {
    }

    public BuilderOption(String id, BuilderType type, BuilderCategory category, String name, String version) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.name = name;
        this.version = version;
    }
    // endregion - Constructors -

    // region - Accessor Methods -
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(BuilderOption option) {
        return Comparator.comparing(BuilderOption::getType)
                .thenComparing(BuilderOption::getCategory)
                .thenComparing(BuilderOption::getName)
                .compare(this, option);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BuilderOption) {
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
