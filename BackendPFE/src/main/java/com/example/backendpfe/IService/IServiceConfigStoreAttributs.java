package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.ConfigAttributsApiPaginee;
import com.example.backendpfe.DTO.ConfigAttributsQuantiteProduit;
import com.example.backendpfe.DTO.ConfigStoreAttributsDTO;
import com.example.backendpfe.entities.typeEnum.ConfigMethode;
import com.example.backendpfe.entities.Store;

import java.util.List;
import java.util.UUID;

public interface IServiceConfigStoreAttributs{
    public ConfigStoreAttributsDTO createConfigStoreAttribut(ConfigStoreAttributsDTO dto);
    public List<ConfigStoreAttributsDTO> findAllConfigStoresAttributs();
    public ConfigStoreAttributsDTO findConfigStoreAttributById(UUID id);
    public ConfigStoreAttributsDTO updateConfigStoreAttribut(ConfigStoreAttributsDTO dto);
    public void deleteConfigStoreAttribut(UUID id);
    public boolean configStoreAttributsExist(UUID id);
    public List<ConfigStoreAttributsDTO> findConfigStoreAttributsByStore(UUID id);
    public List<ConfigStoreAttributsDTO> findConfigStoreAttributsByConfigMethode(UUID storeId, ConfigMethode configMethode);
    public List<String> getKeys(Class<?> classe);
    public ConfigAttributsApiPaginee getConfigAttributsApiPaginee(Store store);
    public ConfigAttributsQuantiteProduit configStoreAttributsQuantiteProduit(Store store);
}
