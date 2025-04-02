package com.bnfd.overseer.model.persistence;

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
@Table(name = "collections")
public class CollectionEntity implements Serializable, Comparable<CollectionEntity> {
    // region - Class Variables -
    @Id
    private String id;

    @Column(name = "library_id")
    private String libraryId;

    @Column(name = "external_id")
    private String externalId;

    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany
    @JoinColumn(name = "reference_id")
    private Set<SettingEntity> settings;

    @EqualsAndHashCode.Exclude
    @OneToMany
    @JoinColumn(name = "reference_id")
    private Set<ActionEntity> actions;

    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "collection_id")
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CollectionBuilderEntity> builders;
    // endregion - Class Variables -

    // region - Constructors -
    public CollectionEntity(String id, String libraryId, String externalId, String name) {
        this.id = id;
        this.libraryId = libraryId;
        this.externalId = externalId;
        this.name = name;
    }
    // endregion - Constructors -

    // region - Overridden Methods -
    @Override
    public int compareTo(CollectionEntity collection) {
        return Comparator.comparing(CollectionEntity::getLibraryId)
                .thenComparing(CollectionEntity::getName)
                .compare(this, collection);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof CollectionEntity) {
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
