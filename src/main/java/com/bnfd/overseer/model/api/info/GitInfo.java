package com.bnfd.overseer.model.api.info;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Hidden
@Schema(hidden = true)
@Data
@AllArgsConstructor
public class GitInfo {
    // region - Class Variables -
    @Schema(name = "branch", example = "main", description = "Git Branch")
    private String branch;
    @Schema(name = "commitId", example = "overseer", description = "Git commit id")
    private String commitId;
    @Schema(name = "commitMessage", example = "32644af4517e28d1a14b905ee5d884821680c035", description = "Git commit message")
    private String commitMessage;
    @Schema(name = "commitUser", example = "abengier", description = "Git user")
    private String commitUser;
    @Schema(name = "commitTime", example = "2025-02-24T08\\:59\\:17-0500", description = "Time of commit", format = "ISO-8601")
    private Instant commitTime;
    // endregion - Class Variables -
}
