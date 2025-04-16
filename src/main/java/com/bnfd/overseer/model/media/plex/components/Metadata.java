package com.bnfd.overseer.model.media.plex.components;


import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Metadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadata {
    @XmlAttribute(name = "key")
    public String key;
    @XmlAttribute(name = "studio")
    public String studio;
    @XmlAttribute(name = "type")
    public String type;
    @XmlAttribute(name = "title")
    public String title;
    @XmlAttribute(name = "contentRating")
    public String contentRating;
    @XmlAttribute(name = "summary")
    public String summary;
    @XmlAttribute(name = "rating")
    public String rating;
    @XmlAttribute(name = "year")
    public String year;
    @XmlAttribute(name = "thumb")
    public String thumb;
    @XmlAttribute(name = "duration")
    public String duration;
    @XmlAttribute(name = "originallyAvailableAt")
    public String originallyAvailableAt;
    @XmlAttribute(name = "index")
    public Integer index;

    @XmlElement(name = "Media")
    public List<Media> media;
    @XmlElement(name = "Genre")
    public List<Genre> genre;
    @XmlElement(name = "Director")
    public List<Director> director;
    @XmlElement(name = "Producer")
    public List<Director> producer;
    @XmlElement(name = "Country")
    public List<Country> country;
}
