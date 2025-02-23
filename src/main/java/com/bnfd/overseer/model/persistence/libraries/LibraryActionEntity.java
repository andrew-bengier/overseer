package com.bnfd.overseer.model.persistence.libraries;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@Data
@Entity
@Table(name = "library_actions")
public class LibraryActionEntity implements Serializable, Comparable<LibraryActionEntity> {
    // region - Class Variables -
    @Id
    private String id;
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private LibraryEntity library;

    private String type;

    private String name;

    private String val;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(LibraryActionEntity action) {
        // Compare using name
        return this.getId().compareTo(action.getId());
    }
    // endregion - Overridden Methods -
}
