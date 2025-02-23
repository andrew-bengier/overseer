package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Producer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Producer {

    @XmlAttribute(name = "tag")
    public String tag;
}
