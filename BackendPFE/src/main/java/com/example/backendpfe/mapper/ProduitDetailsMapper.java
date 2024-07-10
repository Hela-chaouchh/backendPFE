package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.ProduitDTO;
import com.example.backendpfe.DTO.ProduitDetailsDTO;
import com.example.backendpfe.entities.Produit;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProduitDetailsMapper {
    Produit toEntity(ProduitDetailsDTO dto);
    ProduitDetailsDTO toDTO(Produit entity);
    List<ProduitDetailsDTO> toDTOEntities(List<Produit> produitList);
    List<ProduitDetailsDTO> toDTO(List<ProduitDTO> produitList);
}
