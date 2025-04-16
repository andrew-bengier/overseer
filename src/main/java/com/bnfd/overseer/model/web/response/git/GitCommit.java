package com.bnfd.overseer.model.web.response.git;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GitCommit {
    @JsonProperty("sha")
    private String sha;
    @JsonProperty("url")
    private String url;

    public static final GitCommit EMPTY = GitCommit.builder().build();
}
