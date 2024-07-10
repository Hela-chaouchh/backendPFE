package com.example.backendpfe.DTO;

import com.example.backendpfe.entities.DetailsCommande;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeDTO {
    private UUID id;
    private String dateCommande;
    private double montant;
    private UUID client_id;
    private List<DetailsCommande> detailsCommandes = new ArrayList<>();
}
