package com.example.backendpfe.entities;

import com.example.backendpfe.entities.typeEnum.TypeBody;
import com.example.backendpfe.entities.typeEnum.TypeRemise;
import com.example.backendpfe.entities.typeEnum.TypeResponseApiGetProduct;
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
public class ConfigStore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String url;
    @Enumerated(EnumType.STRING)
    private TypeResponseApiGetProduct typeResponseApiGetProduit;
    private String paramsApiPaginable;
    private String cheminListProduit=""; //indique le chemin vers la liste des produits.
    private String APIAddCommande; //équivalent de l'api setOrder dans le store
    private String ApiLogin;
    private String email;
    private String password;
    private String cheminToken;//indique le chemin vers l'attribut token.
    @Enumerated(EnumType.STRING)
    private TypeBody typeBodyLogin;
    private String ApiGetUser; //équivalent de l'api getUser dans le store
    private String logo;
    private boolean typeAutorisationApiGetProduit;
    @Enumerated(EnumType.STRING)
    private TypeRemise typeRemise;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String payload; //payload commande
    private String urlPhoto;
    @OneToOne
    @JsonIgnore
    private Store store;

    @OneToMany(mappedBy = "configStore",cascade = CascadeType.ALL)
    private List<ConfigStoreAttributs> configStoreAttributs = new ArrayList<>();//List qui contient le nom de chaque attribut dans produit dans le fichier envoyé par l’api de store

    @Override
    public String toString() {
        return "configStore{id=" + id+"'}";
    }

}
