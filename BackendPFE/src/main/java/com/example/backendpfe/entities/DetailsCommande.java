package com.example.backendpfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnore
    private  Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
    private double prixUnitaire;
    private int quantite;

    @Override
    public String toString() {
        return "DetailsCommande{" +
                "id=" + id +
                ", produit=" + produit +
                ", prixUnitaire=" + prixUnitaire +
                ", quantite=" + quantite +
                '}';
    }
}
