package com.example.backendpfe.mapper;


import com.example.backendpfe.DTO.ConfigStoreDTO;
import com.example.backendpfe.entities.ConfigStore;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ConfigStoreMapper {
    ConfigStore toEntity(ConfigStoreDTO dto);
    ConfigStoreDTO toDTO(ConfigStore entity);
    List<ConfigStoreDTO> toDTO(List<ConfigStore> entity);
}

