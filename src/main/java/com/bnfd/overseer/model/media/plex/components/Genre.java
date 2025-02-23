package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Genre")
@XmlAccessorType(XmlAccessType.FIELD)
public class Genre {

    @XmlAttribute(name = "tag")
    public String tag;
}
