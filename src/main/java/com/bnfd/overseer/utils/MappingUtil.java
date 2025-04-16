package com.bnfd.overseer.utils;

import com.bnfd.overseer.model.api.info.GitReleaseNote;
import com.bnfd.overseer.model.web.response.git.GitRelease;
import com.bnfd.overseer.model.web.response.git.GitTag;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// NOTE: this is specifically for mapping objects to pojos when the ObjectMapper isn't sufficient
//      ex. 2 objects -> single pojo
public class MappingUtil {
    public static Set<GitReleaseNote> mappingToGitReleaseInfo(List<GitRelease> releases, List<GitTag> tags) {
        TreeSet<GitReleaseNote> gitReleaseNotes = new TreeSet<>();
        for (GitRelease release : releases) {
            GitReleaseNote gitReleaseNote = new GitReleaseNote();
            gitReleaseNote.setId(release.getId());
            gitReleaseNote.setTitle(release.getTitle());
            gitReleaseNote.setUrl(release.getUrl());
            gitReleaseNote.setAuthor(release.getAuthor().getName());
            gitReleaseNote.setAuthorUrl(release.getAuthor().getUrl());
            gitReleaseNote.setAuthorAvatarUrl(release.getAuthor().getAvatarUrl());
            gitReleaseNote.setTag(release.getTag());
            gitReleaseNote.setName(release.getName());
            gitReleaseNote.setBranch(release.getTargetCommitish());
            gitReleaseNote.setCreated(release.getCreated());
            gitReleaseNote.setPublished(release.getPublished());
            gitReleaseNote.setNotes(release.getBody());

            tags.stream().filter(tag -> tag.getName().equals(release.getTag())).findFirst().ifPresent(tag -> {
                gitReleaseNote.setSha(tag.getCommit().getSha());
                gitReleaseNote.setCommitUrl(
                        tag.getCommit().getUrl()
                                .replace("api.", "")
                                .replace("repos/", "")
                );
            });

            gitReleaseNotes.add(gitReleaseNote);
        }

        return gitReleaseNotes;
    }
}
