package com.bnfd.overseer.model.api.info;

import lombok.*;
import org.springframework.core.env.*;

import java.util.*;

@Data
public class ApiData {
    // region - Class Variables -
    private String environment;
    private GitInfo gitInfo;
    // endregion - Class Variables -

    public ApiData(Environment environment, GitInfo gitInfo) {
        this.gitInfo = gitInfo;
        this.environment = Arrays.asList(environment.getActiveProfiles()).stream()
                .filter(Objects::nonNull)
                .map(String::toUpperCase)
                .map(val -> val + " ")
                .reduce(String::concat)
                .orElse("");
    }
}
