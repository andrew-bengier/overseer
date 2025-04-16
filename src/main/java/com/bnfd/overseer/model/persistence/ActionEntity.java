package com.bnfd.overseer.model.persistence;

import com.bnfd.overseer.model.constants.ActionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "actions")
public class ActionEntity implements Serializable, Comparable<ActionEntity> {
    // region - Class Variables -
    @Id
    private String id;

    @Column(name = "reference_id")
    private String referenceId;

    @Enumerated(EnumType.STRING)
    private ActionType type;

    private String name;

    private String val;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(ActionEntity action) {
        return Comparator.comparing(ActionEntity::getReferenceId)
                .thenComparing(ActionEntity::getName)
                .compare(this, action);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof ActionEntity) {
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
