package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.*;
import com.example.backendpfe.entities.typeEnum.TypeBody;
import com.example.backendpfe.entities.typeEnum.TypeRemise;
import com.example.backendpfe.entities.typeEnum.TypeResponseApiGetProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigStoreDTO {
    private UUID id;
    private String url;
    private TypeResponseApiGetProduct typeResponseApiGetProduit;
    private String paramsApiPaginable;
    private String cheminListProduit ;
    private String APIAddCommande;
    private String ApiLogin;
    private String email;
    private String password;
    private String cheminToken;
    private TypeBody typeBodyLogin;
    private String ApiGetUser;
    private TypeRemise typeRemise;
    private boolean typeAutorisationApiGetProduit;
    private String logo;
    private String payload;
    private String urlPhoto;
    private Store store;
    private List<ConfigStoreAttributs> configStoreAttributs = new ArrayList<>();
}
