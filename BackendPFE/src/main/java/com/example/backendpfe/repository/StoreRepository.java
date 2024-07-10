package com.example.backendpfe.repository;

import com.example.backendpfe.DTO.StoreDTO;
import com.example.backendpfe.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    public List<Store> findStoresByNomStartingWith(String nom);
    public List<Store> findByNomContaining(String nom);

    public List<Store> findStoresByArchived(boolean archived);
}
