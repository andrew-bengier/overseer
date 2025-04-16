package com.bnfd.overseer.model.web.response.git;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatboyindustrial.gsonjavatime.InstantConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GitRelease {
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("html_url")
    private String url;
    @JsonProperty("author")
    private GitAuthor author;
    @JsonProperty("tag_name")
    private String tag;
    @JsonProperty("name")
    private String name;
    @JsonProperty("draft")
    private boolean draft;
    @JsonProperty("prerelease")
    private boolean prerelease;
    @JsonProperty("target_commitish")
    private String targetCommitish;
    @JsonProperty("created_at")
    private Instant created;
    @JsonProperty("published_at")
    private Instant published;
    @JsonProperty("body")
    private String body;

    public static final GitRelease EMPTY = GitRelease.builder().build();

    // region - Overridden Methods -
    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Instant.class, new InstantConverter())
                .create();
        return gson.toJson(this);
    }
    // endregion - Overridden Methods -
}
