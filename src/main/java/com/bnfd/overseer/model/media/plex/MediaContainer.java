package com.bnfd.overseer.model.media.plex;

import com.bnfd.overseer.model.media.plex.components.Metadata;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @XmlElement(name = "Video")
    public List<Video> videos;

    @XmlElement(name = "Metadata")
    public List<Metadata> metadata;
}

