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
@XmlRootElement(name = "Field")
@XmlAccessorType(XmlAccessType.FIELD)
public class Field {
    @XmlAttribute(name = "locked")
    public String locked;
    @XmlAttribute(name = "name")
    public String name;

    public Field(String name) {
        locked = "1";
        this.name = name;
    }

    // region -- Overridden Methods --
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, NO_CLASS_NAME_STYLE);
    }
    // endregion -- Overridden Methods --
}
