package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Country")
@XmlAccessorType(XmlAccessType.FIELD)
public class Country {

    @XmlAttribute(name = "tag")
    public String tag;
}
