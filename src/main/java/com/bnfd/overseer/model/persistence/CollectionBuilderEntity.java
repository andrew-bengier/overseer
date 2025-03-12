package com.bnfd.overseer.model.persistence;

import com.bnfd.overseer.model.mapper.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "collection_builders")
public class CollectionBuilderEntity {
    // region - Class Variables -
    @Id
    private String id;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collection_id", nullable = false)
    private CollectionEntity collection;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "builder_id", nullable = false)
    private BuilderEntity builder;

    @Convert(converter = StringListConverter.class)
    @Column(name = "attributes")
    private List<String> builderAttributes;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public final boolean equals(Object object) {
        if (object instanceof CollectionBuilderEntity) {
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
