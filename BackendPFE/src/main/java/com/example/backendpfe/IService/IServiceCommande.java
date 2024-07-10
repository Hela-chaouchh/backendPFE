package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.CommandeDTO;
import com.example.backendpfe.DTO.DetailsCommandeDTO;
import com.example.backendpfe.feign.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IServiceCommande {
    CommandeDTO create(CommandeDTO c, UUID idUser);
    public CommandeDTO updateCommande(CommandeDTO c);
    public CommandeDTO findCommandeById(UUID id);
    public List<CommandeDTO> findAllCommandes();
    public void deleteCommande(UUID id);
    public boolean commandeExist(UUID id);
    public List<CommandeDTO> findCommandeByClientId(UUID clientId);
    public User getClient(UUID id);

    public ResponseEntity<String> createCommandeSiteExterne(CommandeDTO commandeDTO, UUID idUser, UUID idStore) throws JsonProcessingException;
    public CommandeDTO verifStatusAndCreateCommand(CommandeDTO commandeDTO, UUID idUser, UUID idStore) throws JsonProcessingException;

//    public Map<String, Object> getPayload(UUID storeId) throws JsonProcessingException;
    public CommandeDTO createCommandeDifferentStores(CommandeDTO c, UUID idUser) throws JsonProcessingException;
    public long nbreCommandes();
    public double totalVentes();
    public double totalVentesByStore(UUID storeId);

}
