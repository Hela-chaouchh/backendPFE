package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.CategorieDTO;
import com.example.backendpfe.entities.Categorie;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategorieMapper {
    Categorie toEntity(CategorieDTO dto);
    CategorieDTO toDto(Categorie entity);
    List<CategorieDTO> toDto(List<Categorie> entity);
}
