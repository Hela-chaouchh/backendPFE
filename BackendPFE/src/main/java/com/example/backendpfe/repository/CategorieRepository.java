package com.example.backendpfe.repository;

import com.example.backendpfe.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategorieRepository extends JpaRepository<Categorie, UUID> {
    public List<Categorie> findCategoriesByStoreId(UUID storeId);
    public List<Categorie> findCategorieByNom(String nom);
    public Categorie findCategoriesByStoreIdAndNom(UUID storeId, String nom);
}
