package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.ConfigAttributsProduit;
import com.example.backendpfe.DTO.ConfigAttributsQuantiteProduit;
import com.example.backendpfe.DTO.ProduitDTO;
import com.example.backendpfe.DTO.ProduitDetailsDTO;
import com.example.backendpfe.entities.Produit;
import com.example.backendpfe.entities.Store;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;

public interface IServiceProduit {
    public ProduitDTO createProduit(ProduitDTO p);
    public ProduitDTO findProduitById(UUID id);
    public ProduitDTO updateProduit(ProduitDTO p);
    public List<ProduitDTO> findAllProduits();
    public void deleteProduit(UUID id);
    public boolean produitExist(UUID id);
    public int getQuantityByProductApiNonPaginee(String referenceId, Store store, String idProduitAttribuut, String quantityAttribut, String url);
    public int getQuantityByProductApiPaginee(String referenceId,String url, Store store,String IdProduitAttribuut,String quantityAttribut);
    public int getQuantityByProduct(String referenceId, UUID storeId);
    public Produit findProduitByReferenceIdAndStoreId(String referenceId, UUID storeId);

    public List<Produit> findProduitsByStore(UUID idStore);
    public String getNomParProduit(String referenceId, UUID storeId);
    public ProduitDTO getProduitDtoByRefId(String referenceId, UUID storeId);
    public ConfigAttributsProduit getConfigStoreAttributsProduit(UUID storeId);
    public double getPrix(ConfigAttributsProduit configAttributsProduit, Store store, JsonNode node);
    public String getNomCategorie(String attributCategorie, Store store, JsonNode node);
    public String getUrlImage(ConfigAttributsProduit configAttributsProduit, Store store, JsonNode node);
    public int calculStock(JsonNode node, String attributQuantity,Store store);
    public long nbreProduits();

}
