package com.example.backendpfe.controller;


import com.example.backendpfe.DTO.ConfigStoreDTO;
import com.example.backendpfe.IService.IServiceConfigStore;
import com.example.backendpfe.entities.ConfigStore;
import com.example.backendpfe.exception.ConfigStoreNotfoundException;
import com.example.backendpfe.exception.StoreNotfoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/configstore/")
public class ConfigStoreController {

    @Autowired
    IServiceConfigStore iServiceConfigStore;


    @GetMapping("")
    public List<ConfigStoreDTO> getAll(){
        return iServiceConfigStore.findAllConfigStores();
    }
    @GetMapping("findConfigStoreById/{id}")
    public ConfigStoreDTO getParId(@PathVariable UUID id){
        if(!iServiceConfigStore.configStoreExist(id))throw new ConfigStoreNotfoundException();
        return iServiceConfigStore.findConfigStoreById(id);
    }
    @PostMapping("add")
    public ConfigStoreDTO ajoutConfigStore(@RequestBody ConfigStoreDTO dto){
        return iServiceConfigStore.createConfigStore(dto);
    }

    @PutMapping("update")
    public ConfigStoreDTO updateConfigStore(@RequestBody ConfigStoreDTO dto){
        if(!iServiceConfigStore.configStoreExist(dto.getId()))throw new ConfigStoreNotfoundException();
        return iServiceConfigStore.updateConfigStore(dto);
    }


    @DeleteMapping("delete/{id}")
    public String delete (@PathVariable UUID id)
    {
        if(!iServiceConfigStore.configStoreExist(id))throw new ConfigStoreNotfoundException();
        iServiceConfigStore.deleteConfigStore(id);
        return "ConfigStore supprimé";
    }
}
