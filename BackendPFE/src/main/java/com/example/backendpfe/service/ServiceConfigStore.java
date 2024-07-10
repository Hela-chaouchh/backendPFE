package com.example.backendpfe.service;

import com.example.backendpfe.DTO.ConfigStoreDTO;
import com.example.backendpfe.IService.IServiceConfigStore;
import com.example.backendpfe.entities.ConfigStore;
import com.example.backendpfe.entities.ConfigStoreAttributs;
import com.example.backendpfe.exception.ConfigStoreNotfoundException;
import com.example.backendpfe.mapper.ConfigStoreAttributsMapper;
import com.example.backendpfe.mapper.ConfigStoreMapper;
import com.example.backendpfe.repository.ConfigStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceConfigStore implements IServiceConfigStore {
    @Autowired
    private ConfigStoreMapper configStoreMapper;
    @Autowired
    private ConfigStoreRepository configStoreRepository;
    @Autowired
    private ServiceConfigStoreAttributs serviceConfigStoreAttributs;
    @Autowired
    private ConfigStoreAttributsMapper configStoreAttributsMapper;

    /**
     * Permet de céer une nouvelle configuration pour un store.
     *
     * @param configStoreDTO {@link ConfigStoreDTO}
     * @return {@link ConfigStoreDTO}
     */
    @Override
    public ConfigStoreDTO createConfigStore(ConfigStoreDTO configStoreDTO) {
        ConfigStore configStoreToCreate= configStoreMapper.toEntity(configStoreDTO);
        ConfigStore csCreated =  configStoreRepository.save(configStoreToCreate);
        for (ConfigStoreAttributs csa : csCreated.getConfigStoreAttributs()) {
            csa.setConfigStore(csCreated);
            serviceConfigStoreAttributs.createConfigStoreAttribut(configStoreAttributsMapper.toDTO(csa));
        }
        return configStoreMapper.toDTO(csCreated);
    }

    /**
     * Permet de modifier une configuaration de store.
     * @param configStoreDTO {@link ConfigStoreDTO}
     * @return {@link ConfigStoreDTO}
     */
    @Override
    public ConfigStoreDTO updateConfigStore(ConfigStoreDTO configStoreDTO) {
        ConfigStore configStoreToUpdate= configStoreMapper.toEntity(configStoreDTO);
        ConfigStore updatedconfigStore =  configStoreRepository.save(configStoreToUpdate);
        for (ConfigStoreAttributs csa : updatedconfigStore.getConfigStoreAttributs()) {
            csa.setConfigStore(updatedconfigStore);
            serviceConfigStoreAttributs.updateConfigStoreAttribut(configStoreAttributsMapper.toDTO(csa));
        }
        return configStoreMapper.toDTO(updatedconfigStore);
    }

    /**
     * Retourne la liste de toutes les configurations des stores.
     * @return {@link List<ConfigStoreDTO>}
     */
    @Override
    public List<ConfigStoreDTO> findAllConfigStores() {
        return configStoreMapper.toDTO(configStoreRepository.findAll());
    }

    /**
     * Trouve et retourne une configuration de store par son id.
     * @param id {@link UUID}
     * @return {@link ConfigStoreDTO}
     */
    @Override
    public ConfigStoreDTO findConfigStoreById(UUID id) {
        Optional<ConfigStore> configStoreOptional = configStoreRepository.findById(id);
        if(configStoreOptional.isPresent()){
            return configStoreMapper.toDTO(configStoreOptional.get());
        } else throw new ConfigStoreNotfoundException();
    }


    /**
     * Supprimer une configuration de store.
     * @param id {@link UUID}
     */
    @Override
    public void deleteConfigStore(UUID id) {
        configStoreRepository.deleteById(id);
    }

    /**
     * Vérifie si une configuaration existe.
     * @param id {@link UUID}
     * @return true la configuration existe, sinon false.
     */
    @Override
    public boolean configStoreExist(UUID id) {
        return configStoreRepository.existsById(id);
    }
}
