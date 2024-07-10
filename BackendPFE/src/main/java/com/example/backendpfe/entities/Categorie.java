package com.example.backendpfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String nom;
    @OneToMany(mappedBy = "categorie")
    @JsonIgnore
    List<Produit> produits;
    private UUID storeId;

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }


}
