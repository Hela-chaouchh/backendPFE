package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.ConfigStoreAttributsDTO;
import com.example.backendpfe.entities.ConfigStoreAttributs;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ConfigStoreAttributsMapper {
    ConfigStoreAttributs toEntity(ConfigStoreAttributsDTO dto);
    ConfigStoreAttributsDTO toDTO(ConfigStoreAttributs entity);
    List<ConfigStoreAttributsDTO> toDTO(List<ConfigStoreAttributs> entity);
}


