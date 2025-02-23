package com.bnfd.overseer.model.persistence.apikeys;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "api_keys")
public class ApiKeyEntity {
    // region - Class Variables -
    @Id
    private String id;

    private String name;
    private String key;
    private String url;
    // endregion - Class Variables -
}
