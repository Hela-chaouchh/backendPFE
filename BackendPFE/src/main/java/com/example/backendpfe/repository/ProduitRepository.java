package com.example.backendpfe.repository;

import com.example.backendpfe.DTO.ProduitDTO;
import com.example.backendpfe.DTO.ProduitDetailsDTO;
import com.example.backendpfe.entities.Produit;
import com.example.backendpfe.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProduitRepository extends JpaRepository<Produit, UUID> {
    List<Produit> findProduitByReferenceId(String referenceId);

    List<Produit> findByStoreId(UUID idStore);
}
