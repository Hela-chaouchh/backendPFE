package com.example.backendpfe.controller;

import com.example.backendpfe.DTO.CommandeDTO;
import com.example.backendpfe.IService.IServiceCommande;
import com.example.backendpfe.exception.ClientNotfoundException;
import com.example.backendpfe.exception.CommandeNotfoundException;
import com.example.backendpfe.feign.User;
import com.example.backendpfe.mapper.CommandeMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/commande/")
public class CommandeController {
    @Autowired
    IServiceCommande iServiceCommande;
    @Autowired
    CommandeMapper commandeMapper;

    @GetMapping("")
    public List<CommandeDTO> getAll(){
        return iServiceCommande.findAllCommandes();
    }



    @PutMapping("update")
    public CommandeDTO modifCommande(@RequestBody CommandeDTO c){
        if(!iServiceCommande.commandeExist(c.getId()))throw new CommandeNotfoundException();
        return  iServiceCommande.updateCommande(c);
    }

    @DeleteMapping("delete/{id}")
    public String deleteCommande(@PathVariable UUID id){
        if(!iServiceCommande.commandeExist(id))throw new CommandeNotfoundException();
        iServiceCommande.deleteCommande(id);
        return "Commande Supprimée";
    }

    @GetMapping("findById/{id}")
    public CommandeDTO getParId(@PathVariable UUID id){
        if(!iServiceCommande.commandeExist(id))throw new CommandeNotfoundException();
        return iServiceCommande.findCommandeById(id);
    }

    @PostMapping("addOrder/{idUser}/{idStore}")
    public CommandeDTO ajout(@RequestBody CommandeDTO commandeDTO ,@PathVariable UUID idUser){
       // if(iServiceCommande.getClient(idUser) != null){
            return iServiceCommande.create(commandeDTO, idUser);
       /* }
        else{
            throw new ClientNotfoundException();
        }
        */
    }

    @GetMapping("findcommandesByClient/{idClient}")
    public List<CommandeDTO> findCommandesByClient(@PathVariable UUID idClient){
        if(iServiceCommande.getClient(idClient) != null){
            return iServiceCommande.findCommandeByClientId(idClient);
        }
        else{
            throw new ClientNotfoundException();
        }
    }

    @GetMapping("getclient/{id}")
    public User getClient(@PathVariable UUID id){
        return iServiceCommande.getClient(id);
    }

    @PostMapping("addWithPayload/{idUser}/{idStore}")
    public CommandeDTO ajoutCommandeWithPayload(@RequestBody CommandeDTO commandeDTO , @PathVariable UUID idUser, @PathVariable UUID idStore) throws JsonProcessingException {
        if(iServiceCommande.getClient(idUser) != null){
            return iServiceCommande.verifStatusAndCreateCommand(commandeDTO, idUser,idStore);
        }
        else{
            throw new ClientNotfoundException();
        }
    }

    @PostMapping("addCommandDifferentStores/{idUser}")
    public CommandeDTO ajoutCommandeDifferentStores(@RequestBody CommandeDTO commandeDTO , @PathVariable UUID idUser) throws JsonProcessingException {
        if(iServiceCommande.getClient(idUser) != null){
            return iServiceCommande.createCommandeDifferentStores(commandeDTO, idUser);
        }
        else{
            throw new ClientNotfoundException();
        }
    }

    @GetMapping("nbreCommandes")
    public long nbreCommandes(){
        return iServiceCommande.nbreCommandes();
    }

    @GetMapping("totalVentes")
    public double totalVentes(){
        return iServiceCommande.totalVentes();
    }
    @GetMapping("totalVentesByStore/{storeId}")
    public double totalVentesByStore(@PathVariable UUID storeId){
        return iServiceCommande.totalVentesByStore(storeId);
    }

}
