package com.example.backendpfe.controller;

import com.example.backendpfe.DTO.DetailsCommandeDTO;
import com.example.backendpfe.IService.IServiceCommande;
import com.example.backendpfe.IService.IServiceDetailsCommande;
import com.example.backendpfe.exception.CommandeNotfoundException;
import com.example.backendpfe.exception.DetailsCommandeNotfoundException;
import com.example.backendpfe.mapper.CommandeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/detailsCommande/")
public class DetailsCommandeController {
    @Autowired
    IServiceDetailsCommande iServiceDetailsCommande;
    @Autowired
    IServiceCommande iServiceCommande;
    @Autowired
    CommandeMapper commandeMapper;
    @GetMapping("")
    public List<DetailsCommandeDTO> getAll(){
        return iServiceDetailsCommande.findAllDetailsCommandes();
    }

    @PostMapping("add")
    public DetailsCommandeDTO ajoutDetailsCommande(@RequestBody DetailsCommandeDTO dto){
        return iServiceDetailsCommande.createDetailsCommande(dto);
    }

    @PutMapping("update")
    public DetailsCommandeDTO modifDetailsCommande(@RequestBody DetailsCommandeDTO dto){
        if(!iServiceDetailsCommande.DetailscommandeExist(dto.getId()))throw new DetailsCommandeNotfoundException();
        return  iServiceDetailsCommande.updateDetailsCommande(dto);
    }

    @DeleteMapping("delete/{id}")
    public String deleteDetailsCommande(@PathVariable UUID id){
        if(!iServiceDetailsCommande.DetailscommandeExist(id))throw new DetailsCommandeNotfoundException();
        iServiceDetailsCommande.supprimerDetailsCommande(id);
        return "DetailsCommande Supprimée";
    }

    @GetMapping("findById/{id}")
    public DetailsCommandeDTO getParId(@PathVariable UUID id){
        if(!iServiceDetailsCommande.DetailscommandeExist(id))throw new DetailsCommandeNotfoundException();
        return iServiceDetailsCommande.findDetailsCommandeById(id);
    }

    @GetMapping("findByCommande/{id}")
    public List<DetailsCommandeDTO> getAllparCommande(@PathVariable UUID id){
        if(!iServiceCommande.commandeExist(id))throw new CommandeNotfoundException();
        return iServiceDetailsCommande.findDetailsCommandeByCommande(iServiceCommande.findCommandeById(id));
    }
}
