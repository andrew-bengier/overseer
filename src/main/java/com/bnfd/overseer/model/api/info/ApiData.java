package com.bnfd.overseer.model.api.info;

import com.bnfd.overseer.utils.DateUtils;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Objects;

@Data
public class ApiData {
    // region - Class Variables -
    @Schema(name = "environment", allowableValues = "Docker, dev, prod", description = "Environment")
    private String environment;
    @Schema(name = "buildInfo", implementation = BuildProperties.class, description = "Build Information")
    private BuildInfo buildInfo;
    @Schema(name = "gitInfo", implementation = GitInfo.class, description = "Git Information")
    private GitInfo gitInfo;
    // endregion - Class Variables -

    public ApiData(Environment environment, BuildProperties buildProperties) {
        this.environment = Arrays.asList(environment.getActiveProfiles()).stream()
                .filter(Objects::nonNull)
                .map(String::toUpperCase)
                .map(val -> val + " ")
                .reduce(String::concat)
                .orElse("")
                .trim();
        buildInfo = new BuildInfo(
                buildProperties.getGroup(),
                buildProperties.getArtifact(),
                buildProperties.getName(),
                buildProperties.getVersion()
        );
        gitInfo = new GitInfo(
                environment.getProperty("git.branch", Strings.EMPTY),
                environment.getProperty("git.commit.id", Strings.EMPTY),
                environment.getProperty("git.commit.message.full", Strings.EMPTY),
                environment.getProperty("git.commit.user.name", Strings.EMPTY),
                StringUtils.isNotEmpty(
                        environment.getProperty("git.commit.time")) ?
                        DateUtils.offsetStringToInstant(environment.getProperty("git.commit.time"))
                        : null
        );
    }
}
