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
public class GitTag {
    @JsonProperty("name")
    private String name;
    @JsonProperty("commit")
    private GitCommit commit;

    public static final GitTag EMPTY = GitTag.builder().build();
}
