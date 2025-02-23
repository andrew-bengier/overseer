package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Director")
@XmlAccessorType(XmlAccessType.FIELD)
public class Director {

    @XmlAttribute(name = "tag")
    public String tag;
}
