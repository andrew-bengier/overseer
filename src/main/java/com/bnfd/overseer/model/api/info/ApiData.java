package com.bnfd.overseer.model.api.info;

import com.bnfd.overseer.utils.DateUtils;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Data
public class ApiData {
    // region - Class Variables -
    @Schema(name = "environment", allowableValues = "Docker, dev, prod", description = "Environment")
    private String environment;
    @Schema(name = "mode", allowableValues = "Docker, Local", description = "Deployment Mode")
    private String mode;
    @Schema(name = "buildInfo", implementation = BuildProperties.class, description = "Build Information")
    private BuildInfo buildInfo;
    @Schema(name = "gitInfo", implementation = GitInfo.class, description = "Git Information")
    private GitInfo gitInfo;
    @Schema(name = "startTime", description = "API start time - ISO8601")
    private String startTime;
    @Schema(name = "sourceLink", description = "Code Source Link")
    private String sourceLink;
    // endregion - Class Variables -

    public ApiData(Environment environment, BuildProperties buildProperties) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        this.environment = Arrays.stream(environment.getActiveProfiles())
                .filter(Objects::nonNull)
                .map(String::toUpperCase)
                .map(val -> val + " ")
                .reduce(String::concat)
                .orElse("")
                .trim();
        mode = this.environment.contains("Docker") ? "Docker" : "Local";
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
        startTime = DateTimeFormatter
                .ISO_INSTANT
                .format(new Date(runtimeMXBean.getStartTime()).toInstant().atZone(ZoneOffset.UTC));
        sourceLink = environment.getProperty("SOURCE_LINK", environment.getProperty("git.uri", Strings.EMPTY));
    }
}
