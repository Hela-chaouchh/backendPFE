package com.example.backendpfe.mapper;

import com.example.backendpfe.DTO.StoreDTO;
import com.example.backendpfe.entities.Store;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")

public interface StoreMapper {
    Store toEntity(StoreDTO dto);
    StoreDTO toDTO(Store entity);
    List<StoreDTO> toDTO(List<Store> stores);
}

