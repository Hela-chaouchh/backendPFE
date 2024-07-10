package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.Categorie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitDetailsDTO {
    private UUID id;
    private String referenceId;
    private String nom;
    private int quantite;
    private double prix;
    private String photo;
    private Categorie categorie;
}
