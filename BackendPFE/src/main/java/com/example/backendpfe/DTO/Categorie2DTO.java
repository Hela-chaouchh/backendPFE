package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.Produit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie2DTO {
    private UUID id;
    private String nom;
    private UUID storeId;
}
