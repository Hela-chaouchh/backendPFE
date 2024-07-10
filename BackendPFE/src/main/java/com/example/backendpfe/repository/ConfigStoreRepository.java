package com.example.backendpfe.repository;

import com.example.backendpfe.entities.ConfigStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConfigStoreRepository extends JpaRepository<ConfigStore, UUID> {
}
