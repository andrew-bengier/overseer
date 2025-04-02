package com.bnfd.overseer.model.media.plex;

import com.bnfd.overseer.model.media.plex.components.*;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Video")
@XmlAccessorType(XmlAccessType.FIELD)
public class Video {
    @XmlAttribute(name = "guid")
    public String guid;
    @XmlAttribute(name = "ratingKey")
    public String ratingKey;
    @XmlAttribute(name = "key")
    public String key;
    @XmlAttribute(name = "studio")
    public String studio;
    @XmlAttribute(name = "type")
    public String type;
    @XmlAttribute(name = "title")
    public String title;
    @XmlAttribute(name = "tagline")
    public String tagline;
    @XmlAttribute(name = "index")
    public Integer index;
    @XmlAttribute(name = "contentRating")
    public String contentRating;
    @XmlAttribute(name = "summary")
    public String summary;
    @XmlAttribute(name = "rating")
    public Float rating;
    @XmlAttribute(name = "year")
    public String year;
    @XmlAttribute(name = "thumb")
    public String thumb;
    @XmlAttribute(name = "duration")
    public Long duration;
    @XmlAttribute(name = "originallyAvailableAt")
    public String originallyAvailableAt;

    @XmlAttribute(name = "parentRatingKey")
    public String parentRatingKey;
    @XmlAttribute(name = "parentIndex")
    public Integer parentIndex;

    @XmlElement(name = "Media")
    public List<Media> media;
    @XmlElement(name = "Genre")
    public List<Genre> genre;
    @XmlElement(name = "Director")
    public List<Director> director;
    @XmlElement(name = "Producer")
    public List<Producer> producer;
    @XmlElement(name = "Writer")
    public List<Writer> writer;
    @XmlElement(name = "Country")
    public List<Country> country;
    @XmlElement(name = "Guid")
    public List<Guid> guids;
    @XmlElement(name = "Collection")
    public List<Collection> collections;
    @XmlElement(name = "Field")
    public List<Field> field;

    // region -- Overridden Methods --
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, NO_CLASS_NAME_STYLE);
    }
    // endregion -- Overridden Methods --
}
