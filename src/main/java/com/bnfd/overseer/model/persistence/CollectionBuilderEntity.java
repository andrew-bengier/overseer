package com.bnfd.overseer.model.persistence;

import com.bnfd.overseer.model.constants.BuilderCategory;
import com.bnfd.overseer.model.constants.BuilderType;
import com.bnfd.overseer.model.mapper.StringListConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "collection_builders")
public class CollectionBuilderEntity implements Serializable, Comparable<CollectionBuilderEntity> {
    // region - Class Variables -
    @Id
    private String id;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "collection_id")
    private String collectionId;

    @Enumerated(EnumType.STRING)
    private BuilderType type;

    @Enumerated(EnumType.STRING)
    private BuilderCategory category;

    private String name;

    @Convert(converter = StringListConverter.class)
    @Column(name = "attributes")
    private List<String> builderAttributes;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(CollectionBuilderEntity builder) {
        return Comparator.comparing(CollectionBuilderEntity::getType)
                .thenComparing(CollectionBuilderEntity::getCategory)
                .thenComparing(CollectionBuilderEntity::getName)
                .compare(this, builder);
    }

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
