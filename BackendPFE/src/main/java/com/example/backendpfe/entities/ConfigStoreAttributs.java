package com.example.backendpfe.entities;

import com.example.backendpfe.entities.typeEnum.ConfigMethode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConfigStoreAttributs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String attributeKey;
    private String attributeValue;
    private String chemin;
    @Enumerated(EnumType.STRING)
    private ConfigMethode configMethode;

    @ManyToOne
    @JsonIgnore
    private ConfigStore configStore;

}
