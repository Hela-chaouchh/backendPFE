package com.example.backendpfe.controller;

import com.example.backendpfe.DTO.ProduitDTO;
import com.example.backendpfe.DTO.ProduitDetailsDTO;
import com.example.backendpfe.DTO.ProduitIdRefDTO;
import com.example.backendpfe.DTO.StoreDTO;
import com.example.backendpfe.IService.IServiceStore;
import com.example.backendpfe.entities.Produit;
import com.example.backendpfe.entities.Store;
import com.example.backendpfe.exception.StoreNotfoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/store/")
public class StoreController {
    @Autowired
    private IServiceStore iServiceStore;

    @GetMapping("")
    public List<StoreDTO> getAll(){
        return iServiceStore.findAllStores();
    }
    @GetMapping("findstoreById/{id}")
    public StoreDTO getParId(@PathVariable UUID id){
        if(!iServiceStore.storeExist(id))throw new StoreNotfoundException();
        return iServiceStore.findStoreById(id);
    }
    @PostMapping("add")
    public StoreDTO ajoutStore(@RequestBody StoreDTO s){
        return iServiceStore.createStore(s);
    }

    @PutMapping("update")
    public StoreDTO updateStore(@RequestBody StoreDTO s){
        if(!iServiceStore.storeExist(s.getId()))throw new StoreNotfoundException();
        return iServiceStore.updateStore(s);
    }

    @DeleteMapping("delete/{id}")
    public String delete (@PathVariable UUID id)
    {
        if(!iServiceStore.storeExist(id))throw new StoreNotfoundException();
        iServiceStore.deleteStore(id);
        return "Store supprimé";
    }
    @GetMapping("prodByStore/{id}")
    public List<ProduitIdRefDTO> getproduitsByStore(@PathVariable UUID id){
        return iServiceStore.findProduitsByStore(id);
    }

    @GetMapping("getAllproduitsByStore/{id}")
    public List<ProduitDetailsDTO> getAllproduitsByStore(@PathVariable UUID id){
        return iServiceStore.getAllProducts(id);
    }

    @DeleteMapping("deleteAllByStore/{id}")
    public String deleteAll(@PathVariable UUID id){
        if(!iServiceStore.storeExist(id))throw new StoreNotfoundException();
        iServiceStore.deleteAllProductsByStore(id);
        return "Tous les produits de Store sont supprimés";
    }

    @GetMapping("findStoreByNom/{nom}")
    public List<Store> getStoreByNom(@PathVariable String nom){
        return iServiceStore.findStoresByNomContaining(nom);
    }

    @GetMapping("getAllProductsApiPaginee/{idStore}")
    public List<ProduitDetailsDTO> getAll(@PathVariable UUID idStore){
        return iServiceStore.getAllProductsAPIPaginee(idStore);
    }

    @GetMapping("nbreStores")
    public long nbreStores(){
        return iServiceStore.nbreStores();
    }

    @GetMapping("filter/{nom}/{idStore}")
    public List<ProduitDetailsDTO> rechercheProduits(@PathVariable String nom, @PathVariable UUID idStore){
        return iServiceStore.filterProduit(nom,idStore);
    }

}
