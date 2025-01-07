package com.bnfd.overseer.model.api.info;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildInfo {
    // region - Class Variables -
    private String group;
    private String artifact;
    private String name;
    private String version;
    // endregion - Class Variables -
}
