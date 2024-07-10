package com.example.backendpfe.entities;

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
@Entity
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String referenceId ; //l'id de ce produit dans le site externe

    @ManyToOne
    private Store store;

    @OneToMany(mappedBy = "produit",cascade = CascadeType.ALL)
    @JsonIgnore
    List<DetailsCommande> detailsCommandes = new ArrayList<>();

    @ManyToOne
    private Categorie categorie;


    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                "referenceId=" +referenceId+
                '}';
    }


}
