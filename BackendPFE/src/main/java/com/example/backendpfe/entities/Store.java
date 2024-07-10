package com.example.backendpfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String nom;

    @OneToOne(mappedBy = "store",cascade = CascadeType.ALL)
    private ConfigStore configStore;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Produit> Produits = new ArrayList<>();

    private boolean archived;

    @Override
    public int hashCode() {
        return Objects.hash();
    }
    @Override
    public String toString() {
        return "Store{id=" + id + ", name='" + nom +"'}";
    }

}
