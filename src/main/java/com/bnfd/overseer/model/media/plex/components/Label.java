package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Label")
@XmlAccessorType(XmlAccessType.FIELD)
public class Label {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "filter")
    public String filter;
    @XmlAttribute(name = "tag")
    public String tag;
}