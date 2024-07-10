package com.example.backendpfe.service;

import com.example.backendpfe.DTO.*;
import com.example.backendpfe.IService.IServiceApiExterne;
import com.example.backendpfe.IService.IServiceConfigStoreAttributs;
import com.example.backendpfe.IService.IServiceStore;
import com.example.backendpfe.entities.*;
import com.example.backendpfe.entities.typeEnum.TypeResponseApiGetProduct;
import com.example.backendpfe.exception.StoreNotfoundException;
import com.example.backendpfe.mapper.*;
import com.example.backendpfe.repository.StoreRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ServiceStore implements IServiceStore {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ServiceConfigStore serviceConfigStore;
    @Autowired
    private ServiceProduit serviceProduit;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private ConfigStoreMapper configStoreMapper;
    @Autowired
    private ProduitMapper produitMapper;
    @Autowired
    private IServiceConfigStoreAttributs iServiceConfigStoreAttributs;
    @Autowired
    private ProduitDetailsMapper produitDetailsMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private IServiceApiExterne iServiceApiExterne;
    @Autowired
    private ProduitIdRefMapper produitIdRefMapper;

    /**
     * Créer un store.
     * @param storeDTO {@link StoreDTO}
     * @return {@link StoreDTO}
     */
    @Override
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store storeToCreate = storeMapper.toEntity(storeDTO);
        storeToCreate.setArchived(false);
        Store store = storeRepository.save(storeToCreate);

        ConfigStore cs = store.getConfigStore();
        cs.setStore(store);
        serviceConfigStore.createConfigStore(configStoreMapper.toDTO(cs));

        for (Produit p : store.getProduits()) {
            p.setStore(store);
            serviceProduit.createProduit(produitMapper.toDTO(p));
        }
        return storeMapper.toDTO(store);
    }


    /**
     * Cette méthode permet de modifier un store.
     *
     * @param storeDTO {@link StoreDTO}
     * @return {@link StoreDTO}
     */
    @Override
    public StoreDTO updateStore(StoreDTO storeDTO) {
        //mapper les données du DTO vers l'entité Store avant de les sauvegarder
        Store storeToUpdate = storeMapper.toEntity(storeDTO);
        // Enregistrez l'entité mise à jour dans le repository
        Store updatedStore = storeRepository.save(storeToUpdate);

        ConfigStore cs = updatedStore.getConfigStore();
        cs.setStore(updatedStore);
        serviceConfigStore.updateConfigStore(configStoreMapper.toDTO(cs));

        for (Produit p : updatedStore.getProduits()) {
            p.setStore(updatedStore);
            serviceProduit.createProduit(produitMapper.toDTO(p));
        }
        return storeMapper.toDTO(updatedStore);
    }


    /**
     * Trouve et retourne les informations d'un store basé sur son id.
     * @param id  {@link UUID}
     * @return {@link StoreDTO}
     * @throws StoreNotfoundException si le store n'est pas trouvé.
     */
    @Override
    public StoreDTO findStoreById(UUID id) {
        Optional<Store> storeOptional = storeRepository.findById(id);
        if (storeOptional.isPresent()) {
            return storeMapper.toDTO(storeOptional.get());
        } else throw new StoreNotfoundException();
    }

    /**
     * Trouve et retourne la liste de tous les stores qui ne sont pas archivés.
     * @return {@link List<StoreDTO>}
     */
    @Override
    public List<StoreDTO> findAllStores() {
        List<Store> allStores = storeRepository.findStoresByArchived(false);
        return storeMapper.toDTO(allStores);
    }

    /**
     * Archive un store en le marquant comme supprimé
     *
     * @param id {@link UUID}
     */
    @Override
    public void deleteStore(UUID id) {
        StoreDTO storeDTO = findStoreById(id);
        storeDTO.setArchived(true);
        updateStore(storeDTO);
    }


    /**
     * Vérifie si un store existe.
     * @param id {@link UUID}
     * @return true si le store existe, sinon false.
     */
    @Override
    public boolean storeExist(UUID id) {
        return storeRepository.existsById(id);
    }

    /**
     * Trouve et retourne la liste des produits d'un store.
     *
     * @param id {@link UUID}
     * @return {@link List<ProduitIdRefDTO>}
     * @throws StoreNotfoundException si le store n'est pas trouvé.
     */
    @Override
    public List<ProduitIdRefDTO> findProduitsByStore(UUID id) {
        Optional<Store> storeOptional = storeRepository.findById(id);
        if (storeOptional.isPresent()) {
            return produitIdRefMapper.toDTO(storeOptional.get().getProduits());
        } else throw new StoreNotfoundException();
    }

    /**
     * Cette méthode récupère la liste de produits à partir d'un nœud JSON et cée le produit qui n'existe pas.
     *
     * @param jsonNode               {@link JsonNode}
     * @param configAttributsProduit {@link ConfigAttributsProduit}
     * @param store                  {@link Store}
     */
    public List<ProduitDTO> setAndSaveProduit(JsonNode jsonNode, ConfigAttributsProduit configAttributsProduit, Store store) {
        List<ProduitDTO> listProduits = new ArrayList<>();
        for (JsonNode node : jsonNode) {
            ProduitDTO produit = new ProduitDTO();
            if (node != null) {
                produit.setStore(store);
                produit.setReferenceId(node.get(configAttributsProduit.getId()).asText());
                Produit produitExist = serviceProduit.findProduitByReferenceIdAndStoreId(node.get(configAttributsProduit.getId()).asText(), store.getId());
                if (produitExist == null) {
                    serviceProduit.createProduit(produit);
                }
                produit.setNom(node.get(configAttributsProduit.getName()).asText());
                produit.setQuantite(serviceProduit.calculStock(node, configAttributsProduit.getQuantity(), store));
                produit.setPrix(serviceProduit.getPrix(configAttributsProduit, store, node));
                produit.setPhoto(serviceProduit.getUrlImage(configAttributsProduit, store, node));
                String categorie = serviceProduit.getNomCategorie(configAttributsProduit.getCategorie(), store, node);
                Categorie categorieProduit = new Categorie();
                categorieProduit.setStoreId(store.getId());
                categorieProduit.setNom(categorie);
                produit.setCategorie(categorieProduit);
                listProduits.add(produit);
            }
        }
        return listProduits;
    }


    /**
     * Récupère tous les produits associés à un Store en utilisant une API externe non paginée.
     *
     * @param idStore L'identifiant de Store
     * @return List<ProduitDetailsDTO> - contenant les produits récupérés
     */
    @Override
    public List<ProduitDetailsDTO> getAllProduitsAPINonPaginee(UUID idStore) {
        Optional<Store> optionalStore = storeRepository.findById(idStore);
        List<ProduitDTO> produitList = new ArrayList<>();
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            ConfigAttributsProduit configAttributsProduit = serviceProduit.getConfigStoreAttributsProduit(idStore);
            String url = store.getConfigStore().getUrl();
            ResponseEntity<String> listProduitsJson = null;
            boolean typeAutorisation = store.getConfigStore().isTypeAutorisationApiGetProduit();
            if(!typeAutorisation){
                listProduitsJson = iServiceApiExterne.apiExterneMethodeGet(url);
            }
            else{
                String token = iServiceApiExterne.getToken(storeMapper.toDTO(store));
                listProduitsJson = iServiceApiExterne.apiExterneMethodeGetAvecAutorisation(token,url);
            }
            JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(listProduitsJson);
            if (jsonNode != null && !jsonNode.isEmpty()) {
                String productList = store.getConfigStore().getCheminListProduit();
                jsonNode = iServiceApiExterne.getNoeudSelonChemin(jsonNode, productList);
                produitList = setAndSaveProduit(jsonNode, configAttributsProduit, store);
            }
        }
        return produitDetailsMapper.toDTO(produitList);
    }


    /**
     * Supprimer tous les produits d'un store.
     *
     * @param idStore {@link UUID} l'identifient de store
     */
    @Override
    public void deleteAllProductsByStore(UUID idStore) {
        List<Produit> produitsToDelete = serviceProduit.findProduitsByStore(idStore);
        for (Produit p : produitsToDelete) {
            serviceProduit.deleteProduit(p.getId());
        }
    }

    /**
     * Trouve et retourne la liste des stores dont le nom contient la chaîne spécifiée.
     *
     * @param nom la chaîne à rechercher dans les noms des stores.
     * @return {@link List<Store>}
     */
    @Override
    public List<Store> findStoresByNomContaining(String nom) {
        return storeRepository.findByNomContaining(nom);
    }

    /**
     * Récupère tous les produits associés à un Store en utilisant une API externe paginée.
     *
     * @param idStore {@link UUID}
     * @return List<ProduitDetailsDTO> - contenant les produits récupérés
     */
    @Override
    public List<ProduitDetailsDTO> getAllProductsAPIPaginee(UUID idStore) {
        Optional<Store> optionalStore = storeRepository.findById(idStore);
        List<ProduitDTO> produitList = new ArrayList<>();
        List<ProduitDTO> produits = new ArrayList<>();
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            ConfigAttributsProduit configAttributsProduit = serviceProduit.getConfigStoreAttributsProduit(idStore);
            ConfigAttributsApiPaginee configAttributsApiPaginee = iServiceConfigStoreAttributs.getConfigAttributsApiPaginee(store);
            String url = store.getConfigStore().getUrl();
            ResponseEntity<String> listProduitsJson = iServiceApiExterne.responseApiPagineeGet(store, configAttributsApiPaginee.getPageNumber(), configAttributsApiPaginee.getPageSize(),configAttributsApiPaginee.getTotalElements(),url);
            JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(listProduitsJson);
            if (jsonNode != null && !jsonNode.isEmpty()) {
                String cheminListProduits = store.getConfigStore().getCheminListProduit();
                JsonNode NodeProduits = iServiceApiExterne.getNoeudSelonChemin(jsonNode, cheminListProduits);
                produitList = setAndSaveProduit(NodeProduits, configAttributsProduit, store);
                produits.addAll(produitList);
            }
        }
        return produitDetailsMapper.toDTO(produits);
    }


    /**
     * Récupère tous les produits associés à un Store en appelant la méthode appropriée en fonction du type d'API utilisé (soit non paginée, soit paginée).
     * @param idStore {@link UUID}
     * @return List<ProduitDetailsDTO> - contenant les produits récupérés
     */
    @Override
    public List<ProduitDetailsDTO> getAllProducts(UUID idStore) {
        Optional<Store> optionalStore = storeRepository.findById(idStore);
        if (optionalStore.isPresent()) {
            TypeResponseApiGetProduct typeResponseApiGetProduct = optionalStore.get().getConfigStore().getTypeResponseApiGetProduit();
            if (typeResponseApiGetProduct.equals(TypeResponseApiGetProduct.Page_Unique)) {
                return getAllProduitsAPINonPaginee(idStore);
            }
            if (typeResponseApiGetProduct.equals(TypeResponseApiGetProduct.Paginable)) {
                return getAllProductsAPIPaginee(idStore);
            }
        }
        return null;
    }

    /**
     * Retourne le nombre total de stores.
     * @return {@link long}
     */
    @Override
    public long nbreStores() {
        return storeRepository.findAll().size();
    }


    /**
     * Filtre et retourne la liste des produits dont le nom contient la chaîne spécifiée et appartenant à un store spécifique.
     *
     * @param nom {@link String} la chaîne à rechercher dans les noms des produits.
     * @param idStore {@link UUID} l'id du store auquel appartiennent les produits.
     * @return {@link List<ProduitDetailsDTO>} - une liste contenant les informations des produits filtrés.
     */
    @Override
    public List<ProduitDetailsDTO> filterProduit(String nom, UUID idStore) {
        nom = nom.toLowerCase();

        List<ProduitDetailsDTO> produitDTOList = getAllProducts(idStore);
        List<ProduitDetailsDTO> produitFitred = new ArrayList<>();
        for(ProduitDetailsDTO produit:produitDTOList){
            String productName = produit.getNom().toLowerCase();
            if (productName.contains(nom)) {
                produitFitred.add(produit);
            }
        }
        return produitFitred;
    }
}



