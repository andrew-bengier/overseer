package com.bnfd.overseer.service.api.web;

import com.bnfd.overseer.exception.OverseerException;
import com.bnfd.overseer.exception.OverseerPreConditionRequiredException;
import com.bnfd.overseer.model.api.Media;
import com.bnfd.overseer.model.api.Metadata;
import com.bnfd.overseer.model.constants.MediaIdType;
import com.bnfd.overseer.model.constants.MediaType;
import com.bnfd.overseer.model.constants.MetadataType;
import com.bnfd.overseer.service.ApiKeyService;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.collections.CollectionInfo;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.tv.core.TvSeason;
import info.movito.themoviedbapi.model.tv.episode.TvEpisodeDb;
import info.movito.themoviedbapi.model.tv.season.TvSeasonDb;
import info.movito.themoviedbapi.model.tv.season.TvSeasonEpisode;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import info.movito.themoviedbapi.tools.TmdbException;
import info.movito.themoviedbapi.tools.appendtoresponse.MovieAppendToResponse;
import info.movito.themoviedbapi.tools.appendtoresponse.TvEpisodesAppendToResponse;
import info.movito.themoviedbapi.tools.appendtoresponse.TvSeasonsAppendToResponse;
import info.movito.themoviedbapi.tools.appendtoresponse.TvSeriesAppendToResponse;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TmdbWebApiService implements WebApiService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;
    private final TmdbApi api;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public TmdbWebApiService(@Qualifier("overseer-mapper") ModelMapper overseerMapper, ApiKeyService apiKeyService) {
        try {
//            ApiKey apiKey = apiKeyService.getAllApiKeysByName(ApiKeyType.TMDB).getFirst();
//            api = new TmdbApi(apiKey.getKey());
            this.overseerMapper = overseerMapper;
            api = new TmdbApi("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyOTFmNDhmNWJhNjM1Yzg4ZmRkMGYzMTI1ZDMzYjY3MSIsIm5iZiI6MTU4MjA4MDk3OS4xNTYsInN1YiI6IjVlNGNhM2QzMzU4MTFkMDAxMzRkOGQzMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.qyNvEAXytWdysz2Dd78OFRxqB-1W1Qhxab-IO4pfSBc");
        } catch (PersistenceException | NoSuchElementException exception) {
            throw new OverseerPreConditionRequiredException("Tmdb API keys not found");
        }
    }
    // endregion - Constructors -

    @Override
    public List<Media> getMediaFromCollection(String collectionId) {
        try {
            CollectionInfo info = api.getCollections().getDetails(Integer.valueOf(collectionId), "english");

            List<Media> media = new ArrayList<>();
            info.getParts().forEach(part -> {
                switch (MediaType.findByName(part.getMediaType())) {
                    case MOVIE -> media.add(overseerMapper.map(getMovie(String.valueOf(part.getId())), Media.class));
                    case SERIES -> media.add(overseerMapper.map(getSeries(String.valueOf(part.getId())), Media.class));
                    default -> throw new OverseerPreConditionRequiredException(part.getMediaType());
                }
            });

            return media;
        } catch (TmdbException exception) {
            // TODO: add ExpectationFailedException
            throw new OverseerException(exception.getMessage());
        }
    }

    @Override
    public Media getMovie(String movieId) {
        try {
            MovieDb movie = api.getMovies().getDetails(Integer.parseInt(movieId), "english", getResponseAppendicesForMovies().toArray(new MovieAppendToResponse[0]));

            return overseerMapper.map(movie, Media.class);
        } catch (TmdbException exception) {
            throw new OverseerException(exception.getMessage());
        }

    }

    @Override
    public Media getSeries(String seriesId) {
        // TODO: fill in data for series, season, and episode
        try {
            TvSeriesDb series = api.getTvSeries().getDetails(Integer.parseInt(seriesId), "english", getResponseAppendicesForSeries().toArray(new TvSeriesAppendToResponse[0]));

            Media media = overseerMapper.map(series, Media.class);
            media.setType(MediaType.SERIES);
            media.addMetadata(new Metadata(null, MetadataType.TITLE.name(), series.getName()));
            media.addMetadata(new Metadata(null, MetadataType.ORIGINAL_TITLE.name(), series.getOriginalName()));
            media.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), series.getOverview()));
            media.addMetadata(new Metadata(null, MetadataType.TAGLINE.name(), series.getTagline()));
            media.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), series.getFirstAirDate()));
            media.addMetadata(new Metadata(null, MetadataType.POSTER.name(), series.getPosterPath()));
            media.addMetadata(new Metadata(null, MetadataType.EXTERNAL_ID.name(), MediaIdType.IMDB.name() + "_" + series.getExternalIds().getImdbId()));
            media.addMetadata(new Metadata(null, MetadataType.EXTERNAL_ID.name(), MediaIdType.TVDB.name() + "_" + series.getExternalIds().getTvdbId()));

            Set<Media> seasons = new TreeSet<>();
            for (TvSeason seasonRequest : series.getSeasons()) {
                TvSeasonDb seasonResult = api.getTvSeasons().getDetails(series.getId(), seasonRequest.getSeasonNumber(), "english", getResponseAppendicesForSeasons().toArray(new TvSeasonsAppendToResponse[0]));

                Media season = overseerMapper.map(seasonResult, Media.class);
                season.setType(MediaType.SEASON);
                season.addMetadata(new Metadata(null, MetadataType.TITLE.name(), seasonResult.getName()));
                season.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), seasonResult.getOverview()));
                season.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), seasonResult.getAirDate()));
                season.addMetadata(new Metadata(null, MetadataType.POSTER.name(), seasonResult.getPosterPath()));
                season.addMetadata(new Metadata(null, MetadataType.NUMBER.name(), seasonResult.getSeasonNumber().toString()));
                season.addMetadata(new Metadata(null, MetadataType.COUNT.name(), String.valueOf(seasonResult.getEpisodes().size())));
                season.addMetadata(new Metadata(null, MetadataType.POSTER.name(), seasonResult.getPosterPath()));

                Set<Media> episodes = new TreeSet<>();
                for (TvSeasonEpisode episodeRequest : seasonResult.getEpisodes()) {
                    TvEpisodeDb episodeResult = api.getTvEpisodes().getDetails(series.getId(), episodeRequest.getSeasonNumber(), episodeRequest.getEpisodeNumber(), "english", getResponseAppendicesForEpisodes().toArray(new TvEpisodesAppendToResponse[0]));

                    Media episode = overseerMapper.map(episodeResult, Media.class);
                    episode.setType(MediaType.EPISODE);
                    episode.addMetadata(new Metadata(null, MetadataType.TITLE.name(), episodeResult.getName()));
                    episode.addMetadata(new Metadata(null, MetadataType.SUMMARY.name(), episodeResult.getOverview()));
                    episode.addMetadata(new Metadata(null, MetadataType.RELEASE_DATE.name(), episodeResult.getAirDate()));
                    episode.addMetadata(new Metadata(null, MetadataType.NUMBER.name(), episodeResult.getEpisodeNumber().toString()));

                    episodes.add(overseerMapper.map(episode, Media.class));
                }

                season.setChildren(overseerMapper.map(episodes, new TypeToken<TreeSet<Media>>() {
                }.getType()));
                seasons.add(season);
            }

            media.setChildren(seasons);

            return media;
        } catch (TmdbException exception) {
            throw new OverseerException(exception.getMessage());
        }

    }

    private static List<MovieAppendToResponse> getResponseAppendicesForMovies() {
        return List.of(
                MovieAppendToResponse.ALTERNATIVE_TITLES,
                MovieAppendToResponse.EXTERNAL_IDS,
                MovieAppendToResponse.IMAGES,
                MovieAppendToResponse.KEYWORDS,
                MovieAppendToResponse.LISTS,
                MovieAppendToResponse.RELEASE_DATES);
    }

    private static List<TvSeriesAppendToResponse> getResponseAppendicesForSeries() {
        return List.of(
                TvSeriesAppendToResponse.ALTERNATIVE_TITLES,
                TvSeriesAppendToResponse.EPISODE_GROUPS,
                TvSeriesAppendToResponse.EXTERNAL_IDS,
                TvSeriesAppendToResponse.IMAGES,
                TvSeriesAppendToResponse.KEYWORDS,
                TvSeriesAppendToResponse.LISTS);
    }

    private static List<TvSeasonsAppendToResponse> getResponseAppendicesForSeasons() {
        return List.of(
                TvSeasonsAppendToResponse.EXTERNAL_IDS,
                TvSeasonsAppendToResponse.IMAGES);
    }

    private static List<TvEpisodesAppendToResponse> getResponseAppendicesForEpisodes() {
        return List.of(
                TvEpisodesAppendToResponse.EXTERNAL_IDS,
                TvEpisodesAppendToResponse.IMAGES);
    }
}
