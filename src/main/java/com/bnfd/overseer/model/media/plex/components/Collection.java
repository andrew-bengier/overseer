package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Collection")
@XmlAccessorType(XmlAccessType.FIELD)
public class Collection {
    @XmlAttribute(name = "id")
    public String id;
    @XmlAttribute(name = "filter")
    public String filter;
    @XmlAttribute(name = "tag")
    public String tag;

    public Collection(String tag) {
        this.tag = tag;
    }

    // region -- Overridden Methods --
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, NO_CLASS_NAME_STYLE);
    }
    // endregion -- Overridden Methods --
}