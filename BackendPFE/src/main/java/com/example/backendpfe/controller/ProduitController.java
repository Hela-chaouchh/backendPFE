package com.example.backendpfe.controller;

import com.example.backendpfe.DTO.ProduitDTO;
import com.example.backendpfe.IService.IServiceProduit;
import com.example.backendpfe.IService.IServiceStore;
import com.example.backendpfe.entities.Produit;
import com.example.backendpfe.exception.ProduitNotfoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/product/")
public class ProduitController {
    @Autowired
    IServiceProduit iServiceProduit;
    IServiceStore iServiceStore;

    @GetMapping("")
    public List<ProduitDTO> getAll(){
        return iServiceProduit.findAllProduits();
    }
    @GetMapping("findproductById/{id}")
    public ProduitDTO getParId(@PathVariable UUID id){
        if(!iServiceProduit.produitExist(id))throw new ProduitNotfoundException();
        return iServiceProduit.findProduitById(id);
    }
    @PostMapping("add")
    public ProduitDTO ajoutProduit(@RequestBody ProduitDTO p){
        return iServiceProduit.createProduit(p);
    }

    @PutMapping("update")
    public ProduitDTO updateProduit(@RequestBody ProduitDTO p){
        if(!iServiceProduit.produitExist(p.getId()))throw new ProduitNotfoundException();
        return iServiceProduit.updateProduit(p);
    }


    @DeleteMapping("delete/{id}")
    public String delete (@PathVariable UUID id)
    {
        if(!iServiceProduit.produitExist(id))throw new ProduitNotfoundException();
        iServiceProduit.deleteProduit(id);
        return "produit supprimé";
    }
    
    @GetMapping("QuantityByProd/{refId}/{storeId}")
    public int getQuantityByProduit(@PathVariable String refId,@PathVariable UUID storeId) throws JsonProcessingException {
        Produit produit = iServiceProduit.findProduitByReferenceIdAndStoreId(refId,storeId);
        if(!iServiceProduit.produitExist(produit.getId()))throw new ProduitNotfoundException();
        return iServiceProduit.getQuantityByProduct(refId,storeId);
    }

    @GetMapping("NameByProd/{refId}/{storeId}")
    public String getNomByProduit(@PathVariable String refId,@PathVariable UUID storeId) {
        Produit produit = iServiceProduit.findProduitByReferenceIdAndStoreId(refId,storeId);
        if(!iServiceProduit.produitExist(produit.getId()))throw new ProduitNotfoundException();
        return iServiceProduit.getNomParProduit(refId,storeId);
    }

    @GetMapping("prodDtoByRefId/{refId}/{storeId}")
    public ProduitDTO getProduitDtoByRefId(@PathVariable String refId, @PathVariable UUID storeId) {
        Produit produit = iServiceProduit.findProduitByReferenceIdAndStoreId(refId,storeId);
        if(!iServiceProduit.produitExist(produit.getId()))throw new ProduitNotfoundException();
        return iServiceProduit.getProduitDtoByRefId(refId,storeId);
    }


    @GetMapping("findProdbyRefId/{refId}/{storeId}")
    public Produit getProdByRefId(@PathVariable String refId, @PathVariable UUID storeId){
        return iServiceProduit.findProduitByReferenceIdAndStoreId(refId, storeId);
    }

    @GetMapping("nbreProduits")
    public long nbreProduits(){
        return iServiceProduit.nbreProduits();
    }


}
