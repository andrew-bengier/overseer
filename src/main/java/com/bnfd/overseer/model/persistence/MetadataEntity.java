package com.bnfd.overseer.model.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metadata")
public class MetadataEntity implements Serializable, Comparable<MetadataEntity> {
    // region - Class Variables -
    @Id
    private String id;

    @Column(name = "reference_id")
    private String referenceId;

    private String name;

    private String val;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(MetadataEntity metadata) {
        return Comparator.comparing(MetadataEntity::getReferenceId)
                .thenComparing(MetadataEntity::getName)
                .compare(this, metadata);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof MetadataEntity) {
            return EqualsBuilder.reflectionEquals(this, object);
        } else {
            return false;
        }
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public final String toString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }
    // endregion - Overridden Methods -
}
