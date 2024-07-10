package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.ConfigAttributsLogin;
import com.example.backendpfe.DTO.StoreDTO;
import com.example.backendpfe.entities.Store;
import com.example.backendpfe.entities.typeEnum.TypeBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public interface IServiceApiExterne {
    public ResponseEntity<String> apiExterneMethodeGet(String url);

    public ResponseEntity<String> apiExterneMethodeGetAvecAutorisation(String token, String url);

    public ResponseEntity<String> apiExterneMethodePost(String url, HttpEntity<String> requestEntity);

    public ResponseEntity<String> apiExternePostJSON(String url, HttpEntity<Map<String, Object>> requestEntity);

    public HttpEntity<Map<String, Object>> requestEntityLoginJSON(String email, String password, ConfigAttributsLogin configAttributsLogin);

    public ResponseEntity<String> apiExternePostURLENCODED(String url, HttpEntity<MultiValueMap<String, String>> requestEntity);

    public HttpEntity<MultiValueMap<String, String>> requestEntityLoginURLENCODED(String email, String password, ConfigAttributsLogin configAttributsLogin);


    public JsonNode mapperChaineJsonEnObjetJson(ResponseEntity<String> response) throws JsonProcessingException;

    public JsonNode verifieResponseAndGetJsonNode(ResponseEntity<String> response);

    public JsonNode getNoeudSelonChemin(JsonNode jsonNode, String chemin);

    public ResponseEntity<String> getResponseMethodePost(String token, String requestBodyJsonString, String url);

    public ConfigAttributsLogin getConfigAttributsLogin(StoreDTO store);
    public ResponseEntity<String> responseLoginSiteExterne(StoreDTO store);
    public String getToken(StoreDTO store);
    public JsonNode decodeToken(String token);

    public String[][] configQueryParams(String params);

    public void setQueryParamsRequete(String[][] paramsRequete,
                                      UriComponentsBuilder builder,
                                      String pageNumberAttribut,
                                      String pageSizeAttribut,
                                      int pageNumber,
                                      int pageSize);

    public int getNbreTotalElementsApiPaginee(StoreDTO store, String[][] queryParams, String url, UriComponentsBuilder builder, String pageNumberAttribut, String pageSizeAttribut,String totalElementsAttribut);
    public ResponseEntity<String> responseApiPagineeGet(Store store, String pageNumberAttribut, String pageSizeAttribut,String totalElementsAttribut,String url);
}
