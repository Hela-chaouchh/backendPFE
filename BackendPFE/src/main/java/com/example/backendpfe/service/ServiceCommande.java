package com.example.backendpfe.service;

import com.example.backendpfe.DTO.*;
import com.example.backendpfe.IService.*;
import com.example.backendpfe.entities.*;
import com.example.backendpfe.entities.typeEnum.ConfigMethode;
import com.example.backendpfe.exception.CommandeNotfoundException;
import com.example.backendpfe.feign.User;
import com.example.backendpfe.feign.ServiceFeignUser;
import com.example.backendpfe.mapper.CommandeMapper;
import com.example.backendpfe.mapper.DetailsCommandeMapper;
import com.example.backendpfe.repository.CommandeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceCommande implements IServiceCommande {
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private ServiceDetailsCommande serviceDetailsCommande;
    @Autowired
    private CommandeMapper commandeMapper;
    @Autowired
    private DetailsCommandeMapper detailsCommandeMapper;
    @Autowired
    private ServiceFeignUser serviceClient;
    @Autowired
    private IServiceStore serviceStore;
    @Autowired
    private IServiceProduit serviceProduit;
    @Autowired
    private IServiceConfigStoreAttributs iServiceConfigStoreAttributs;
    @Autowired
    private IServiceApiExterne iServiceApiExterne;

    /**
     * créer une nouvelle commande.
     *
     * @param commandeDTO {@link CommandeDTO}
     * @param idUser      {@link UUID}
     * @return CommandeDTO
     */
    @Override
    public CommandeDTO create(CommandeDTO commandeDTO, UUID idUser) {
        //Convertir le DTO en entity
        Commande commandeToCreate = commandeMapper.toEntity(commandeDTO);
        commandeToCreate.setClient_id(idUser);
        double montantCommande = calculTotalMontantCommande(commandeDTO);
        commandeToCreate.setMontant(montantCommande);

        // Obtenir la date système
        LocalDateTime dateSystem = LocalDateTime.now();
        // Formatter la date selon le format souhaité
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // Convertir la date en chaîne de caractères
        String date = dateSystem.format(formatter);
        commandeToCreate.setDateCommande(date);
        //enregistrer le nouveau commande dans le repository
        Commande commande = commandeRepository.save(commandeToCreate);
        for (DetailsCommande detailsCommande : commande.getDetailsCommandes()) {
            detailsCommande.setCommande(commande);
            serviceDetailsCommande.createDetailsCommande(detailsCommandeMapper.toDTO(detailsCommande));
        }
        return commandeMapper.toDto(commande);
    }


    /**
     * Mettre à jour une commande
     *
     * @param commandeDTO {@link CommandeDTO}
     * @return CommandeDTO
     */
    @Override
    public CommandeDTO updateCommande(CommandeDTO commandeDTO) {
        //Convertir le DTO en entity
        Commande commandeToUpdate = commandeMapper.toEntity(commandeDTO);
        //enregistrer le commande mis à jour dans le repository
        Commande commandeUpdated = commandeRepository.save(commandeToUpdate);
        for (DetailsCommande detailsCommande : commandeUpdated.getDetailsCommandes()) {
            detailsCommande.setCommande(commandeUpdated);
            serviceDetailsCommande.updateDetailsCommande(detailsCommandeMapper.toDTO(detailsCommande));
        }
        return commandeMapper.toDto(commandeUpdated);
    }

    /**
     * Trouver la commande par son id
     *
     * @param id {@link UUID}
     * @return CommandeDTO
     */
    @Override
    public CommandeDTO findCommandeById(UUID id) {
        Optional<Commande> commandeOptional = commandeRepository.findById(id);
        if (commandeOptional.isPresent()) {
            return commandeMapper.toDto(commandeOptional.get());
        } else throw new CommandeNotfoundException();
    }

    /**
     * Retourner la liste de toutes les commandes.
     *
     * @return List<CommandeDTO>
     */
    @Override
    public List<CommandeDTO> findAllCommandes() {
        return commandeRepository.findAll().stream()
                .sorted(Comparator.comparing(Commande::getDateCommande).reversed())
                .map(commandeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Supprimer une commande
     *
     * @param id {@link UUID}
     */
    @Override
    public void deleteCommande(UUID id) {
        commandeRepository.deleteById(id);
    }

    /**
     * Permet de vérifier si une commande existe ou non.
     *
     * @param id {@link UUID}
     * @return boolean
     */
    @Override
    public boolean commandeExist(UUID id) {
        return commandeRepository.existsById(id);
    }

    /**
     * Trouver la liste des commandes d'un client spécifique
     *
     * @param clientId {@link UUID}
     * @return List<CommandeDTO>
     */
    @Override
    public List<CommandeDTO> findCommandeByClientId(UUID clientId) {
        List<CommandeDTO> commandes = this.findAllCommandes();
        List<CommandeDTO> commandeClient = new ArrayList<>();
        for (CommandeDTO cmd : commandes) {
            if (cmd.getClient_id().equals(clientId)) {
                commandeClient.add(cmd);
            }
        }
        return commandeClient.stream()
                .sorted(Comparator.comparing(CommandeDTO::getDateCommande).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Retourne un client par son id
     *
     * @param id {@link UUID}
     * @return {@link User}
     */
    @Override
    public User getClient(UUID id) {
        return serviceClient.getClientById(id);
    }


    /**
     * Cette méthode récupère l'identifiant de l'utilisateur à partir d'un jeton d'authentification ou de l'API de profil, en fonction de la disponibilité de l'identifiant dans le jeton.
     *
     * @param token                  {@link String} Le jeton d'authentification à partir duquel extraire l'identifiant de l'utilisateur.
     * @param userIdAttributeToken   {@link String} Le nom de l'attribut dans le jeton contenant l'identifiant de l'utilisateur.
     * @param userIdAttributeProfile {@link String} Le nom de l'attribut dans la réponse de l'API de profil contenant l'identifiant de l'utilisateur.
     * @param idStore                {@link UUID} L'identifiant unique du store pour lequel obtenir l'identifiant de l'utilisateur.
     * @return String - L'identifiant de l'utilisateur récupéré, ou null si une erreur s'est produite lors de la récupération.
     */
    public String getUserId(String token, String userIdAttributeToken, String userIdAttributeProfile, UUID idStore) {
        String userIdd = null;
        JsonNode jsonNode = iServiceApiExterne.decodeToken(token);
        if (jsonNode.has(userIdAttributeToken)) {
            userIdd = jsonNode.get(userIdAttributeToken).asText();
        }
        if (userIdd == null) {
            StoreDTO store = serviceStore.findStoreById(idStore);
            String urlProfil = store.getConfigStore().getApiGetUser();
            ResponseEntity<String> result = iServiceApiExterne.apiExterneMethodeGetAvecAutorisation(token, urlProfil);
            JsonNode jsonNodeProfile = iServiceApiExterne.verifieResponseAndGetJsonNode(result);
            userIdd = jsonNodeProfile.get(userIdAttributeProfile).asText();
        }
        return userIdd;
    }


    /**
     * Permet de vérifier s'il y a des produits commandés hors stock
     *
     * @param commandeDTO {@link CommandeDTO}
     * @return List<DetailsCommande>
     */
    public boolean verifProduitsHorsStock(CommandeDTO commandeDTO) {
        List<DetailsCommande> detailsCommandeList = commandeDTO.getDetailsCommandes();
        for (DetailsCommande details : detailsCommandeList) {
            if (details.getQuantite() > serviceProduit.getQuantityByProduct(details.getProduit().getReferenceId(), details.getProduit().getStore().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * elle permet de calculer le montant total de la commande
     *
     * @param commandeDTO {@link CommandeDTO}
     * @return double
     */
    private double calculTotalMontantCommande(CommandeDTO commandeDTO) {
        double totalMontantCommande = 0;
        if (commandeDTO != null) {
            List<DetailsCommande> listDetailsCommande = commandeDTO.getDetailsCommandes();
            if (listDetailsCommande != null && !listDetailsCommande.isEmpty()) {
                for (DetailsCommande details : listDetailsCommande) {
                    if (details != null && details.getProduit() != null) {
                        totalMontantCommande += (details.getPrixUnitaire()) * details.getQuantite();
                        System.out.println("totalMontantCommande :" + totalMontantCommande);
                    }
                }
            }
        }
        return totalMontantCommande;
    }

    /**
     * Convertit un nœud JSON en un objet Java correspondant.
     * Cette méthode prend un nœud JSON (JsonNode) en entrée et le convertit en un objet Java du type correspondant.
     *
     * @param valueNode {@link JsonNode} le nœud JSON à convertir
     * @return l'objet Java correspondant au contenu du nœud JSON
     * @throws JsonProcessingException si une erreur survient lors de la conversion
     */
    public Object convertirJsonEnObjetJava(JsonNode valueNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object value;
        if (valueNode.isTextual()) {
            value = valueNode.asText();
        } else if (valueNode.isBoolean()) {
            value = valueNode.asBoolean();
        } else if (valueNode.isInt()) {
            value = valueNode.asInt();
        } else if (valueNode.isLong()) {
            value = valueNode.asLong();
        } else if (valueNode.isDouble()) {
            value = valueNode.asDouble();
        } else if (valueNode.isArray()) {
            List<Object> listValue = new ArrayList<>();
            for (JsonNode arrayElement : valueNode) {
                if (arrayElement.isObject()) {
                    listValue.add(objectMapper.treeToValue(arrayElement, Map.class));
                } else {
                    listValue.add(arrayElement);
                }
            }
            value = listValue;
        } else {
            value = valueNode.isNull() ? null : valueNode.toString();
        }
        return value;
    }


    /**
     * Cette méthode génère le corps de la requête JSON à partir des données de la commande, en utilisant la configuration spécifiée des attributs de commande.
     *
     * @param listdetailsCommande     {@link List<DetailsCommande>} La liste des détails de commande à mettre à jour.
     * @param montantCommande         {@link double} Le montant total de la commande.
     * @param idStore                 {@link UUID} L'identifiant unique du store.
     * @param configAttributsCommande {@link ConfigAttributsCommande} La configuration des attributs de commande.
     * @param userId                  {@link UUID} L'identifiant de l'utilisateur associé à la commande.
     * @return Map<String, Object> - Le corps de la requête JSON généré.
     * @throws JsonProcessingException Si une erreur survient lors du traitement JSON.
     */
    public Map<String, Object> requestBodyAddCommande(List<DetailsCommande> listdetailsCommande, double montantCommande, String dateCommande, UUID idStore, ConfigAttributsCommande configAttributsCommande, String userId) throws JsonProcessingException {
        StoreDTO store = serviceStore.findStoreById(idStore);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(store.getConfigStore().getPayload());

        Map<String, Object> requestBodyJson = new HashMap<>();

        for (Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields(); iterator.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String key = entry.getKey();
            JsonNode valueNode = entry.getValue();
            // Vérifier si la clé de configuration attendue est présente
            if (key.equals(configAttributsCommande.getMontant())) {
                requestBodyJson.put(configAttributsCommande.getMontant(), String.valueOf(montantCommande));
            } else if (key.equals(configAttributsCommande.getUserIdCommande())) {
                requestBodyJson.put(configAttributsCommande.getUserIdCommande(), userId);
            } else if (key.equals(configAttributsCommande.getDateCommande())) {
                requestBodyJson.put(configAttributsCommande.getDateCommande(), String.valueOf(dateCommande));
            } else if (key.equals(configAttributsCommande.getDetailsCommandes())) {

                List<Map<String, Object>> detailsCommandesList = new ArrayList<>();

                for (DetailsCommande detailsCommandeMiseaJour : listdetailsCommande) {
                    ProduitDTO produitDTO = serviceProduit.findProduitById(detailsCommandeMiseaJour.getProduit().getId());
                    // Parcourir les éléments de la liste detailscommande
                    for (JsonNode lineModelNode : valueNode) {
                        Map<String, Object> lineModel = new HashMap<>();
                        // Parcourir les clés de chaque élément de la liste lineModels
                        for (Iterator<Map.Entry<String, JsonNode>> iterator2 = lineModelNode.fields(); iterator2.hasNext(); ) {
                            Map.Entry<String, JsonNode> entry2 = iterator2.next();
                            String key2 = entry2.getKey();
                            JsonNode node = entry2.getValue();
                            lineModel.put(key2, convertirJsonEnObjetJava(node));
                        }
                        // Ajouter les autres détails spécifiques du produit
                        lineModel.put(configAttributsCommande.getDetails_commande_IdProduit(), produitDTO.getReferenceId());
                        lineModel.put(configAttributsCommande.getDetails_commande_quantity(), String.valueOf(detailsCommandeMiseaJour.getQuantite()));
                        System.out.println("prixUnitaire" + produitDTO.getPrix());
                        lineModel.put(configAttributsCommande.getDetails_commande_PrixUnitaire(), detailsCommandeMiseaJour.getPrixUnitaire());
                        // Ajouter les détails du produit à la liste
                        detailsCommandesList.add(lineModel);
                    }
                }
                // Ajouter la liste de détails de commande au corps de la requête
                requestBodyJson.put(configAttributsCommande.getDetailsCommandes(), detailsCommandesList);
            } else {
                // Pour les autres clés non spécifiées dans la configuration, ajouter telles quelles
                // Convertir la valeur en l'objet Java approprié
                Object value = convertirJsonEnObjetJava(valueNode);
                requestBodyJson.put(key, value);
            }
        }
        return requestBodyJson;
    }

    /**
     * Cette méthode récupère les attributs de configuration de store pour l'ajout d'une commande.
     *
     * @param storeId {@link UUID} L'identifiant du store pour lequel récupérer les attributs de configuration.
     * @return ConfigAttributsCommande - contenant les attributs de configuration pour l'ajout de commande.
     */
    public ConfigAttributsCommande configStoreAttributsAddCommande(UUID storeId) {
        List<ConfigStoreAttributsDTO> configStoreAttributsDTOList = iServiceConfigStoreAttributs.findConfigStoreAttributsByConfigMethode(storeId, ConfigMethode.POST);
        ConfigAttributsCommande configAttributsCommande = new ConfigAttributsCommande();
        for (ConfigStoreAttributsDTO a : configStoreAttributsDTOList) {
            if (Objects.nonNull(a)) {
                configAttributsCommande.setAttributeValueByKey(a.getAttributeKey(), a.getAttributeValue());
            }
        }
        return configAttributsCommande;
    }

    /**
     * Cette méthode convertit une map de chaînes d'objets en une chaîne JSON.
     *
     * @param requestBodyJson {@link Map} La map à convertir en chaîne JSON.
     * @return String - La chaîne JSON résultante.
     */
    public String convertirMapEnStringJson(Map<String, Object> requestBodyJson) {
        System.out.println("méthode convertirMapEnStringJson");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJsonString = null;
        try {
            requestBodyJsonString = objectMapper.writeValueAsString(requestBodyJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestBodyJsonString;
    }

    /**
     * Cette méthode crée une commande en envoyant une requête externe vers l'API appropriée du store.
     * Elle effectue les étapes suivantes :
     * 1. Vérifie si tous les produits sont en stock.
     * 2. Calcule le montant total de la commande.
     * 3. Récupère la configuration des attributs de commande pour le store.
     * 4. Effectue une authentification pour obtenir le jeton d'accès.
     * 5. Obtient l'identifiant de l'utilisateur à partir du jeton d'accès.
     * 6. Crée le corps de la requête JSON à envoyer à l'API du store.
     * 7. Envoie la requête POST à l'API du magasin avec le corps de la requête JSON.
     *
     * @param commandeDTO {@link CommandeDTO}  Les détails de la commande à créer.
     * @param idUser      {@link UUID}  L'identifiant de l'utilisateur associé à la commande.
     * @param idStore     {@link UUID}  L'identifiant du magasin où la commande est passée.
     * @return ResponseEntity<String> .
     * @throws JsonProcessingException Si une erreur se produit lors de la conversion des objets en chaîne JSON.
     */
    @Override
    public ResponseEntity<String> createCommandeSiteExterne(CommandeDTO commandeDTO, UUID idUser, UUID idStore) throws JsonProcessingException {
        boolean toutProduitsDisponibles = verifProduitsHorsStock(commandeDTO);
        if(!toutProduitsDisponibles){
            List<DetailsCommande> detailsCommandeList = commandeDTO.getDetailsCommandes();

            double montant_commande = calculTotalMontantCommande(commandeDTO);

            // Obtenir la date système
            LocalDateTime dateSystem = LocalDateTime.now();
            // Formatter la date selon le format souhaité
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Convertir la date en chaîne de caractères
            String date = dateSystem.format(formatter);

            ConfigAttributsCommande configAttributsCommande = configStoreAttributsAddCommande(idStore);

            StoreDTO store = serviceStore.findStoreById(idStore);

            String token = iServiceApiExterne.getToken(store);
            String userIdd = this.getUserId(token, configAttributsCommande.getUserIdAttributeToken(), configAttributsCommande.getUserIdAttributeProfile(), idStore);

            Map<String, Object> requestBodyJson = requestBodyAddCommande(detailsCommandeList, montant_commande, date, idStore, configAttributsCommande, userIdd);

            System.out.println("Corps de la requête externe : " + requestBodyJson.toString());

            String requestBodyJsonString = convertirMapEnStringJson(requestBodyJson);

            String url = store.getConfigStore().getAPIAddCommande();
            return iServiceApiExterne.getResponseMethodePost(token, requestBodyJsonString, url);
        }
        return null;
    }

    /**
     * Cette méthode envoie une commande à un site externe.
     * Vérifie le code d'état de la réponse.
     * Ajoute une commande dans la base de données locale si le code d'état est 200.
     *
     * @param commandeDTO {@link CommandeDTO} contenant les détails de la commande à créer.
     * @param idUser      {@link UUID} l'UUID de l'utilisateur qui crée la commande.
     * @param idStore     {@link UUID} l'UUID du store où la commande est créée.
     * @return {@link CommandeDTO} la commande créée si la requête réussit, sinon retourne null.
     * @throws JsonProcessingException si une erreur de traitement JSON survient lors de la communication avec le site externe.
     */
    @Override
    public CommandeDTO verifStatusAndCreateCommand(CommandeDTO commandeDTO, UUID idUser, UUID idStore) throws JsonProcessingException {
        ResponseEntity<String> result = this.createCommandeSiteExterne(commandeDTO, idUser, idStore);
        // Vérifier le code d'état HTTP
        if (result.getStatusCode().is2xxSuccessful()) {
            // La requête a réussi (code d'état 2xx)
            System.out.println("La requête a réussi. Code d'état : " + result.getStatusCode().value());
            return create(commandeDTO, idUser);
        } else {
            // La requête a échoué.
            System.err.println("La requête a échoué. Code d'état : " + result.getStatusCode().value());
            return null;
        }
    }


    /**
     * Cette méthode divise les détails de la commande par store.
     * Crée une nouvelle commande pour chaque magasin.
     * Et envoie chaque commande à un site externe.
     * Ensuite, elle crée la commande principale.
     *
     * @param commandeDTO {@link CommandeDTO} contenant les détails de la commande initiale
     * @param idUser      {@link UUID} UUID de l'utilisateur qui crée la commande.
     * @return {@link CommandeDTO} la commande principale après la création des commandes spécifiques aux magasins.
     * @throws JsonProcessingException si une erreur de traitement JSON survient lors de la communication avec le site externe.
     */
    @Override
    public CommandeDTO createCommandeDifferentStores(CommandeDTO commandeDTO, UUID idUser) throws JsonProcessingException {
        List<DetailsCommande> detailsCommandeList = commandeDTO.getDetailsCommandes();
        // Regrouper les détails par ID de store en utilisant les Streams de Java
        Map<UUID, List<DetailsCommande>> detailsByStore = detailsCommandeList.stream()
                .collect(Collectors.groupingBy(details -> details.getProduit().getStore().getId()));

        // Traiter maintenant chaque groupe de détails pour chaque store
        for (Map.Entry<UUID, List<DetailsCommande>> entry : detailsByStore.entrySet()) {
            UUID storeId = entry.getKey();
            List<DetailsCommande> storeDetails = entry.getValue();

            // Créer un nouveau CommandeDTO pour chaque store
            CommandeDTO storeCommandeDTO = new CommandeDTO();
            storeCommandeDTO.setDetailsCommandes(storeDetails);
            createCommandeSiteExterne(storeCommandeDTO, idUser, storeId);
        }
        return create(commandeDTO, idUser);
    }

    /**
     * Retourner le nombre total des commandes.
     * @return {@link long} - le nombre total des commandes
     */
    @Override
    public long nbreCommandes() {
        return commandeRepository.findAll().size();
    }

    /**
     * Calcule et retourne le montant total des ventes.
     *
     * @return le montant total des ventes.
     */
    @Override
    public double totalVentes() {
        double totalVentes = 0;
        List<Commande> commandes = commandeRepository.findAll();
        for (Commande commande : commandes) {
            totalVentes += commande.getMontant();
        }
        return totalVentes;
    }

    /**
     * Calcule et retourne le montant total des ventes pour un store spécifique.
     *
     * @param storeId {@link UUID}
     * @return le montant total des ventes pour le store spécifié.
     */
    @Override
    public double totalVentesByStore(UUID storeId) {
        double totalVentes = 0;
        List<CommandeDTO> commandes = findAllCommandes();
        for (CommandeDTO commande : commandes) {
            for (DetailsCommande detailsCommande : commande.getDetailsCommandes()) {
                if ((detailsCommande.getProduit().getStore().getId()).equals(storeId)) {
                    totalVentes += (detailsCommande.getPrixUnitaire()) * (detailsCommande.getQuantite());
                }
            }
        }
        return totalVentes;
    }


}
