package com.bnfd.overseer.model.api.info;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Comparator;

@Hidden
@Schema(hidden = true)
@Data
@RequiredArgsConstructor
public class GitReleaseNote implements Comparable<GitReleaseNote> {
    @Schema(name = "id", example = "209454914", description = "Id")
    private String id;
    @Schema(name = "title", example = "v2.2.0", description = "Title")
    private String title;
    @Schema(name = "description", example = "Release for specific updates", description = "Description")
    private String description;
    @Schema(name = "url", example = "https://github.com/andrew-bengier/overseer/releases/tag/v2.2.0", description = "Url")
    private String url;
    @Schema(name = "author", example = "andrew-bengier", description = "Author Name")
    private String author;
    @Schema(name = "authorUrl", example = "https://github.com/andrew-bengier", description = "Author Url")
    private String authorUrl;
    @Schema(name = "authorAvatarUrl", example = "https://avatars.githubusercontent.com/u/13891981?v=4", description = "Author Avatar Url")
    private String authorAvatarUrl;
    @Schema(name = "tag", example = "v2.2.0", description = "Tag Name")
    private String tag;
    @Schema(name = "name", example = "v2.2.0", description = "Name")
    private String name;
    @Schema(name = "branch", example = "main", description = "Branch release is from")
    private String branch;
    @Schema(name = "draft", description = "Is release a draft")
    private boolean draft;
    @Schema(name = "prerelease", description = "Is release a prerelease")
    private boolean prerelease;
    @Schema(name = "created", example = "2025-02-24T08\\:59\\:17-0500", description = "Time release created")
    private Instant created;
    @Schema(name = "published", example = "2025-02-24T08\\:59\\:17-0500", description = "Time release published")
    private Instant published;
    @Schema(name = "notes", description = "Notes - in RTF")
    private String notes;
    @Schema(name = "sha", example = "e7478644cfa204eea5f6e9c05a00e4007727eb3d", description = "Release commit hash")
    private String sha;
    @Schema(name = "commitUrl", example = "https://github.com/andrew-bengier/overseer/commit/65f7c30dbaf1862ab2862f90bcb134c282c5de2c", description = "Release commit hash")
    private String commitUrl;

    // region - Overridden Methods -
    @Override
    public int compareTo(GitReleaseNote note) {
        return Comparator.comparing(GitReleaseNote::getTag)
                .thenComparing(GitReleaseNote::getCreated).reversed()
                .compare(this, note);
    }
    // endregion - Overridden Methods -
}
