package com.bnfd.overseer.model.media.plex;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "MediaContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class MediaContainer {
    @XmlAttribute(name = "size")
    public Integer size;
    @XmlAttribute(name = "allowSync")
    public Integer allowSync;
    @XmlAttribute(name = "title1")
    public String title1;
    @XmlAttribute(name = "title2")
    public String title2;
    @XmlAttribute(name = "thumb")
    public String thumb;

    @XmlElement(name = "Directory")
    public List<Directory> directories;
}

