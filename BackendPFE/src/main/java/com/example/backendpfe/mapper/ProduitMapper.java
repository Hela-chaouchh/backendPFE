package com.example.backendpfe.mapper;


import com.example.backendpfe.DTO.ProduitDTO;
import com.example.backendpfe.entities.Produit;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ProduitMapper {
    Produit toEntity(ProduitDTO dto);
    ProduitDTO toDTO(Produit entity);
    List<ProduitDTO> toDTO(List<Produit> entity);

}


