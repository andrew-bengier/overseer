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
@Table(name = "servers")
public class ServerEntity implements Serializable, Comparable<ServerEntity> {
    // region - Class Variables -
    @Id
    private String id;

    private String name;

    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "api_key_id")
    private ApiKeyEntity apiKey;

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
    @JoinColumn(name = "server_id")
    private Set<LibraryEntity> libraries;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(ServerEntity server) {
        return Comparator.comparing(ServerEntity::getName)
                .compare(this, server);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof ServerEntity) {
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
