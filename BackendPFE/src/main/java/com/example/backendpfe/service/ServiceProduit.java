package com.example.backendpfe.service;

import com.example.backendpfe.DTO.*;
import com.example.backendpfe.IService.IServiceApiExterne;
import com.example.backendpfe.IService.IServiceConfigStoreAttributs;
import com.example.backendpfe.IService.IServiceProduit;
import com.example.backendpfe.entities.*;
import com.example.backendpfe.entities.typeEnum.ConfigMethode;
import com.example.backendpfe.entities.typeEnum.TypeRemise;
import com.example.backendpfe.entities.typeEnum.TypeResponseApiGetProduct;
import com.example.backendpfe.exception.ProduitNotfoundException;
import com.example.backendpfe.mapper.ProduitMapper;
import com.example.backendpfe.mapper.StoreMapper;
import com.example.backendpfe.repository.ProduitRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceProduit implements IServiceProduit {
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private ProduitMapper produitMapper;
    @Autowired
    private IServiceApiExterne iServiceApiExterne;
    @Autowired
    private IServiceConfigStoreAttributs iServiceConfigStoreAttributs;
    @Autowired
    private StoreMapper storeMapper;

    /**
     * Créer un nouveau produit.
     * @param produitDTO {@link ProduitDTO}
     * @return {@link ProduitDTO}
     */
    @Override
    public ProduitDTO createProduit(ProduitDTO produitDTO) {
        // Convertir le DTO en entité
        Produit produitToCreate = produitMapper.toEntity(produitDTO);
        //Enregistrer le produit p dans le repository
        Produit p = produitRepository.save(produitToCreate);
        //Convertir en dto et revoyer le produit créé
        return produitMapper.toDTO(p);
    }

    /**
     * Trouve et retourne un produit basé sur son id.
     *
     * @param id {@link UUID}
     * @return {@link ProduitDTO}
     * @throws ProduitNotfoundException si le produit n'est pas trouvé.
     */
    @Override
    public ProduitDTO findProduitById(UUID id) {
        Optional<Produit> produitOptional = produitRepository.findById(id);
        if (produitOptional.isPresent()) {
            return produitMapper.toDTO(produitOptional.get());
        } else throw new ProduitNotfoundException();
    }

    /**
     * Cette méthode permet de modifier un produit.
     * @param produitDTO {@link ProduitDTO}
     * @return
     */
    @Override
    public ProduitDTO updateProduit(ProduitDTO produitDTO) {
        // Convertir le DTO en entité
        Produit produitToUpdate = produitMapper.toEntity(produitDTO);
        //Enregistrer le produit mise à jour dans le repository
        Produit produitUpdated = produitRepository.save(produitToUpdate);
        //Convertir en dto et revoyer le produit créé
        return produitMapper.toDTO(produitUpdated);
    }

    /**
     * Retourne la liste de tous les produits.
     *
     * @return {@link List<ProduitDTO>}
     */
    @Override
    public List<ProduitDTO> findAllProduits() {
        return produitMapper.toDTO(produitRepository.findAll());
    }

    /**
     * Cette méthode permet supprimer un produit
     * @param id {@link UUID}
     */
    @Override
    public void deleteProduit(UUID id) {
        produitRepository.deleteById(id);
    }

    /**
     * Vérifie si un produit existe.
     * @param id {@link UUID}
     * @return true si le produit existe, sinon false.
     */
    @Override
    public boolean produitExist(UUID id) {
        return produitRepository.existsById(id);
    }


    /**
     * Retourne le chemin de l'attribut "prix" pour un store donné.
     *
     * @param store {@link Store}
     * @return {@link String} le chemin de l'attribut "prix" sous forme de chaîne de caractères.
     */
    public String getCheminPrix(Store store) {
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        String cheminPrix = "";
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("price")) {
                cheminPrix = configStoreAttributs.getChemin();
                break;
            }
        }
        return cheminPrix;
    }
    /**
     * Retourne le chemin de l'attribut "image" pour un store donné.
     *
     * @param store {@link Store}
     * @return {@link String} le chemin de l'attribut "image" sous forme de chaîne de caractères.
     */
    public String getCheminImage(Store store) {
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        String cheminImage = "";
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("image")) {
                cheminImage = configStoreAttributs.getChemin();
                break;
            }
        }
        return cheminImage;
    }

    /**
     * Retourne le chemin de l'attribut "quantité" pour un store donné.
     *
     * @param store {@link Store}
     * @return {@link String} le chemin de l'attribut "quantité" sous forme de chaîne de caractères.
     */
    public String getCheminQuantite(Store store) {
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        String cheminQuantity = "";
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("quantity")) {
                cheminQuantity = configStoreAttributs.getChemin();
                break;
            }
        }
        return cheminQuantity;
    }

    /**
     * Retourne le chemin de l'attribut "catégorie" pour un store donné.
     *
     * @param store {@link Store}
     * @return {@link String} le chemin de l'attribut "catégorie" sous forme de chaîne de caractères.
     */
    public String getCheminCategorie(Store store) {
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
     * Récupère le prix du produit.
     *
     * @param configAttributsProduit {@link ConfigAttributsProduit} Les attributs de configuration pour récupérer le prix du produit.
     * @param store                  {@link Store} Store associé au produit.
     * @param node                   {@link JsonNode} Les données JSON représentant le produit.
     * @return double - Le prix du produit.
     */
    @Override
    public double getPrix(ConfigAttributsProduit configAttributsProduit, Store store, JsonNode node) {
        double prix = 0;
        double remise = 0;
        double prixFinal = 0;
        TypeRemise typeRemise = store.getConfigStore().getTypeRemise();
        if (!configAttributsProduit.getPrice().isEmpty()) {
            String productPrice = getCheminPrix(store);
            JsonNode productPriceNode = node;
            if (productPrice != null && !productPrice.isEmpty()) {
                productPriceNode = iServiceApiExterne.getNoeudSelonChemin(productPriceNode, productPrice);
                // Récupérer la valeur du nœud price
                if (productPriceNode != null && productPriceNode.has(configAttributsProduit.getPrice())) {
                    prix = productPriceNode.get(configAttributsProduit.getPrice()).asDouble();
                } else {
                    System.out.println("Propriété 'Prix' manquante pour ce produit.");
                }
                if (productPriceNode != null && productPriceNode.has(configAttributsProduit.getRemise()) && !(node.get(configAttributsProduit.getRemise()) == null) && !(node.get(configAttributsProduit.getRemise()).isEmpty())) {
                    remise = productPriceNode.get(configAttributsProduit.getRemise()).asDouble();
                } else {
                    System.out.println("Propriété 'remise' manquante pour ce produit.");
                }
            } else {
                if (node.has(configAttributsProduit.getPrice())) {
                    prix = node.get(configAttributsProduit.getPrice()).asDouble();
                }
                if (node.has(configAttributsProduit.getPrice()) && !(node.get(configAttributsProduit.getRemise()) == null) && !(node.get(configAttributsProduit.getRemise()).isEmpty())) {
                    remise = node.get(configAttributsProduit.getRemise()).asDouble();
                }
            }
        } else {
            System.out.println("Propriété 'prix' manquante pour ce produit.");
        }
        if (typeRemise.equals(TypeRemise.Pas_de_remise)) {
            prixFinal = prix;
        }
        if (typeRemise.equals(TypeRemise.Remise_fixe)) {
            prixFinal = prix - remise;
        }
        if (typeRemise.equals(TypeRemise.Remise_par_pourcentage)) {
            prixFinal = (prix / 100) * (100 - remise);
        }
        return prixFinal;
    }

    /**
     * Récupère le nom de la catégorie du produit.
     *
     * @param attributCategorie {@link String} Les attributs de configuration pour récupérer le nom de la catégorie du produit.
     * @param store             {@link Store} Store associée au produit.
     * @param node              {@link JsonNode} Les données JSON représentant le produit.
     * @return String - Le nom de la catégorie du produit.
     */
    @Override
    public String getNomCategorie(String attributCategorie, Store store, JsonNode node) {
        String category = "";
        if (!attributCategorie.isEmpty()) {
            if (node != null) {
                String cheminCategorie = getCheminCategorie(store);
                JsonNode productCategorieNode = node;
                if (cheminCategorie != null && !cheminCategorie.isEmpty()) {
                    productCategorieNode = iServiceApiExterne.getNoeudSelonChemin(productCategorieNode, cheminCategorie);
                    // Récupérer la valeur du nœud image
                    if (productCategorieNode != null) {
                        if (productCategorieNode.isArray() && !productCategorieNode.isEmpty()) {
                            JsonNode dernierElement = null;
                            for (JsonNode element : productCategorieNode) {
                                dernierElement = element;
                            }
                            if (dernierElement != null && !dernierElement.isEmpty()) {
                                JsonNode attributNode = dernierElement.get(attributCategorie);
                                if (attributNode != null && !attributNode.isNull()) {
                                    category = attributNode.asText();
                                }
                            }
                        } else {
                            JsonNode attributNode = productCategorieNode.get(attributCategorie);
                            if (attributNode != null && !attributNode.isNull()) {
                                category = attributNode.asText();
                            }
                        }
                    }
                } else {
                    if (node.get(attributCategorie).isArray() && !node.get(attributCategorie).isEmpty()) {
                        JsonNode dernierElement = null;
                        for (JsonNode element : node) {
                            dernierElement = element;
                        }
                        if (dernierElement != null && !dernierElement.isEmpty()) {
                            category = dernierElement.get(attributCategorie).asText();
                        }
                    } else {
                        JsonNode attributNode = node.get(attributCategorie);
                        if (attributNode != null && !attributNode.isNull()) {
                            category = attributNode.asText();
                        }
                    }
                }
            } else {
                System.out.println("Le nœud JSON est null.");
            }
        } else {
            System.out.println("Propriété 'categorie' manquante pour ce produit.");
        }
        return category;
    }

    /**
     * Récupère l'url de l'image du produit.
     *
     * @param configAttributsProduit {@link ConfigAttributsProduit} Les attributs de configuration pour récupérer l'url de l'image de la catégorie du produit.
     * @param store                  {@link Store} Store associé au produit.
     * @param node                   {@link JsonNode} Les données JSON représentant le produit.
     * @return String - Le nom de la catégorie du produit.
     */
    @Override
    public String getUrlImage(ConfigAttributsProduit configAttributsProduit, Store store, JsonNode node) {
        String photo = "";
        if (!configAttributsProduit.getImage().isEmpty()) {
            String productImage = getCheminImage(store);
            JsonNode productImageNode = node;
            if (productImage != null && !productImage.isEmpty()) {
                productImageNode = iServiceApiExterne.getNoeudSelonChemin(productImageNode, productImage);
                // Récupérer la valeur du nœud image
                if (productImageNode != null && !productImageNode.isEmpty()) {
                    if (productImageNode.isArray()) {
                        for (JsonNode ImageNode : productImageNode) {
                            photo = ImageNode.get(configAttributsProduit.getImage()).asText();
                            photo = photo.replaceAll("[\\[\\]\"]", "");
                            break;
                        }
                    } else {
                        photo = productImageNode.get(configAttributsProduit.getImage()).asText();
                    }
                }
            } else {
                if (node.get(configAttributsProduit.getImage()).isArray()) {
                    photo = node.get(configAttributsProduit.getImage()).get(0).asText();
                    photo = photo.replaceAll("[\\[\\]\"]", "");
                } else {
                    photo = node.get(configAttributsProduit.getImage()).asText();
                }
            }
            if (!photo.isEmpty() && !photo.equals("null")) {
                if (!photo.startsWith("http")) {
                    String urlUploadPhoto = store.getConfigStore().getUrlPhoto();
                    if (urlUploadPhoto != null && !urlUploadPhoto.isEmpty()) {
                        String idPhoto = photo;
                        photo = urlUploadPhoto + idPhoto;
                    } else {
                        photo = "../../../../assets/noPhoto.png";
                    }

                }
            }
            if (photo.isEmpty() || photo.equals("null")) {
                photo = "../../../../assets/noPhoto.png";
            }
        } else {
            System.out.println("Propriété 'image' manquante pour ce produit.");
        }
        return photo;
    }


    /**
     * Récupère la quantité d'un produit en utilisant une API externe non paginée.
     *
     * @param referenceId {@link String} Référence du produit dans le store externe.
     * @param store {@link Store}
     * @param idProduitAttribuut {@link String}
     * @param quantityAttribut {@link String}
     * @param url {@link String}
     * @return int - La quantité du produit.
     */
    @Override
    public int getQuantityByProductApiNonPaginee(String referenceId, Store store, String idProduitAttribuut, String quantityAttribut, String url) {
        if (url != null && !url.isEmpty()) {
            ResponseEntity<String> result = null;
            boolean typeAutorisation = store.getConfigStore().isTypeAutorisationApiGetProduit();
            if(!typeAutorisation){
                result = iServiceApiExterne.apiExterneMethodeGet(url);
            }
            if(typeAutorisation){
                String token = iServiceApiExterne.getToken(storeMapper.toDTO(store));
                result = iServiceApiExterne.apiExterneMethodeGetAvecAutorisation(token,url);
            }
            JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(result);
            if (jsonNode != null && !jsonNode.isEmpty()) {
                String productList = store.getConfigStore().getCheminListProduit();
                jsonNode = iServiceApiExterne.getNoeudSelonChemin(jsonNode, productList);
                JsonNode product = rechercheProduitSiteExterne(referenceId, jsonNode, idProduitAttribuut);
                if(product != null){
                    return this.calculStock(product, quantityAttribut, store);
                }
            }
        }
        return -1;
    }

    /**
     * Récupère la quantité d'un produit en utilisant une API externe paginée.
     *
     * @param referenceId {@link String} Référence du produit dans le store externe.
     * @param store {@link Store}
     * @param idProduitAttribuut {@link String}
     * @param quantityAttribut {@link String}
     * @return int - La quantité du produit.
     */
    @Override
    public int getQuantityByProductApiPaginee(String referenceId,String url, Store store, String idProduitAttribuut, String quantityAttribut) {
        ConfigAttributsApiPaginee configAttributsApiPaginee = iServiceConfigStoreAttributs.getConfigAttributsApiPaginee(store);
        ResponseEntity<String> response = iServiceApiExterne.responseApiPagineeGet(store, configAttributsApiPaginee.getPageNumber(), configAttributsApiPaginee.getPageSize(),configAttributsApiPaginee.getTotalElements(),url);
        JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(response);
        if (jsonNode != null && !jsonNode.isEmpty()) {
            String cheminListProduits = store.getConfigStore().getCheminListProduit();
            JsonNode NodeProduits = iServiceApiExterne.getNoeudSelonChemin(jsonNode, cheminListProduits);
            JsonNode product = rechercheProduitSiteExterne(referenceId, NodeProduits, idProduitAttribuut);
            if(product != null){
                return this.calculStock(product, quantityAttribut, store);
            }
        }
        return -1;
    }

    /**
     * Récupère la quantité d'un produit en appelant la méthode appropriée en fonction du type d'API utilisé (soit non paginée, soit paginée).
     *
     * @param referenceId l'identifiant de référence du produit
     * @param storeId l'UUID du store
     * @return la quantité du produit ; si le produit ou la configuration du magasin n'est pas trouvé, retourne 0
     */
    @Override
    public int getQuantityByProduct(String referenceId, UUID storeId) {
        Produit produit = findProduitByReferenceIdAndStoreId(referenceId, storeId);
        if (produit != null) {
            Store store = produit.getStore();
            if (store.getConfigStore() != null) {
                String url = store.getConfigStore().getUrl();
                ConfigAttributsQuantiteProduit configAttributsQuantiteProduit = iServiceConfigStoreAttributs.configStoreAttributsQuantiteProduit(store);
                TypeResponseApiGetProduct typeResponseApiGetProduct = store.getConfigStore().getTypeResponseApiGetProduit();
                if (typeResponseApiGetProduct.equals(TypeResponseApiGetProduct.Page_Unique)) {
                    return getQuantityByProductApiNonPaginee(referenceId,store,configAttributsQuantiteProduit.getIdProduit(),configAttributsQuantiteProduit.getQuantity(),url);
                }
                else if (typeResponseApiGetProduct.equals(TypeResponseApiGetProduct.Paginable)){
                    return getQuantityByProductApiPaginee(referenceId,url,store,configAttributsQuantiteProduit.getIdProduit(),configAttributsQuantiteProduit.getQuantity());
                }
            }
        }
        return -1;
    }

    /**
     * Recherche un produit dans les données JSON du site externe.
     *
     * @param referenceId       {@link String} référence du produit qui correspond à l'id de ce produit dans le site externe.
     * @param jsonNode          {@link JsonNode} Le nœud JSON contenant les données des produits.
     * @param attributProductId {@link String} Le nom de l'attribut contenant l'identifiant du produit.
     * @return JsonNode - Le nœud JSON du produit trouvé, ou null s'il n'est pas trouvé.
     */
    public JsonNode rechercheProduitSiteExterne(String referenceId, JsonNode jsonNode, String attributProductId) {
        for (JsonNode product : jsonNode) {
            if (product != null) {
                String productId = product.get(attributProductId).toString();
                if (productId.equals(referenceId)) {
                    System.out.println(product);
                    return product;
                }
            }
        }
        return null;
    }

    /**
     * Calcule le stock d'un produit en fonction des données JSON récupérées depuis le site externe.
     *
     * @param node             {@link JsonNode} Les données JSON contenant le produit.
     * @param attributQuantity {@link String} L'attribut de configuration pour récupérer la quantité du produit.
     * @param store            {@link Store} Store associé au produit.
     * @return int - Le stock total du produit.
     */
    @Override
    public int calculStock(JsonNode node, String attributQuantity, Store store) {
        int stock = 0;
        if (!attributQuantity.isEmpty()) {
            JsonNode productQuantityNode = node;
            String items = getCheminQuantite(store);
            if (items != null && !items.isEmpty()) {
                productQuantityNode = iServiceApiExterne.getNoeudSelonChemin(productQuantityNode, items);
                if (productQuantityNode != null) {
                    if (productQuantityNode.isArray()) {
                        for (JsonNode item : productQuantityNode) {
                            stock += item.get(attributQuantity).asInt();
                        }
                    } else {
                        stock += productQuantityNode.get(attributQuantity).asInt();
                    }
                }
            } else {
                stock += node.get(attributQuantity).asInt();
            }
        } else {
            System.out.println("Propriété 'quantity' manquante pour ce produit.");
        }
        return stock;
    }


    /**
     * Récupère la configuration des attributs de produit pour un store donné
     *
     * @param storeId {@link UUID} L'identifiant du store
     * @return ConfigAttributsProduit contenant - la configuration des attributs de produit pour le magasin spécifié.
     */
    @Override
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
     * Recherche et retourne le produit avec son id dans un store donné.
     *
     * @param referenceId {@link String}
     * @param storeId {@link UUID}
     * @return {@link Produit} le produit trouvé correspondant aux critères spécifiés, ou null s'il n'est pas trouvé.
     */
    @Override
    public Produit findProduitByReferenceIdAndStoreId(String referenceId, UUID storeId) {
        List<Produit> ListProduits = produitRepository.findProduitByReferenceId(referenceId);
        Produit produit = null;
        for (Produit p : ListProduits) {
            if (p.getStore().getId().equals(storeId)) {
                produit = p;
                break;
            }
        }
        return produit;
    }

    /**
     * Trouve et retourne la liste des produits appartenant à un store spécifique.
     *
     * @param idStore
     * @return {@link List<Produit>}
     */
    @Override
    public List<Produit> findProduitsByStore(UUID idStore) {
        return produitRepository.findByStoreId(idStore);
    }

    /**
     * Récupère les attributs de configuration nécessaires pour la récupération du nom d'un produit.
     * @param store {@link Store}
     * @param attributNom {@link String}
     * @param attributId {@link String}
     */
    void attributsConfigRecupereNom(Store store,String attributNom,String attributId){
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();

        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("name")) {
                attributNom = configStoreAttributs.getAttributeKey();
            }
            if (configStoreAttributs.getAttributeKey().equals("id")) {
                attributId = configStoreAttributs.getAttributeKey();
            }
        }
    }

    /**
     * Cette méthode récupère le nom d'un produit en fonction de son référenceId et de l'ID du store.
     * Elle commence par trouver le produit à l'aide de referenceId et de l'ID du store, puis récupère le nom du produit à partir d'une API externe en fonction de la configuration du store.
     *
     * @param referenceId {@link String}
     * @param storeId {@link UUID}
     * @return String
     */
    @Override
    public String getNomParProduit(String referenceId, UUID storeId) {
        String nom = null;
        Produit produit = findProduitByReferenceIdAndStoreId(referenceId, storeId);
        if (produit != null) {
            String attributNom = "";
            String attributId = "";
            Store store = produit.getStore();
            attributsConfigRecupereNom(store,attributNom,attributId);
            String url = store.getConfigStore().getUrl();
            if (url != null) {
                ResponseEntity<String> result = iServiceApiExterne.apiExterneMethodeGet(url);
                JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(result);
                if (jsonNode != null && !jsonNode.isEmpty()) {
                    String cheminListProduit = store.getConfigStore().getCheminListProduit();
                    jsonNode = iServiceApiExterne.getNoeudSelonChemin(jsonNode, cheminListProduit);
                    JsonNode product = rechercheProduitSiteExterne(referenceId, jsonNode, attributId);
                    if (product != null && !product.isEmpty()) {
                        nom = product.get(attributNom).asText();
                    }
                }
            }
        }
        return nom;
    }

    /**
     * Récupère un produit par son referenceId et l'id du store que l'appartient.
     * @param referenceId {@link String}
     * @param storeId {@link UUID}
     * @return ProduitDTO
     */
    @Override
    public ProduitDTO getProduitDtoByRefId(String referenceId, UUID storeId) {
        Produit produit = findProduitByReferenceIdAndStoreId(referenceId, storeId);
        ProduitDTO produitDTO = produitMapper.toDTO(produit);

        if (produit != null) {
            ConfigAttributsProduit configAttributsProduit = getConfigStoreAttributsProduit(storeId);
            Store store = produit.getStore();
            if (store.getConfigStore() != null) {
                String url = store.getConfigStore().getUrl();
                if (url != null) {
                    ResponseEntity<String> result = iServiceApiExterne.apiExterneMethodeGet(url);
                    JsonNode jsonNode = iServiceApiExterne.verifieResponseAndGetJsonNode(result);
                    if (jsonNode != null && !jsonNode.isEmpty()) {
                        String productList = store.getConfigStore().getCheminListProduit();
                        jsonNode = iServiceApiExterne.getNoeudSelonChemin(jsonNode, productList);
                        JsonNode product = rechercheProduitSiteExterne(referenceId, jsonNode, configAttributsProduit.getId());
                        if (product != null && !product.isEmpty()) {
                            produitDTO.setNom(product.get(configAttributsProduit.getName()).asText());
                            produitDTO.setPhoto(getUrlImage(configAttributsProduit, store, product));
                        }
                    }
                }
            }
        }
        return produitDTO;
    }

    /**
     * Retourne le nombre total des produits.
     * @return long - le nombre total
     */
    @Override
    public long nbreProduits() {
        return produitRepository.findAll().size();
    }


}
