package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.*;
import com.example.backendpfe.entities.Produit;
import com.example.backendpfe.entities.Store;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

public interface IServiceStore {
    public StoreDTO createStore(StoreDTO dto);

    public StoreDTO updateStore(StoreDTO dto);

    public StoreDTO findStoreById(UUID id);

    public List<StoreDTO> findAllStores();

    public void deleteStore(UUID id);

    public boolean storeExist(UUID id);
    public List<ProduitIdRefDTO> findProduitsByStore(UUID id);
    public List<ProduitDetailsDTO> getAllProduitsAPINonPaginee(UUID id);
    public void deleteAllProductsByStore(UUID id);
    public List<Store> findStoresByNomContaining(String nom);
    //public ConfigAttributsProduit getConfigStoreAttributsProduit(UUID storeId);

    public List<ProduitDetailsDTO> getAllProductsAPIPaginee(UUID idStore);

    public List<ProduitDetailsDTO> getAllProducts(UUID idStore);
    public long nbreStores();
    public List<ProduitDetailsDTO> filterProduit(String nom, UUID idStore);
}
