package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Writer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Writer {

    @XmlAttribute(name = "tag")
    public String tag;
}
