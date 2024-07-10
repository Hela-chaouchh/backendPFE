package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.ProduitIdRefDTO;
import com.example.backendpfe.entities.Produit;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProduitIdRefMapper {
    Produit toEntity(ProduitIdRefDTO dto);
    ProduitIdRefDTO toDTO(Produit entity);
    List<ProduitIdRefDTO> toDTO(List<Produit> entity);
}
