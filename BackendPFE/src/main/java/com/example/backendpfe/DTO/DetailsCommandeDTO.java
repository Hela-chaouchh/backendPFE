package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.Commande;
import com.example.backendpfe.entities.Produit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsCommandeDTO {
    private UUID id;
    private Commande commande;
    private Produit produit;
    private double prixUnitaire;
    private int quantite;
}
