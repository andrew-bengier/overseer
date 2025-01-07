package com.bnfd.overseer.model.api.info;

import lombok.*;

import java.time.*;

@Data
@AllArgsConstructor
public class GitInfo {
    // region - Class Variables -
    private String branch;
    private String commitId;
    private String commitMessage;
    private String commitUser;

    private Instant commitTime;
    // endregion - Class Variables -
}
