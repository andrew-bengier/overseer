package com.bnfd.overseer.service.api;

import com.bnfd.overseer.model.api.info.GitReleaseNote;
import com.bnfd.overseer.model.web.response.git.GitRelease;
import com.bnfd.overseer.model.web.response.git.GitTag;
import com.bnfd.overseer.service.templates.ApiRestTemplate;
import com.bnfd.overseer.utils.HttpUtils;
import com.bnfd.overseer.utils.ListUtils;
import com.bnfd.overseer.utils.MappingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class GitApiService {
    // region - Class Variables -
    private final static String GIT_API_BASE_URL = "https://api.github.com";
    private final static String GIT_API_REPOS_URL = "repos";
    private final static String GIT_API_RELEASES_URL = "releases";
    private final static String GIT_API_TAGS_URL = "tags";
    private final static String GIT_API_COMMIT_URL = "commit";
    private final static String GIT_API_TREE_URL = "tree";

    private final ApiRestTemplate restTemplate;

    private final String releasesEndpoint;
    private final String tagsEndpoint;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public GitApiService(Environment environment, ApiRestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        releasesEndpoint = HttpUtils.generateUrl(
                GIT_API_BASE_URL,
                GIT_API_REPOS_URL,
                environment.getProperty("GIT_REPO_OWNER"),
                environment.getProperty("GIT_REPO"),
                GIT_API_RELEASES_URL
        ).toString();

        tagsEndpoint = HttpUtils.generateUrl(
                GIT_API_BASE_URL,
                GIT_API_REPOS_URL,
                environment.getProperty("GIT_REPO_OWNER"),
                environment.getProperty("GIT_REPO"),
                GIT_API_TAGS_URL
        ).toString();
    }
    // endregion - Constructors -

    public Set<GitReleaseNote> getGitRelease() {
        List<GitRelease> releaseResponse = restTemplate.callRest(releasesEndpoint,
                HttpMethod.GET,
                MediaType.APPLICATION_JSON,
                Map.of(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE),
                null,
                new ParameterizedTypeReference<List<GitRelease>>() {
                },
                "Get Git Releases");

        List<GitTag> tagResponse = restTemplate.callRest(tagsEndpoint,
                HttpMethod.GET,
                MediaType.APPLICATION_JSON,
                Map.of(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE),
                null,
                new ParameterizedTypeReference<List<GitTag>>() {
                },
                "Get Git Tag");

        return MappingUtil.mappingToGitReleaseInfo(ListUtils.shrinkTo(releaseResponse, 3), tagResponse);
    }
}
