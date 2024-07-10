package com.example.backendpfe.service;

import com.example.backendpfe.DTO.ConfigAttributsApiPaginee;
import com.example.backendpfe.DTO.ConfigAttributsQuantiteProduit;
import com.example.backendpfe.DTO.ConfigStoreAttributsDTO;
import com.example.backendpfe.IService.IServiceConfigStoreAttributs;
import com.example.backendpfe.entities.typeEnum.ConfigMethode;
import com.example.backendpfe.entities.ConfigStoreAttributs;
import com.example.backendpfe.entities.Store;
import com.example.backendpfe.exception.ConfigStoreAttributsNotfoundException;
import com.example.backendpfe.mapper.ConfigStoreAttributsMapper;
import com.example.backendpfe.repository.ConfigStoreAttributsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceConfigStoreAttributs implements IServiceConfigStoreAttributs {
    @Autowired
    private ConfigStoreAttributsRepository configStoreAttributsRepository;
    @Autowired
    private ConfigStoreAttributsMapper configStoreAttributsMapper;

    /**
     * Crééer un nouvel attribut de configuaration.
     * @param configStoreAttributsDTO {@link ConfigStoreAttributsDTO}
     * @return {@link ConfigStoreAttributsDTO}
     */
    @Override
    public ConfigStoreAttributsDTO createConfigStoreAttribut(ConfigStoreAttributsDTO configStoreAttributsDTO) {
        //mapper les données du DTO vers l'entité ConfigStoreAttributs avant de les sauvegarder
        ConfigStoreAttributs csaToCreate = configStoreAttributsMapper.toEntity(configStoreAttributsDTO);
        // Enregistrez le nouveau configStoreAttributs dans le repository
        ConfigStoreAttributs csa = configStoreAttributsRepository.save(csaToCreate);
        // Convertir et renvoyer le ConfigstoreAttributs créé
        return configStoreAttributsMapper.toDTO(csa);
    }

    /**
     * Modifier un attribut de configuaration.
     * @param configStoreAttributsDTO {@link ConfigStoreAttributsDTO}
     * @return {@link ConfigStoreAttributsDTO}
     */
    @Override
    public ConfigStoreAttributsDTO updateConfigStoreAttribut(ConfigStoreAttributsDTO configStoreAttributsDTO) {
        ConfigStoreAttributs csaToUpdate = configStoreAttributsMapper.toEntity(configStoreAttributsDTO);
        ConfigStoreAttributs csa = configStoreAttributsRepository.save(csaToUpdate);
        return configStoreAttributsMapper.toDTO(csa);
    }

    /**
     * Retourne la liste de tous les attributs.
     * @return List<ConfigStoreAttributsDTO>
     */
    @Override
    public List<ConfigStoreAttributsDTO> findAllConfigStoresAttributs() {
        return configStoreAttributsMapper.toDTO(configStoreAttributsRepository.findAll());
    }

    /**
     * Trouve et retourne un attribut de configuration par son id.
     * @param id {@link UUID}
     * @return {@link ConfigStoreAttributsDTO}
     */
    @Override
    public ConfigStoreAttributsDTO findConfigStoreAttributById(UUID id) {
        Optional<ConfigStoreAttributs> configStoreAttributsOptional = configStoreAttributsRepository.findById(id);
        if (configStoreAttributsOptional.isPresent()) {
            return configStoreAttributsMapper.toDTO(configStoreAttributsOptional.get());
        } else throw new ConfigStoreAttributsNotfoundException();
    }

    /**
     * Supprimer un attribut de configuration.
     * @param id {@link UUID}
     */
    @Override
    public void deleteConfigStoreAttribut(UUID id) {
        configStoreAttributsRepository.deleteById(id);
    }

    /**
     * Vérifie si un attribut existe.
     * @param id {@link UUID}
     * @return true si l'attribut existe, sinon false.
     */
    @Override
    public boolean configStoreAttributsExist(UUID id) {
        return configStoreAttributsRepository.existsById(id);
    }

    /**
     * Retourne la liste des attributs de configuration par store.
     * @param id {@link UUID}
     * @return List<ConfigStoreAttributsDTO>
     */
    @Override
    public List<ConfigStoreAttributsDTO> findConfigStoreAttributsByStore(UUID id) {
        List<ConfigStoreAttributsDTO> Allcsa = this.findAllConfigStoresAttributs();
        List<ConfigStoreAttributsDTO> csaByStore = new ArrayList<>();
        for (ConfigStoreAttributsDTO configStoreAttributsDTO : Allcsa) {
            if (configStoreAttributsDTO != null) {
                if (configStoreAttributsDTO.getConfigStore() != null) {
                    if (configStoreAttributsDTO.getConfigStore().getStore().getId().equals(id)) {
                        csaByStore.add(configStoreAttributsDTO);
                    }
                }
            }
        }
        return csaByStore;
    }

    /**
     * Trouve et retourne une liste d'attributs de configuration de store pour une méthode spécifique.
     * @param storeId {@link UUID}
     * @param configMethode {@link ConfigMethode}
     * @return List<ConfigStoreAttributsDTO>
     */
    @Override
    public List<ConfigStoreAttributsDTO> findConfigStoreAttributsByConfigMethode(UUID storeId, ConfigMethode configMethode) {
        if (configMethode == null) {
            throw new IllegalArgumentException("ConfigMethode cannot be null");
        } else {
            List<ConfigStoreAttributsDTO> configStoreAttributsDTOList = findConfigStoreAttributsByStore(storeId);
            List<ConfigStoreAttributsDTO> configStoreAttributsDTOByMethode = new ArrayList<>();
            for (ConfigStoreAttributsDTO configStoreAttributsDTO : configStoreAttributsDTOList) {
                if(configStoreAttributsDTO.getConfigMethode() != null){
                    if (configStoreAttributsDTO.getConfigMethode().equals(configMethode)) {
                        configStoreAttributsDTOByMethode.add(configStoreAttributsDTO);
                    }
                }
            }
            return configStoreAttributsDTOByMethode;
        }

    }

    /**
     * Récupère et retourne les noms des attributs d'une classe donnée.
     *
     * @param classe la classe dont on souhaite obtenir les noms des attributs.
     * @return une liste contenant les noms des attributs de la classe spécifiée.
     */
    @Override
    public List<String> getKeys(Class<?> classe) {
        List<String> keys = new ArrayList<>();
        Field[] fields = classe.getDeclaredFields();
        for (Field field : fields) {
            keys.add(field.getName());
        }
        return keys;
    }


    /**
     * Récupère et retourne les attributs de pagination de l'API configurés pour un store donné.
     *
     * @param store {@link Store}
     * @return ConfigAttributsApiPaginee - contenant les attributs de pagination configurés.
     */
    @Override
    public ConfigAttributsApiPaginee getConfigAttributsApiPaginee(Store store) {
        ConfigAttributsApiPaginee configAttributsApiPaginee = new ConfigAttributsApiPaginee();
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("totalElements")) {
                configAttributsApiPaginee.setTotalElements(configStoreAttributs.getAttributeValue());
            }
            if (configStoreAttributs.getAttributeKey().equals("pageSize")) {
                configAttributsApiPaginee.setPageSize(configStoreAttributs.getAttributeValue());
            }
            if (configStoreAttributs.getAttributeKey().equals("pageNumber")) {
                configAttributsApiPaginee.setPageNumber(configStoreAttributs.getAttributeValue());
            }
        }
        return configAttributsApiPaginee;
    }

    /**
     * Récupère les attributs de configuration nécessaires pour la récupération de la quantité d'un produit.
     *
     * @param store {@link Store}
     * @return ConfigAttributsQuantiteProduit - La configuration des attributs de quantité du produit.
     */
    @Override
    public ConfigAttributsQuantiteProduit configStoreAttributsQuantiteProduit(Store store) {
        ConfigAttributsQuantiteProduit configAttributsQuantiteProduit = new ConfigAttributsQuantiteProduit();
        List<ConfigStoreAttributs> configStoreAttributsList = store.getConfigStore().getConfigStoreAttributs();
        for (ConfigStoreAttributs configStoreAttributs : configStoreAttributsList) {
            if (configStoreAttributs.getAttributeKey().equals("quantity")) {
                configAttributsQuantiteProduit.setQuantity(configStoreAttributs.getAttributeValue());
            }
            if (configStoreAttributs.getAttributeKey().equals("id")) {
                configAttributsQuantiteProduit.setIdProduit(configStoreAttributs.getAttributeValue());
            }
        }
        return configAttributsQuantiteProduit;
    }
}
