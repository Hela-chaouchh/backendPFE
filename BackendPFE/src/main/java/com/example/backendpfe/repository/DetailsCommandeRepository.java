package com.example.backendpfe.repository;

import com.example.backendpfe.DTO.CommandeDTO;
import com.example.backendpfe.DTO.DetailsCommandeDTO;
import com.example.backendpfe.entities.Commande;
import com.example.backendpfe.entities.DetailsCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DetailsCommandeRepository extends JpaRepository<DetailsCommande, UUID> {
    public List<DetailsCommandeDTO> findDetailsCommandeByCommande(CommandeDTO commande);
}
