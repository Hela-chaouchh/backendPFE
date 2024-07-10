package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.Categorie;
import com.example.backendpfe.entities.ConfigStore;
import com.example.backendpfe.entities.Produit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private UUID id;
    private String nom;
    private ConfigStore configStore;
    private List<Produit> Produits = new ArrayList<>();
    private boolean archived;
}

