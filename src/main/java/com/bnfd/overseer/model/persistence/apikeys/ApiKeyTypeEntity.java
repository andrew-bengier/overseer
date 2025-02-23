package com.bnfd.overseer.model.persistence.apikeys;

import jakarta.persistence.*;
import lombok.*;

/**
 * ApiKeyTypeEntity - database representation of ApiKeyTypes
 * NOTE: this is just for completion - not to be used during CRUD
 * operations. There is a foreign key constraint on the api keys
 * table to insure no mal-formed data can be injected.
 */
@Data
@Entity
@Table(name = "api_key_types")
public class ApiKeyTypeEntity {
    // region - Class Variables -
    @Id
    private String id;

    private String type;
    // endregion - Class Variables -
}
