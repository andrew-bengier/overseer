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
@Table(name = "libraries")
public class LibraryEntity implements Serializable, Comparable<LibraryEntity> {
    // region - Class Variables -
    @Id
    private String id;

    @Column(name = "server_id")
    private String serverId;

    @Column(name = "external_id")
    private String externalId;

    private String type;

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
    @OneToMany
    @JoinColumn(name = "library_id")
    private Set<CollectionEntity> collections;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(LibraryEntity library) {
        return Comparator.comparing(LibraryEntity::getServerId)
                .thenComparing(LibraryEntity::getName)
                .compare(this, library);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof LibraryEntity) {
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
