package com.bnfd.overseer.model.persistence;

import com.bnfd.overseer.model.constants.BuilderCategory;
import com.bnfd.overseer.model.constants.BuilderType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "builders")
public class BuilderEntity {
    // region - Class Variables -
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private BuilderType type;

    @Enumerated(EnumType.STRING)
    private BuilderCategory category;

    private String name;

    private String version;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public final boolean equals(Object object) {
        if (object instanceof BuilderEntity) {
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
