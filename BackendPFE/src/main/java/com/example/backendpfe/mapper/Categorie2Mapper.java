package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.Categorie2DTO;
import com.example.backendpfe.DTO.CategorieDTO;
import com.example.backendpfe.entities.Categorie;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Categorie2Mapper {
    Categorie toEntity(Categorie2DTO dto);
    Categorie2DTO toDto(Categorie entity);
    List<Categorie2DTO> toDto(List<Categorie> entity);
}