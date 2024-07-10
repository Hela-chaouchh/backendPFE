package com.example.backendpfe.repository;

import com.example.backendpfe.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommandeRepository extends JpaRepository<Commande, UUID> {
}
