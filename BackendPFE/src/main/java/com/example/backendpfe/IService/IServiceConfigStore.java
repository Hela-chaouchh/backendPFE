package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.ConfigStoreDTO;
import com.example.backendpfe.entities.ConfigStore;

import java.util.List;
import java.util.UUID;

public interface IServiceConfigStore {
    public ConfigStoreDTO createConfigStore(ConfigStoreDTO dto);
    public List<ConfigStoreDTO> findAllConfigStores();
    public ConfigStoreDTO findConfigStoreById(UUID id);
    public ConfigStoreDTO updateConfigStore(ConfigStoreDTO dto);
    public void deleteConfigStore(UUID id);
    public boolean configStoreExist(UUID id);
}
