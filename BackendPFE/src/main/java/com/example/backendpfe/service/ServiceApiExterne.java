package com.example.backendpfe.service;

import com.example.backendpfe.DTO.ConfigAttributsLogin;
import com.example.backendpfe.DTO.StoreDTO;
import com.example.backendpfe.IService.IServiceApiExterne;
import com.example.backendpfe.entities.ConfigStoreAttributs;
import com.example.backendpfe.entities.Store;
import com.example.backendpfe.entities.typeEnum.TypeBody;
import com.example.backendpfe.mapper.StoreMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class ServiceApiExterne implements IServiceApiExterne {
    @Autowired
    private StoreMapper storeMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Cette méthode effectue une requête GET vers une API externe à l'aide de l'URL spécifiée.
     *
     * @param url {@link String} Un objet ResponseEntity contenant la réponse de la requête HTTP. Le corps de la réponse est de type String
     * @return ResponseEntity<String> - contenant la réponse de l'API externe sous forme de chaîne de caractères.
     */
    @Override
    public ResponseEntity<String> apiExterneMethodeGet(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, String.class);
    }

    /**
     * Appelle une méthode GET sur une API externe en incluant un jeton d'autorisation dans les en-têtes.
     *
     * @param token {@link String} Le jeton d'autorisation
     * @param url   {@link String} L'URL de l'API externe à appeler.
     * @return ResponseEntity<String> - contenant la réponse de l'API externe sous forme de chaîne de caractères.
     */
    @Override
    public ResponseEntity<String> apiExterneMethodeGetAvecAutorisation(String token, String url) {
        // Création des en-têtes
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        // Création de l'objet HttpEntity avec les en-têtes
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    /**
     * Cette méthode analyse la réponse de type ResponseEntity<String> et retourne son contenu sous forme de JsonNode, si la réponse du serveur est réussie.
     *
     * @param response {@link ResponseEntity<String>} La réponse à analyser
     * @return JsonNode - contenant le contenu de la réponse si la requête est réussie, sinon null.
     */
    @Override
    public JsonNode verifieResponseAndGetJsonNode(ResponseEntity<String> response) {
        JsonNode jsonNode = null;
        // Vérifier si la réponse du serveur (réussie ou non)
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                jsonNode = mapperChaineJsonEnObjetJson(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // La réponse du serveur n'est pas réussie
            System.out.println("La requête a échoué avec le code d'état : " + response.getStatusCode().value());
        }
        return jsonNode;
    }

    /**
     * Récupère le nœud JSON correspondant au chemin spécifié.
     *
     * @param jsonNode {@link JsonNode} Le nœud JSON de départ.
     * @param chemin   {@link String}   Le chemin spécifiant l'emplacement du nœud recherché.
     * @return JsonNode - Le nœud JSON correspondant au chemin.
     */
    @Override
    public JsonNode getNoeudSelonChemin(JsonNode jsonNode, String chemin) {
        if (chemin != null && !chemin.isEmpty()) {
            String[] nodes = chemin.split("\\.");
            for (String noeud : nodes) {
                if (jsonNode == null) {
                    return null;
                }

                if (noeud.matches("\\d+")) {
                    int index = Integer.parseInt(noeud);
                    if (jsonNode.isArray() && index < jsonNode.size()) {
                        jsonNode = jsonNode.get(index);
                    } else {
                        return null; // Index out of bounds or not an array
                    }
                } else {
                    jsonNode = jsonNode.get(noeud);
                }
            }
        }
        return jsonNode;
    }


    /*
    public JsonNode getNoeudSelonChemin(JsonNode jsonNode, String chemin) {
        if (chemin != null && !chemin.isEmpty()) {
            String[] nodes = chemin.split("\\.");
            for (String noeud : nodes) {
                if(noeud != null){
                    jsonNode = jsonNode.get(noeud);
                }
            }
        }
        return jsonNode;
    }

     */

    /**
     * Envoie une requête POST à l'URL spécifiée de l'API externe avec un corps de requête de type chaîne et retourne la réponse sous forme de chaîne.
     *
     * @param url {@link String} L'URL de l'API externe à laquelle envoyer la requête POST.
     * @param requestEntity {@link HttpEntity<String>} L'entité HTTP contenant le corps de la requête et les en-têtes.
     * @return ResponseEntity<String> La réponse de l'API externe.
     */
    @Override
    public ResponseEntity<String> apiExterneMethodePost(String url, HttpEntity<String> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url, requestEntity, String.class);
        } catch (HttpServerErrorException e) {
            System.out.println("Erreur du serveur lors de l'appel à l'API externe: {}"+ e.getResponseBodyAsString());
            throw e;
        }
       return  response;
    }

    /**
     * Envoie une requête POST avec un corps JSON à l'URL spécifiée de l'API externe et retourne la réponse sous forme de chaîne.
     *
     * @param url {@link String}L'URL de l'API externe à laquelle la requête POST est envoyée.
     * @param requestEntity {@link HttpEntity<Map<String, Object>>}L'entité de la requête contenant le corps JSON et les en-têtes HTTP.
     * @return Une ResponseEntity contenant la réponse de l'API externe sous forme de chaîne.
     * @throws HttpClientErrorException si une erreur HTTP se produit pendant la requête.
     */
    @Override
    public ResponseEntity<String> apiExternePostJSON(String url, HttpEntity<Map<String, Object>> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            System.out.println("Erreur lors de l'envoi de la demande : " + e.getStatusCode() + " - " + responseBody);
            throw e;
        }
    }

    /**
     * Crée une entité HTTP (HttpEntity) contenant un corps de requête JSON pour une demande de connexion et les en-têtes appropriés.
     *
     * @param email L'adresse e-mail.
     * @param password Le mot de passe.
     * @param configAttributsLogin Un objet ConfigAttributsLogin contenant les clés pour les attributs de l'email et du mot de passe.
     * @return Une HttpEntity contenant une Map avec les données de connexion et les en-têtes HTTP JSON.
     */
    @Override
    public HttpEntity<Map<String, Object>> requestEntityLoginJSON(String email, String password, ConfigAttributsLogin configAttributsLogin) {
        System.out.println("méthode requestEntityLoginJSON");
        // Création d'une Map pour les données
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(configAttributsLogin.getEmail(), email);
        requestBody.put(configAttributsLogin.getPassword(), password);
        requestBody.put("rememberMe", "true");
        // En-têtes de la requête
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        // Création de l'entité HTTP avec le corps et les en-têtes
        return new HttpEntity<>(requestBody, headers1);
    }

    /**
     * Envoi de la requête POST et récupération de la réponse.
     *
     * @param url           {@link String} L'URL de l'API externe.
     * @param requestEntity {@link HttpEntity} L'entité HTTP contenant le corps de la requête et les en-têtes.
     * @return ResponseEntity<String> - contenant la réponse de la requete.
     */
    @Override
    public ResponseEntity<String> apiExternePostURLENCODED(String url, HttpEntity<MultiValueMap<String, String>> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            System.out.println("Erreur lors de l'envoi de la demande : " + e.getStatusCode() + " - " + responseBody);
            throw e;
        }
    }

    /**
     * Crée une entité HTTP pour une demande de connexion.
     *
     * @param email {@link String}
     * @param password {@link String}
     * @param configAttributsLogin {@link ConfigAttributsLogin} La configuration des attributs de connexion, contenant les clés pour l'email et le mot de passe
     * @return HttpEntity - contenant les données de la demande et les en-têtes correspondants
     */
    @Override
    public HttpEntity<MultiValueMap<String, String>> requestEntityLoginURLENCODED(String email, String password, ConfigAttributsLogin configAttributsLogin) {
        System.out.println("méthode requestEntityLoginURLENCODED");
        // Création d'une Map pour les données
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(configAttributsLogin.getEmail(), email);
        requestBody.add(configAttributsLogin.getPassword(), password);
        requestBody.add("rememberMe", "true");
        // En-têtes de la requête
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Création de l'entité HTTP avec le corps et les en-têtes
        return new HttpEntity<>(requestBody, headers1);
    }

    /**
     * Permet de mapper une chaine JSON en un objet JSON.
     *
     * @param response {@link ResponseEntity<String>} La réponse HTTP contenant la chaîne JSON à mapper
     * @return JsonNode - représentant la structure JSON
     * @throws JsonProcessingException Si une erreur de traitement JSON se produit.
     */
    @Override
    public JsonNode mapperChaineJsonEnObjetJson(ResponseEntity<String> response) throws JsonProcessingException {
        String responseBody = response.getBody();
        return this.objectMapper.readTree(responseBody);
    }

    /**
     * Effectue une requete POST
     *
     * @param token                 {@link String}
     * @param requestBodyJsonString {@link String}
     * @param url                   {@link String}
     * @return ResponseEntity - String
     */
    @Override
    public ResponseEntity<String> getResponseMethodePost(String token, String requestBodyJsonString, String url) {
        // Création d'un objet HttpHeaders pour définir les entêtes de la requête
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // Création de l'entité HttpEntity avec le corps JSON et les entêtes
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJsonString, headers);
        return apiExterneMethodePost(url, requestEntity);
    }


    /**
     * Obtient la configuration des attributs de connexion.
     *
     * @param store {@link StoreDTO}
     * @return ConfigAttributsLogin - La configuration des attributs de connexion.
     */
    @Override
    public ConfigAttributsLogin getConfigAttributsLogin(StoreDTO store) {
        ConfigAttributsLogin configStoreAttributsLogin = new ConfigAttributsLogin();
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("email")) {
                configStoreAttributsLogin.setEmail(configStoreAttributs.getAttributeValue());
            }
            if (configStoreAttributs.getAttributeKey().equals("password")) {
                configStoreAttributsLogin.setPassword(configStoreAttributs.getAttributeValue());
            }
        }
        return configStoreAttributsLogin;
    }

    /**
     * Effectue une requête de connexion à un site externe en utilisant les informations du store spécifié.
     *
     * @param store {@link StoreDTO}
     * @return ResponseEntity<String> - contenant la réponse de la requête de connexion - le corps de la réponse est une chaîne de caractères.
     */
    @Override
    public ResponseEntity<String> responseLoginSiteExterne(StoreDTO store) {
        String urlLogin = store.getConfigStore().getApiLogin();
        String email = store.getConfigStore().getEmail();
        String password = store.getConfigStore().getPassword();
        ConfigAttributsLogin configAttributsLogin = getConfigAttributsLogin(store);
        TypeBody typeBody = store.getConfigStore().getTypeBodyLogin();
        if(typeBody.equals(TypeBody.FORM_URLENCODED)){
            HttpEntity<MultiValueMap<String, String>> requestEntity = requestEntityLoginURLENCODED(email, password, configAttributsLogin);
            // Envoi de la requête POST et récupération de la réponse
            return apiExternePostURLENCODED(urlLogin, requestEntity);
        }
        else {
            HttpEntity<Map<String, Object>> requestEntity = requestEntityLoginJSON(email, password, configAttributsLogin);
            // Envoi de la requête POST et récupération de la réponse
            return apiExternePostJSON(urlLogin, requestEntity);
        }
    }


    /**
     * Récupère le jeton d'authentification.
     *
     * @param store {@link StoreDTO} L'identifiant unique du store.
     * @return String - cotenant le jeton.
     */
    @Override
    public String getToken(StoreDTO store) {
        String token = null;
        ResponseEntity<String> responseEntity = responseLoginSiteExterne(store);
        JsonNode jsonNode = verifieResponseAndGetJsonNode(responseEntity);
        String cheminToken = store.getConfigStore().getCheminToken();
        if (cheminToken != null && !cheminToken.isEmpty()) {
            jsonNode = getNoeudSelonChemin(jsonNode, cheminToken);
            token = jsonNode.asText();
        } else {
            System.err.println("Token introuvable dans la réponse JSON.");
        }
        return token;
    }

    /**
     * Décode un token JWT et retourne le payload sous forme de JsonNode.
     *
     * @param token Le token JWT à décoder.
     * @return Un JsonNode représentant le payload du token décodé, ou null si une erreur se produit.
     */
    @Override
    public JsonNode decodeToken(String token) {
        JsonNode jsonNode = null;
        try {
            // Divisez le token en parties (header, payload, signature)
            String[] parts = token.split("\\.");
            // La partie du payload est à l'index 1
            String payload = parts[1];
            // Décodage Base64 de la partie du payload
            byte[] decodedPayload = Base64.getDecoder().decode(payload);
            // Conversion du payload décodé en chaîne de caractères
            String payloadString = new String(decodedPayload);

            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(payloadString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode;
    }


    /**
     * Convertit une chaîne de paramètres en une matrice de chaînes de caractères représentant des paires clé-valeur.
     *
     * @param params Une chaîne de paramètres où chaque paramètre est séparé par une virgule et chaque paire clé-valeur est séparée par un deux-points (par exemple, "clé1:valeur1,clé2:valeur2").
     * @return Une matrice de chaînes de caractères où chaque ligne contient une paire clé-valeur.
     * @throws IllegalArgumentException si un paramètre n'est pas au format clé:valeur.
     */
    @Override
    public String[][] configQueryParams(String params) {
        String[] paramsResult = null;
        if (params != null && !params.isEmpty()) {
            paramsResult = params.split(",");
        }
        String[][] result = new String[paramsResult.length][2];

        for (int i = 0; i < paramsResult.length; i++) {
            String[] queryParam = paramsResult[i].split(":");
            if (queryParam.length == 2) {
                result[i][0] = queryParam[0];
                result[i][1] = queryParam[1];
            } else {
                throw new IllegalArgumentException("Invalid parameter format: " + paramsResult[i]);
            }
        }
        return result;
    }

    /**
     * Configure les paramètres de requête en utilisant les paires clé-valeur fournies et remplace les valeurs des paramètres de pagination par les valeurs spécifiées.
     *
     * @param paramsRequete Une matrice de chaînes de caractères contenant les paires clé-valeur des paramètres de requête.
     * @param builder Un UriComponentsBuilder pour construire l'URI avec les paramètres de requête.
     * @param pageNumberAttribut Le nom de l'attribut pour le numéro de page.
     * @param pageSizeAttribut Le nom de l'attribut pour la taille de page.
     * @param pageNumber Le numéro de page à utiliser pour le paramètre de pagination.
     * @param pageSize La taille de page à utiliser pour le paramètre de pagination.
     */
    @Override
    public void setQueryParamsRequete(String[][] paramsRequete,
                                      UriComponentsBuilder builder,
                                      String pageNumberAttribut,
                                      String pageSizeAttribut,
                                      int pageNumber,
                                      int pageSize) {
        builder.replaceQuery(null);
        if (paramsRequete != null) {
            for (String[] queryParam : paramsRequete) {
                if (queryParam.length == 2) {
                    String key = queryParam[0];
                    System.out.println("key :" + key);
                    String value;
                    if (key.equals(pageNumberAttribut)) {
                        value = String.valueOf(pageNumber);
                    } else if (key.equals(pageSizeAttribut)) {
                        value = String.valueOf(pageSize);
                    } else {
                        value = queryParam[1];
                    }
                    System.out.println("value :" + value);
                    builder.queryParam(key, value);
                }
            }
        }
    }


    /**
     * Récupère le nombre total d'éléments d'une API paginée en effectuant une requête GET avec les paramètres de requête spécifiés.
     *
     * @param store {@link StoreDTO}.
     * @param queryParams Une matrice de chaînes de caractères contenant les paires clé-valeur des paramètres de requête.
     * @param url L'URL de l'API.
     * @param builder Un UriComponentsBuilder pour construire l'URI avec les paramètres de requête.
     * @param pageNumberAttribut Le nom de l'attribut pour le numéro de page.
     * @param pageSizeAttribut Le nom de l'attribut pour la taille de page.
     * @param totalElementsAttribut Le nom de l'attribut pour le nombre total d'éléments dans la réponse JSON.
     * @return Le nombre total d'éléments renvoyés par l'API paginée.
     */
    @Override
    public int getNbreTotalElementsApiPaginee(StoreDTO store, String[][] queryParams, String url, UriComponentsBuilder builder, String pageNumberAttribut, String pageSizeAttribut,String totalElementsAttribut)
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null ;
        setQueryParamsRequete(
                queryParams,
                builder,
                pageNumberAttribut,
                pageSizeAttribut,
                1,
                1
        );
        HttpEntity<String> entity = null;
        boolean typeAutorisation = store.getConfigStore().isTypeAutorisationApiGetProduit();
        if (typeAutorisation) {
            String token = getToken(store);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            // Création de l'objet HttpEntity avec les en-têtes
            entity = new HttpEntity<>(headers);

            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } else {
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    String.class
            );
        }
        JsonNode jsonNode1 = verifieResponseAndGetJsonNode(response);
        return jsonNode1.get(totalElementsAttribut).asInt();
    }


    /**
     * Effectue une requête GET vers une API paginée en utilisant les paramètres configurés pour le store spécifié.
     * La méthode configure les paramètres de requête, récupère le nombre total d'éléments de l'API paginée,
     * puis effectue la requête GET avec ou sans autorisation selon la configuration du store.
     *
     * @param store L'objet Store contenant les informations de configuration pour l'API paginée.
     * @param pageNumberAttribut Le nom de l'attribut pour le numéro de page dans les paramètres de requête.
     * @param pageSizeAttribut Le nom de l'attribut pour la taille de page dans les paramètres de requête.
     * @param totalElementsAttribut Le nom de l'attribut pour le nombre total d'éléments dans la réponse JSON de l'API paginée.
     * @return Une ResponseEntity<String> contenant la réponse de l'API paginée suite à la requête GET.
     */
    @Override
    public ResponseEntity<String> responseApiPagineeGet(Store store, String pageNumberAttribut, String pageSizeAttribut,String totalElementsAttribut,String url) {
        ResponseEntity<String> response = null;
        if (store.getConfigStore() != null) {
            boolean typeAutorisation = store.getConfigStore().isTypeAutorisationApiGetProduit();
            if (url != null && !url.isEmpty()) {
                String params = "";
                String[][] queryParams = null;
                params = store.getConfigStore().getParamsApiPaginable();
                queryParams = configQueryParams(params);
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                int totalElements = getNbreTotalElementsApiPaginee(storeMapper.toDTO(store), queryParams, url, builder, pageNumberAttribut, pageSizeAttribut,totalElementsAttribut);
                RestTemplate restTemplate = new RestTemplate();
                UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(url);
                setQueryParamsRequete(
                        queryParams,
                        builder2,
                        pageNumberAttribut,
                        pageSizeAttribut,
                        1,
                        totalElements
                );
                if (typeAutorisation) {
                    HttpEntity<String> entity = null;
                    String token = getToken(storeMapper.toDTO(store));
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);
                    // Création de l'objet HttpEntity avec les en-têtes
                    entity = new HttpEntity<>(headers);

                    response = restTemplate.exchange(
                            builder2.toUriString(),
                            HttpMethod.GET,
                            entity,
                            String.class
                    );
                } else {
                    response = restTemplate.exchange(
                            builder2.toUriString(),
                            HttpMethod.GET,
                            null,
                            String.class
                    );
                }
            }
        }
        return response;
    }
}