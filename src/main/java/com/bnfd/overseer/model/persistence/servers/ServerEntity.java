package com.bnfd.overseer.model.persistence.servers;

import com.bnfd.overseer.model.persistence.apikeys.*;
import com.bnfd.overseer.model.persistence.libraries.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Entity
@Table(name = "servers")
@NoArgsConstructor
@AllArgsConstructor
public class ServerEntity {
    // region - Class Variables -
    @Id
    private String id;

    private String name;

    @OneToOne
    @JoinColumn(name = "api_key_id")
    private ApiKeyEntity apiKey;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ServerSettingEntity> settings;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<LibraryEntity> libraries;
    // endregion - Class Variables -

    // region - Constructors -
    public ServerEntity(String id, String name, ApiKeyEntity apiKey) {
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
    }

    public ServerEntity thinCopy() {
        return new ServerEntity(
                this.getId(),
                this.getName(),
                this.getApiKey()
        );
    }
    // endregion - Constructors -
}
