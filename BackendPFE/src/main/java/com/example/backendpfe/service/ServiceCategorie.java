package com.example.backendpfe.service;

import com.example.backendpfe.DTO.*;
import com.example.backendpfe.IService.*;
import com.example.backendpfe.entities.*;
import com.example.backendpfe.entities.typeEnum.ConfigMethode;
import com.example.backendpfe.entities.typeEnum.TypeResponseApiGetProduct;
import com.example.backendpfe.exception.CategorieNotfoundException;
import com.example.backendpfe.mapper.Categorie2Mapper;
import com.example.backendpfe.mapper.CategorieMapper;
import com.example.backendpfe.mapper.StoreMapper;
import com.example.backendpfe.repository.CategorieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceCategorie implements IServiceCategorie {
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private CategorieMapper categorieMapper;
    @Autowired
    private IServiceStore iServiceStore;
    @Autowired
    private Categorie2Mapper categorie2Mapper;
    @Autowired
    private IServiceApiExterne iServiceApiExterne;
    @Autowired
    private IServiceConfigStoreAttributs iServiceConfigStoreAttributs;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private IServiceProduit iServiceProduit;

    /**
     * Créer une nouvelle catégorie
     *
     * @param categorieDTO {@link CategorieDTO}
     * @return {@link CategorieDTO}
     */
    @Override
    public CategorieDTO createCategorie(CategorieDTO categorieDTO) {
        Categorie categorieToCreate = categorieMapper.toEntity(categorieDTO);
        Categorie categorieCreated = categorieRepository.save(categorieToCreate);
        return categorieMapper.toDto(categorieCreated);
    }

    /**
     * Modifier une catégorie
     *
     * @param categorieDTO {@link CategorieDTO}
     * @return {@link CategorieDTO}
     */
    @Override
    public CategorieDTO updateCategorie(CategorieDTO categorieDTO) {
        Categorie categorieToUpdate = categorieMapper.toEntity(categorieDTO);
        Categorie categorieUpdated = categorieRepository.save(categorieToUpdate);
        return categorieMapper.toDto(categorieUpdated);
    }

    /**
     * Trouve et retourne une categorie basé sur son id.
     *
     * @param id {@link UUID}
     * @return {@link CategorieDTO}
     * @throws CategorieNotfoundException si la catégorie n'est pas trouvé.
     */
    @Override
    public CategorieDTO findCategorieById(UUID id) {
        Optional<Categorie> categorieOptional = categorieRepository.findById(id);
        if (categorieOptional.isPresent()) {
            return categorieMapper.toDto(categorieOptional.get());
        } else throw new CategorieNotfoundException();

    }

    /**
     * Retourne la liste de toutes les catégories.
     *
     * @return {@link List<CategorieDTO>}
     */
    @Override
    public List<CategorieDTO> findAllCategories() {
        return categorieMapper.toDto(categorieRepository.findAll());
    }

    /**
     * Supprimer une catégorie.
     *
     * @param id {@link UUID}
     */
    @Override
    public void deleteCategorie(UUID id) {
        categorieRepository.deleteById(id);
    }

    /**
     * Vérifie si une catégorie existe.
     *
     * @param id {@link UUID}
     * @return true si la catégorie existe, sinon false.
     */
    @Override
    public boolean CategorieExist(UUID id) {
        return categorieRepository.existsById(id);
    }

    /**
     * Récupérer la configuration de l'attribut categorie
     *
     * @param store {@link StoreDTO}
     * @return String
     */
    public String configAttributCategorie(StoreDTO store) {
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        String categorie = "";
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("categorie")) {
                categorie = configStoreAttributs.getAttributeValue();
                break;
            }
        }
        return categorie;
    }

    /**
     * Récupère le chemin de l'attribut "catégorie" à partir de la configuration du store spécifié.
     *
     * @param store {@link StoreDTO}.
     * @return Le chemin de l'attribut "catégorie" dans la configuration du store, ou une chaîne vide si non trouvé.
     */
    public String getCheminCategorie(StoreDTO store) {
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        String cheminCategorie = "";
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("categorie")) {
                cheminCategorie = configStoreAttributs.getChemin();
                break;
            }
        }
        return cheminCategorie;
    }

    /**
     * Créer toutes les catégories à partir d'un JsonNode.
     *
     * @param jsonNode {@link JsonNode} Le nœud JSON contenant les données des catégories.
     * @param store    {@link StoreDTO} Le Store pour lequel les catégories sont créées.
     */
    public List<Categorie> getAllCategories(JsonNode jsonNode, StoreDTO store) {
        List<Categorie> categorieList = new ArrayList<>();
        String categorie = configAttributCategorie(store);
        String cheminCategorie = getCheminCategorie(store);
        for (JsonNode node : jsonNode) {
            JsonNode nodeCategory = node;
            nodeCategory = iServiceApiExterne.getNoeudSelonChemin(nodeCategory, cheminCategorie);
            String category = "";
            if (nodeCategory.isArray()) {
                for (JsonNode item : nodeCategory) {
                    category = item.get(categorie).asText();
                }
                verifAndGetCategorie(categorieList, category, store.getId());
            } else if (!nodeCategory.isArray() && nodeCategory.has(categorie)) {
                category = nodeCategory.get(categorie).asText();
                verifAndGetCategorie(categorieList, category, store.getId());
            }
        }
        return categorieList;
    }

    /**
     * Récupère la configuration des attributs de produit pour un store donné
     *
     * @param storeId {@link UUID} L'identifiant du store
     * @return ConfigAttributsProduit contenant - la configuration des attributs de produit pour le magasin spécifié.
     */
    public ConfigAttributsProduit getConfigStoreAttributsProduit(UUID storeId) {
        List<ConfigStoreAttributsDTO> configStoreAttributsDTOList = iServiceConfigStoreAttributs.findConfigStoreAttributsByConfigMethode(storeId, ConfigMethode.GET);
        ConfigAttributsProduit configAttributsProduit = new ConfigAttributsProduit();
        for (ConfigStoreAttributsDTO configStoreAttributsDTO : configStoreAttributsDTOList) {
            if (Objects.nonNull(configStoreAttributsDTO)) {
                configAttributsProduit.setAttributeValueByKey(configStoreAttributsDTO.getAttributeKey(), configStoreAttributsDTO.getAttributeValue());
            }
        }
        return configAttributsProduit;
    }

    /**
     * Vérifie l'existence d'un produit et crée le produit s'il n'existe pas déjà.
     *
     * @param node le nœud JSON contenant les informations du produit.
     * @param produit l'objet à créer.
     * @param attributId l'équivalent de l'attribut id du produit dans le site externe.
     * @param storeId id du store.
     */
    public void verifExistanceEtCreerProduit(JsonNode node, ProduitDTO produit, String attributId, UUID storeId) {
        if(!node.isNull() && node.has(attributId) ){
            Produit produitExist = iServiceProduit.findProduitByReferenceIdAndStoreId(node.get(attributId).asText(), storeId);
            if (produitExist == null) {
                iServiceProduit.createProduit(produit);
            } else {
                UUID produitId = produitExist.getId();
                produit.setId(produitId);
            }
        }
    }

    /**
     * Récupère et retourne les informations d'un produit à partir d'un nœud JSON.
     *
     * @param node le nœud JSON contenant les informations du produit.
     * @param configAttributsProduit la configuration des attributs du produit.
     * @param store le store auquel le produit appartient.
     * @return un objet ProduitDTO contenant les informations du produit.
     */
    ProduitDTO recupereInfoProduit(JsonNode node, ConfigAttributsProduit configAttributsProduit, Store store) {
        ProduitDTO produit = new ProduitDTO();
        produit.setReferenceId(node.get(configAttributsProduit.getId()).asText());
        produit.setNom(node.get(configAttributsProduit.getName()).asText());
        produit.setStore(store);
        verifExistanceEtCreerProduit(node, produit, node.get(configAttributsProduit.getId()).asText(), store.getId());

        produit.setQuantite(iServiceProduit.calculStock(node, configAttributsProduit.getQuantity(), store));
        produit.setPrix(iServiceProduit.getPrix(configAttributsProduit, store, node));
        produit.setPhoto(iServiceProduit.getUrlImage(configAttributsProduit, store, node));
        return produit;
    }

    /**
     * Parcourt les nœuds JSON et retourne une liste de produits par catégorie.
     *
     * @param jsonNode le nœud JSON à parcourir.
     * @param cheminCategorie le chemin de la catégorie dans le nœud JSON.
     * @param categorieAttribut l'attribut de la catégorie à comparer.
     * @param categorie la catégorie à rechercher.
     * @param configAttributsProduit la configuration des attributs du produit.
     * @param store le store auquel les produits appartiennent.
     * @return une liste d'objets ProduitDTO correspondant à la catégorie spécifiée.
     */
    public List<ProduitDTO> parcourirNoeudsEtRecupererListProduitsParCategorie(JsonNode jsonNode,String cheminCategorie,String categorieAttribut,String categorie,ConfigAttributsProduit configAttributsProduit,Store store){
        List<ProduitDTO> produitDTOList = new ArrayList<>();
        for (JsonNode node : jsonNode) {
            JsonNode nodeCategory = node;
            nodeCategory = iServiceApiExterne.getNoeudSelonChemin(nodeCategory, cheminCategorie);
            if (nodeCategory.isArray()) {
                String category = "";
                for (JsonNode item : nodeCategory) {
                    category = item.get(categorieAttribut).asText();
                }
                if (categorie.equals(category)) {
                    ProduitDTO produit = recupereInfoProduit(node, configAttributsProduit, store);
                    produitDTOList.add(produit);
                }
            } else if (!nodeCategory.isArray() && nodeCategory.has(categorieAttribut)) {
                String category = nodeCategory.get(categorieAttribut).asText();
                if (categorie.equals(category)) {
                    ProduitDTO produit = recupereInfoProduit(node, configAttributsProduit, store);
                    produitDTOList.add(produit);
                }
            }
        }
        return produitDTOList;
    }

    /**
     * Retourne une liste de produits appartenant à une catégorie spécifique pour un store donné.
     *
     * @param categorie {@link String}
     * @param storeId   {@link UUID}
     * @return List<ProduitDTO>
     */
    @Override
    public List<ProduitDTO> listProduitsParCategorie(String categorie, UUID storeId) {
        StoreDTO storeDTO = iServiceStore.findStoreById(storeId);
        Store store = storeMapper.toEntity(storeDTO);
        String url = store.getConfigStore().getUrl();
        ResponseEntity<String> result = iServiceApiExterne.apiExterneMethodeGet(url);
        JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(result);
        String cheminListProduit = store.getConfigStore().getCheminListProduit();
        jsonNode = iServiceApiExterne.getNoeudSelonChemin(jsonNode, cheminListProduit);
        String categorieAttribut = configAttributCategorie(storeDTO);
        String cheminCategorie = getCheminCategorie(storeDTO);
        ConfigAttributsProduit configAttributsProduit = getConfigStoreAttributsProduit(store.getId());
        return parcourirNoeudsEtRecupererListProduitsParCategorie(jsonNode,cheminCategorie,categorieAttribut,categorie,configAttributsProduit,store);
    }

    /**
     * Récupère les catégories depuis une API non paginée pour un store spécifié.
     *
     * @param storeId {@link UUID} L'identifiant du Store
     * @return List<Categorie2DTO> - Une liste de DTO représentant les catégories du Store
     */
    @Override
    public List<Categorie2DTO> getCategoriesByStoreApiNonPaginee(UUID storeId) {
        StoreDTO store = iServiceStore.findStoreById(storeId);
        String url = store.getConfigStore().getUrl();
        ResponseEntity<String> result = null;
        boolean typeAutorisation = store.getConfigStore().isTypeAutorisationApiGetProduit();
        if (!typeAutorisation) {
            result = iServiceApiExterne.apiExterneMethodeGet(url);
        }
        if (typeAutorisation) {
            String token = iServiceApiExterne.getToken(store);
            result = iServiceApiExterne.apiExterneMethodeGetAvecAutorisation(token, url);
        }
        JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(result);
        String productList = store.getConfigStore().getCheminListProduit();
        jsonNode = iServiceApiExterne.getNoeudSelonChemin(jsonNode, productList);
        List<Categorie> categorieList = getAllCategories(jsonNode, store);
        return categorie2Mapper.toDto(categorieList);
    }


    /**
     * Récupère les catégories depuis une API paginée pour un store spécifié.
     *
     * @param storeId {@link UUID} L'identifiant du Store
     * @return List<Categorie2DTO> - Une liste de DTO représentant les catégories du Store
     */
    @Override
    public List<Categorie2DTO> getCategoriesByStoreApiPaginee(UUID storeId) {
        StoreDTO store = iServiceStore.findStoreById(storeId);
        List<ProduitDetailsDTO> produitDTOList = iServiceStore.getAllProductsAPIPaginee(storeId);
        List<Categorie> categorieList = new ArrayList<>();
        String category = "";
        for (ProduitDetailsDTO produitDetailsDTO : produitDTOList) {
            category = produitDetailsDTO.getCategorie().getNom();
            verifAndGetCategorie(categorieList, category, store.getId());
        }
        return categorie2Mapper.toDto(categorieList);
    }

    /**
     * Récupère les catégories d'un store spécifié en fonction du type de réponse de l'API configurée.
     * Cette méthode détermine le type de réponse attendu (page unique ou paginable) depuis la configuration du store,
     * puis appelle la méthode appropriée pour récupérer les catégories en conséquence.
     *
     * @param storeId L'identifiant UUID du store.
     * @return Une liste de Categorie2DTO contenant les catégories récupérées depuis l'API.
     */
    @Override
    public List<Categorie2DTO> getCategoriesByStore(UUID storeId) {

        StoreDTO store = iServiceStore.findStoreById(storeId);
        if (store != null) {
            TypeResponseApiGetProduct typeResponseApiGetProduct = store.getConfigStore().getTypeResponseApiGetProduit();
            if (typeResponseApiGetProduct == TypeResponseApiGetProduct.Page_Unique) {
                return getCategoriesByStoreApiNonPaginee(storeId);
            }
            if (typeResponseApiGetProduct == TypeResponseApiGetProduct.Paginable) {
                return getCategoriesByStoreApiPaginee(storeId);
            }
        }
        return null;
    }


    /**
     * Vérifie et ajoute une catégorie à la liste des catégories si elle n'existe pas déjà.
     *
     * @param category {@link String} Le nom de la catégorie à vérifier et créer
     * @param storeId  {@link UUID} L'identifiant du Store auquel appartient la catégorie
     */
    public List<Categorie> verifAndGetCategorie(List<Categorie> listCategories, String category, UUID storeId) {
        if (!category.equals("null") && !category.isEmpty() && !existInListCategories(listCategories, category)) {
            Categorie c = null;
            c = new Categorie();
            c.setNom(category);
            c.setStoreId(storeId);
            listCategories.add(c);
        }
        return listCategories;
    }

    /**
     * Vérifier si une catégorie spécifiée existe déjà dans une liste donnée de catégories.
     *
     * @param listCategories {@link List<Categorie>} La liste de catégories dans laquelle rechercher.
     * @param category       {@link String} Le nom de la catégorie à rechercher.
     * @return boolean - true si la catégorie existe dans la liste, sinon false.
     */
    private boolean existInListCategories(List<Categorie> listCategories, String category) {
        for (Categorie c : listCategories) {
            if (c.getNom().equals(category)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Supprimer toutes les catégories d'un Store spécifié par son identifiant
     *
     * @param id {@link UUID} l'identifient du Store
     */
    @Override
    public void deleteAllCategoriesByStore(UUID id) {
        List<Categorie> categoriesToDelete = categorieRepository.findCategoriesByStoreId(id);
        for (Categorie c : categoriesToDelete) {
            this.deleteCategorie(c.getId());
        }
    }

    /**
     * Rechercher une catégorie par son nom.
     *
     * @param nom {@link String} Le nom de la catégorie à rechercher.
     * @return Categorie - La catégorie correspondant au nom spécifié, ou null si aucune catégorie correspondante n'est trouvée.
     */
    @Override
    public Categorie findCategorieByNom(String nom) {
        return null;
    }

    /**
     * Rechercher une catégorie par l'identifiant du Store et son nom.
     *
     * @param storeId      {@link UUID} L'identifiant du Store
     * @param categoryName {@link String} Le nom de la catégorie à rechercher
     * @return Categorie - La catégorie correspondant à l'identifiant du Store et au nom spécifiés, ou null si aucune catégorie correspondante n'est trouvée.
     */
    @Override
    public Categorie findCategorieByStoreIdAndNom(UUID storeId, String categoryName) {
        return categorieRepository.findCategoriesByStoreIdAndNom(storeId, categoryName);
    }

}
