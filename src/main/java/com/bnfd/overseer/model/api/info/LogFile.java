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
public class LogFile {
    // region - Class Variables -
    @Schema(name = "fileName", example = "OverseerApi_log.out", description = "Log file name (with extension)")
    private String fileName;
    @Schema(name = "lastUpdated", example = "2025-02-24T08\\:59\\:17-0500", description = "Time log was last updated", format = "ISO-8601")
    private Instant lastUpdated;
    @Schema(name = "path", description = "Log file path (for download)")
    private String path;
    // endregion - Class Variables -
}
