package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.Categorie;
import com.example.backendpfe.entities.Commande;
import com.example.backendpfe.entities.DetailsCommande;
import com.example.backendpfe.entities.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitDTO {
    private UUID id;
    private String nom;
    private int quantite;
    private double prix;
    private String referenceId;
    private String photo;
    private Store store;
    private List<DetailsCommande> detailsCommandes = new ArrayList<>();
    private Categorie categorie;
}