package com.example.backendpfe.repository;

import com.example.backendpfe.entities.ConfigStoreAttributs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConfigStoreAttributsRepository extends JpaRepository<ConfigStoreAttributs, UUID> {
}
