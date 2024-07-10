package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.CommandeDTO;
import com.example.backendpfe.entities.Commande;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")

public interface CommandeMapper {
    Commande toEntity(CommandeDTO dto);
    List<Commande> toEntity(List<CommandeDTO> dto);
    CommandeDTO toDto(Commande entity);
    List<CommandeDTO> toDto(List<Commande> entity);
}
