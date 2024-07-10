package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.Produit;
import com.example.backendpfe.entities.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDTO {
    private UUID id;
    private String nom;
    private List<Produit> produits;
    private UUID storeId;
}
