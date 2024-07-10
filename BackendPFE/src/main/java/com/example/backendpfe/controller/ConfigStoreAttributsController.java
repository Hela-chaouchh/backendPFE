package com.example.backendpfe.controller;

import com.example.backendpfe.DTO.ConfigAttributsCommande;
import com.example.backendpfe.DTO.ConfigAttributsParamsApiPageable;
import com.example.backendpfe.DTO.ConfigAttributsProduit;
import com.example.backendpfe.DTO.ConfigStoreAttributsDTO;
import com.example.backendpfe.IService.IServiceConfigStoreAttributs;
import com.example.backendpfe.entities.ConfigStoreAttributs;
import com.example.backendpfe.exception.ConfigStoreAttributsNotfoundException;
import com.example.backendpfe.exception.StoreNotfoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/configstoreattributs/")
public class ConfigStoreAttributsController {
    @Autowired
    IServiceConfigStoreAttributs iServiceConfigStoreAttributs;



    @GetMapping("")
    public List<ConfigStoreAttributsDTO> getAll(){
        return iServiceConfigStoreAttributs.findAllConfigStoresAttributs();
    }
    @GetMapping("findCSAById/{id}")
    public ConfigStoreAttributsDTO getParId(@PathVariable UUID id){
        if(!iServiceConfigStoreAttributs.configStoreAttributsExist(id))throw new ConfigStoreAttributsNotfoundException();
        return iServiceConfigStoreAttributs.findConfigStoreAttributById(id);
    }
    @PostMapping("add")
    public ConfigStoreAttributsDTO ajoutCSA(@RequestBody ConfigStoreAttributsDTO dto){
        return iServiceConfigStoreAttributs.createConfigStoreAttribut(dto);
    }

    @PutMapping("update")
    public ConfigStoreAttributsDTO updateCSA(@RequestBody ConfigStoreAttributsDTO c){
        if(!iServiceConfigStoreAttributs.configStoreAttributsExist(c.getId()))throw new ConfigStoreAttributsNotfoundException();
        return iServiceConfigStoreAttributs.updateConfigStoreAttribut(c);
    }


    @DeleteMapping("delete/{id}")
    public String delete (@PathVariable UUID id)
    {
        if(!iServiceConfigStoreAttributs.configStoreAttributsExist(id))throw new ConfigStoreAttributsNotfoundException();
        iServiceConfigStoreAttributs.deleteConfigStoreAttribut(id);
        return "ConfigStoreAttribut supprimé";
    }

    @GetMapping("getCSAByStore/{storeId}")
    public List<ConfigStoreAttributsDTO> findConfigStoreAttributsByStore(@PathVariable UUID storeId){
        return iServiceConfigStoreAttributs.findConfigStoreAttributsByStore(storeId);
    }
    @GetMapping("configAttributs")
    public List<String> getKeysConfigAttributs(){
        List<String> keys = new ArrayList<>();
        keys.addAll(iServiceConfigStoreAttributs.getKeys(ConfigAttributsProduit.class));
        keys.addAll(iServiceConfigStoreAttributs.getKeys(ConfigAttributsCommande.class));
        return keys;
    }
    @GetMapping("configAttributsProduit")
    public List<String> getKeysConfigAttributsProduit(){
        return iServiceConfigStoreAttributs.getKeys(ConfigAttributsProduit.class);
    }
    @GetMapping("configAttributsCommande")
    public List<String> getKeysConfigAttributsCommande(){
        return iServiceConfigStoreAttributs.getKeys(ConfigAttributsCommande.class);
    }
    @GetMapping("configAttributsParamsApiPageable")
    public List<String> getKeysConfigAttributsParamsApiPageable(){
        return iServiceConfigStoreAttributs.getKeys(ConfigAttributsParamsApiPageable.class);
    }
}