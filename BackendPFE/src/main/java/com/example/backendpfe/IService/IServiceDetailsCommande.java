package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.CommandeDTO;
import com.example.backendpfe.DTO.DetailsCommandeDTO;
import com.example.backendpfe.entities.Commande;
import com.example.backendpfe.entities.DetailsCommande;

import java.util.List;
import java.util.UUID;

public interface IServiceDetailsCommande {
    public DetailsCommandeDTO createDetailsCommande(DetailsCommandeDTO dto);
    public DetailsCommandeDTO updateDetailsCommande(DetailsCommandeDTO dto);
    public DetailsCommandeDTO findDetailsCommandeById(UUID id);
    public List<DetailsCommandeDTO> findAllDetailsCommandes();
    public void supprimerDetailsCommande(UUID id);
    public boolean DetailscommandeExist(UUID id);
    public List<DetailsCommandeDTO> findDetailsCommandeByCommande(CommandeDTO commande);
}
