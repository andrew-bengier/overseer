package com.bnfd.overseer.model.api;

import com.bnfd.overseer.model.constants.MediaType;
import com.bnfd.overseer.model.constants.MetadataType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Media implements Serializable, Comparable<Media> {
    // region - Class Variables -
    private String id;
    private String libraryId;
    private String externalId;
    private MediaType type;
    private Set<Metadata> metadata;
    private boolean acquired;
    private Set<Media> children;
    // endregion - Class Variables -

    // region - Accessor Methods -
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public Set<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(Set<Metadata> metadata) {
        this.metadata = metadata;
    }

    public Set<Metadata> addMetadata(Metadata meta) {
        if (CollectionUtils.isEmpty(metadata)) {
            metadata = new HashSet<>();
        }

        metadata.add(meta);

        return metadata;
    }

    public void removeMetadata(Metadata meta) {
        if (CollectionUtils.isNotEmpty(metadata)) {
            metadata.remove(meta);
        }
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
    }

    public Set<Media> getChildren() {
        return children;
    }

    public void setChildren(Set<Media> children) {
        this.children = children;
    }

    public Set<Media> addChildren(Media child) {
        if (CollectionUtils.isEmpty(children)) {
            children = new HashSet<>();
        }

        children.add(child);

        return children;
    }

    public void removeChildren(Media child) {
        if (CollectionUtils.isNotEmpty(children)) {
            children.remove(child);
        }
    }
    // endregion - Accessor Methods -

    // region - Overridden Methods -
    @Override
    public int compareTo(Media media) {
        if (CollectionUtils.isNotEmpty(metadata) && CollectionUtils.isNotEmpty(media.getMetadata())) {
            Metadata thisTitle = this.getMetadata().stream().filter(meta -> meta.getName().equalsIgnoreCase(MetadataType.TITLE.name())).findFirst().orElse(null);
            Metadata mediaTitle = media.getMetadata().stream().filter(meta -> meta.getName().equalsIgnoreCase(MetadataType.TITLE.name())).findFirst().orElse(null);

            Metadata thisNumber = this.getMetadata().stream().filter(meta -> meta.getName().equalsIgnoreCase(MetadataType.NUMBER.name())).findFirst().orElse(null);
            Metadata mediaNumber = media.getMetadata().stream().filter(meta -> meta.getName().equalsIgnoreCase(MetadataType.NUMBER.name())).findFirst().orElse(null);

            if ((thisNumber != null && mediaNumber != null) && (thisNumber != null && mediaNumber != null)) {
                int number = Comparator.comparing(Metadata::getValue)
                        .compare(thisNumber, mediaNumber);
                if (number == 0) {
                    return Comparator.comparing(Metadata::getValue)
                            .compare(thisTitle, mediaTitle);
                } else {
                    return number;
                }
            }
            if (thisNumber != null && mediaNumber != null) {
                return Comparator.comparing(Metadata::getValue)
                        .compare(thisNumber, mediaNumber);
            } else if (thisTitle != null && mediaTitle != null) {
                return Comparator.comparing(Metadata::getValue)
                        .compare(thisTitle, mediaTitle);
            } else {
                return Comparator.comparing(Media::getLibraryId)
                        .thenComparing(Media::getType)
                        .compare(this, media);
            }
        } else {
            return Comparator.comparing(Media::getLibraryId)
                    .thenComparing(Media::getType)
                    .compare(this, media);
        }
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Media) {
            return EqualsBuilder.reflectionEquals(obj, this);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }
    // endregion - Overridden Methods -
}
