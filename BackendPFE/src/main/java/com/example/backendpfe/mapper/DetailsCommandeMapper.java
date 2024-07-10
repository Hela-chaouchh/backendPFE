package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.DetailsCommandeDTO;
import com.example.backendpfe.entities.DetailsCommande;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")

public interface DetailsCommandeMapper {
    DetailsCommande toEntity(DetailsCommandeDTO dto);
    DetailsCommandeDTO toDTO(DetailsCommande entity);
    List<DetailsCommandeDTO> toDTO(List<DetailsCommande> entity);
}


