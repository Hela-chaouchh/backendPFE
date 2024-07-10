package com.example.backendpfe.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreIdNomDTO {
    private UUID id;
    private String nom;
}
