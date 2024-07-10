package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.StoreIdNomDTO;
import com.example.backendpfe.entities.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreIdNomMapper {
    Store toEntity(StoreIdNomDTO dto);
    StoreIdNomDTO toDTO(Store entity);
    List<StoreIdNomDTO> toDTO(List<Store> stores);
}
