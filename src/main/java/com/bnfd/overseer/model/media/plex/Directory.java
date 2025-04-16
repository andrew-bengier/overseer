package com.bnfd.overseer.model.media.plex;

import com.bnfd.overseer.model.media.plex.components.*;
import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Directory")
@XmlAccessorType(XmlAccessType.FIELD)
public class Directory {

    @XmlAttribute(name = "uuid")
    public String uuid;
    @XmlAttribute(name = "allowSync")
    public Boolean allowSync;
    @XmlAttribute(name = "art")
    public String art;
    @XmlAttribute(name = "key")
    public String key;
    @XmlAttribute(name = "type")
    public String type;
    @XmlAttribute(name = "title")
    public String title;
    @XmlAttribute(name = "agent")
    public String agent;

    @XmlAttribute(name = "studio")
    public String studio;
    @XmlAttribute(name = "contentRating")
    public String contentRating;
    @XmlAttribute(name = "summary")
    public String summary;
    @XmlAttribute(name = "year")
    public String year;
    @XmlAttribute(name = "thumb")
    public String thumb;
    @XmlAttribute(name = "duration")
    public String duration;
    @XmlAttribute(name = "originallyAvailableAt")
    public String originallyAvailableAt;
    @XmlAttribute(name = "leafCount")
    public String leafCount;
    @XmlAttribute(name = "childCount")
    public String childCount;
    @XmlAttribute(name = "rating")
    public String rating;

    @XmlElement(name = "Genre")
    public List<Genre> genres;
}
