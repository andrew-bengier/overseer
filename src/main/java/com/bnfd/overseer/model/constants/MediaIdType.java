package com.bnfd.overseer.model.constants;

public enum MediaIdType {
    IMDB("imdb://"),
    TVDB("tvdb://"), // TvSeries only for now
    TMDB("tmdb://"),
    MAL("mal://"),
    ANIDB("anidb://"),
    TRAKT("trakt://");

    public final String prefix;

    MediaIdType(String prefix) {
        this.prefix = prefix;
    }

    public static MediaIdType findByName(String name) {
        MediaIdType result = null;
        for (MediaIdType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
