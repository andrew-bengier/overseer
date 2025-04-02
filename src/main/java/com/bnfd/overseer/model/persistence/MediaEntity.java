package com.bnfd.overseer.model.persistence;

import com.bnfd.overseer.model.constants.MediaType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
public class MediaEntity implements Serializable, Comparable<MediaEntity> {
    // region - Class Variables -
    @Id
    private String id;

    @Column(name = "library_id")
    private String libraryId;

    @Column(name = "external_id")
    private String externalId;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @EqualsAndHashCode.Exclude
    @OneToMany
    @JoinColumn(name = "reference_id")
    private Set<MetadataEntity> metadata;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(MediaEntity metadata) {
        return Comparator.comparing(MediaEntity::getLibraryId)
                .thenComparing(MediaEntity::getType)
                .thenComparing(MediaEntity::getExternalId)
                .compare(this, metadata);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof MediaEntity) {
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
