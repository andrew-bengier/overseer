package com.bnfd.overseer.model.api.info;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Hidden
@Schema(hidden = true)
@Data
@AllArgsConstructor
public class BuildInfo {
    // region - Class Variables -
    @Schema(name = "group", example = "com.bnfd", description = "Build group")
    private String group;
    @Schema(name = "artifact", example = "overseer", description = "Build artifact")
    private String artifact;
    @Schema(name = "name", example = "Overseer Application", description = "Application Name")
    private String name;
    @Schema(name = "version", example = "0.0.1", description = "Build version")
    private String version;
    // endregion - Class Variables -
}
