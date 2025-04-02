package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.MetadataType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Comparator;

public class Metadata implements Comparable<Metadata> {
    // region - Class Variables -
    private String id;

    private String name;

    private String value;
    // endregion - Class Variables -

    // region - Constructors -
    public Metadata() {
    }

    public Metadata(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    // endregion - Accessor Methods -
//    Comparator.comparing(Task::getStatus)

    // region - Overridden Methods -
    @Override
    public int compareTo(Metadata metadata) {
        MetadataType thisName = MetadataType.findByName(this.name);
        MetadataType metadataName = MetadataType.findByName(metadata.name);

        if (thisName != null && metadataName != null) {
            return thisName.compareTo(metadataName);
        } else {
            return Comparator.comparing(Metadata::getName)
                    .compare(this, metadata);
        }
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Metadata) {
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
