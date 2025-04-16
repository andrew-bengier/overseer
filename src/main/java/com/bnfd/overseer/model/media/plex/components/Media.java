package com.bnfd.overseer.model.media.plex.components;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.math.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Media")
@XmlAccessorType(XmlAccessType.FIELD)
public class Media {
    @XmlAttribute(name = "duration")
    private Integer duration;
    @XmlAttribute(name = "aspectRatio")
    private BigDecimal aspectRatio;
    @XmlAttribute(name = "audioCodec")
    private String audioCodec;
    @XmlAttribute(name = "videoCodec")
    private String videoCodec;
    @XmlAttribute(name = "videoResolution")
    private String videoResolution;
    @XmlAttribute(name = "container")
    private String container;
}
