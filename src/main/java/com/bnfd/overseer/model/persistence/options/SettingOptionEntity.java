package com.bnfd.overseer.model.persistence.options;

import com.bnfd.overseer.model.constants.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "setting_options")
public class SettingOptionEntity {
    // region - Class Variables -
    @Id
    private String id;
    private String name;
    @Enumerated(EnumType.STRING)
    private SettingType type;
    private String version;
    // endregion - Class Variables -
}
