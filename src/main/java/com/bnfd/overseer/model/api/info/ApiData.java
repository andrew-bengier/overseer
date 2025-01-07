package com.bnfd.overseer.model.api.info;

import com.bnfd.overseer.utils.*;
import io.micrometer.common.util.*;
import lombok.*;
import org.apache.logging.log4j.util.*;
import org.springframework.boot.info.*;
import org.springframework.core.env.*;

import java.util.*;

@Data
public class ApiData {
    // region - Class Variables -
    private String environment;
    private BuildInfo buildInfo;
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
        this.buildInfo = new BuildInfo(
                buildProperties.getGroup(),
                buildProperties.getArtifact(),
                buildProperties.getName(),
                buildProperties.getVersion()
        );
        this.gitInfo = new GitInfo(
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
