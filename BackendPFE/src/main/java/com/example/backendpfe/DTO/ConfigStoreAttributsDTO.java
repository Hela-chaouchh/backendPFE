package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.ConfigStore;
import com.example.backendpfe.entities.typeEnum.ConfigMethode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigStoreAttributsDTO {
    private UUID id;
    private String attributeKey;
    private String attributeValue;
    private String chemin;
    private ConfigMethode configMethode;
    private ConfigStore configStore;

}
