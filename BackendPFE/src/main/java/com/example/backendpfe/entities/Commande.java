package com.example.backendpfe.entities;

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
@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String dateCommande;
    private double montant;
    private UUID client_id;
    @OneToMany(mappedBy = "commande",cascade = CascadeType.ALL)
    private List<DetailsCommande> detailsCommandes = new ArrayList<>();

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", dateCommande='" + dateCommande + '\'' +
                ", montant=" + montant +
                ", client_id=" + client_id +
                ", detailsCommandes=" + detailsCommandes +
                '}';
    }

}
