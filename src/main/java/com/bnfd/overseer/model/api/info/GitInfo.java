package com.bnfd.overseer.model.api.info;

import lombok.*;
import org.springframework.context.annotation.*;

import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PropertySource("classpath:git.properties")
public class GitInfo {
    // region - Class Variables -
    private String branch;
    private String buildVersion;
    private String commitId;
    private String commitMessageFull;
    private Instant commitTime;
    private String commitUserName;
    // endregion - Class Variables -
}
