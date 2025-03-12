package com.bnfd.overseer.model.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "collections")
public class CollectionEntity {
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
    @OneToMany(mappedBy = "collection", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CollectionBuilderEntity> builders;
    // endregion - Class Variables -

    // region - Overridden Methods -
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
