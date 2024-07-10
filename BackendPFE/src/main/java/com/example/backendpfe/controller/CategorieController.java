package com.example.backendpfe.controller;

import com.example.backendpfe.DTO.Categorie2DTO;
import com.example.backendpfe.DTO.CategorieDTO;
import com.example.backendpfe.DTO.ProduitDTO;
import com.example.backendpfe.DTO.ProduitDetailsDTO;
import com.example.backendpfe.IService.IServiceCategorie;
import com.example.backendpfe.IService.IServiceStore;
import com.example.backendpfe.entities.Categorie;
import com.example.backendpfe.exception.CategorieNotfoundException;
import com.example.backendpfe.exception.ProduitNotfoundException;
import com.example.backendpfe.exception.StoreNotfoundException;
import com.example.backendpfe.service.ServiceCategorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categorie/")
public class CategorieController {
    @Autowired
    IServiceCategorie iServiceCategorie;
    @Autowired
    IServiceStore iServiceStore;

    @GetMapping("getById/{id}")
    public CategorieDTO getCategorieById(@PathVariable UUID id){
        if(!iServiceCategorie.CategorieExist(id))throw new CategorieNotfoundException();
        return iServiceCategorie.findCategorieById(id);
    }
    @GetMapping("")
    public List<CategorieDTO> getAll(){
        return iServiceCategorie.findAllCategories();
    }
    @PostMapping("add")
    public CategorieDTO ajoutCategorie(@RequestBody CategorieDTO dto){
        UUID StoreId = dto.getStoreId();
        if(!iServiceStore.storeExist(StoreId)) throw new StoreNotfoundException();
        return iServiceCategorie.createCategorie(dto);
    }
    @PutMapping("update")
    public CategorieDTO modifCategorie(@RequestBody CategorieDTO dto){
        if(!iServiceCategorie.CategorieExist(dto.getId()))throw new CategorieNotfoundException();
        return iServiceCategorie.updateCategorie(dto);
    }
    @DeleteMapping("delete/{id}")
    public String deleteCategorie(@PathVariable UUID id){
        if(!iServiceCategorie.CategorieExist(id))throw new CategorieNotfoundException();
        iServiceCategorie.deleteCategorie(id);
        return "Categorie supprimée";
    }
    @GetMapping("getCategoriesByStore/{storeId}")
    public List<Categorie2DTO> getCategories(@PathVariable UUID storeId){
        return iServiceCategorie.getCategoriesByStore(storeId);
    }
    @GetMapping("getCategoriesByStoreApiPaginee/{storeId}")
    public List<Categorie2DTO> getCategoriesApiPaginee(@PathVariable UUID storeId){
        return iServiceCategorie.getCategoriesByStoreApiPaginee(storeId);
    }

    @DeleteMapping("deleteAllCategoriesByStore/{idStore}")
    public String deleteAll(@PathVariable UUID idStore){
        if(!iServiceStore.storeExist(idStore))throw new StoreNotfoundException();
        iServiceCategorie.deleteAllCategoriesByStore(idStore);
        return "Tous les categories de Store sont supprimés";
    }

    @GetMapping("getProduitsByCategorie/{nomCategorie}/{storeId}")
    public List<ProduitDTO> getProduitsByCategorie(@PathVariable String nomCategorie, @PathVariable UUID storeId){
        return iServiceCategorie.listProduitsParCategorie(nomCategorie,storeId);
    }

}
