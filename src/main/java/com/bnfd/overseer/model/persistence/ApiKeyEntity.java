package com.bnfd.overseer.model.persistence;

import com.bnfd.overseer.model.constants.ApiKeyType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "api_keys")
public class ApiKeyEntity {
    // region - Class Variables -
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private ApiKeyType type;

    private String name;

    private String key;

    private String url;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public final boolean equals(Object object) {
        if (object instanceof ApiKeyEntity) {
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
