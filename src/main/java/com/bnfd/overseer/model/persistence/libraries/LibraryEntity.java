package com.bnfd.overseer.model.persistence.libraries;

import com.bnfd.overseer.model.persistence.servers.*;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Entity
@Table(name = "libraries")
@NoArgsConstructor
@AllArgsConstructor
public class LibraryEntity {
    // region - Class Variables -
    @Id
    private String id;

    @Column(name = "reference_id")
    private String referenceId;

    private String type;

    private String name;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<LibrarySettingEntity> settings;
    // endregion - Class Variables -

    // region - Constructors -
    public LibraryEntity(String id, String referenceId, String type, String name) {
        this.id = id;
        this.referenceId = referenceId;
        this.type = type;
        this.name = name;
    }

    public LibraryEntity fromPersistence(LibraryEntity entity) {
        return new LibraryEntity(
                entity.getId(),
                entity.getReferenceId(),
                entity.getType(),
                entity.getName(),
                entity.getServer(),
                entity.getSettings()
        );
    }

    public LibraryEntity thinCopy() {
        return new LibraryEntity(
                this.getId(),
                this.getReferenceId(),
                this.getType(),
                this.getName()
        );
    }
    // endregion - Constructors -
}
