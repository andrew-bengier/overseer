package com.bnfd.overseer.model.constants;

public enum BuilderType {
    //    PLEX,
    TMDB;
//    TVDB,
//    IMDB,
//    TRAKT,
//    AniDB,
//    AniList,
//    MyAnimeList,
//    MDBList,
//    Letterboxd,
//    iCheckMovies,
//    FlixPatrol,
//    Reciperr,
//    stevenlu,
//    Sonarr,
//    Radarr,
//    Tautulli;

    public static BuilderType findByName(String name) {
        BuilderType result = null;
        for (BuilderType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }

        return result;
    }
}
