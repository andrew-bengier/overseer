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
@XmlRootElement(name = "Image")
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {
    @XmlAttribute(name = "alt")
    private String alt;
    @XmlAttribute(name = "type")
    private String type;
    @XmlAttribute(name = "url")
    private String url;
}
