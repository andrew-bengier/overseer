package com.bnfd.overseer.model.persistence.servers;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@Data
@Entity
@Table(name = "server_settings")
public class ServerSettingEntity implements Serializable, Comparable<ServerSettingEntity> {
    // region - Class Variables -
    @Id
    private String id;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    private String type;

    private String name;

    private String val;
    // endregion - Class Variables -

    // region - Overridden Methods -
    @Override
    public int compareTo(ServerSettingEntity setting) {
        // Compare using name
        return this.getId().compareTo(setting.getId());
    }
    // endregion - Overridden Methods -
}
