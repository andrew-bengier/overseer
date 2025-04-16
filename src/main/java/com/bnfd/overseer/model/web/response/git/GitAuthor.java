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
public class GitAuthor {
    @JsonProperty("name")
    private String name;
    @JsonProperty("html_url")
    private String url;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    public static final GitAuthor EMPTY = GitAuthor.builder().build();
}
