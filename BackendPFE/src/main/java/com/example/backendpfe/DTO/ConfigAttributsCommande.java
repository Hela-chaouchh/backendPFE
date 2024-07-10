package com.example.backendpfe.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigAttributsCommande {
    private String montant = "";
    private String detailsCommandes = "";
    private String details_commande_IdProduit = "";
    private String details_commande_PrixUnitaire = "";
    private String details_commande_quantity = "";
    private String userIdAttributeToken = "";
    private String userIdAttributeProfile = "";
    private String userIdCommande = "";
    private String dateCommande = "";
    private String email = "";
    private String password = "";



    /**
     * Méthode pour mapper les attributs en fonction de la clé
     * @param key {@link String}
     * @param value {@link String}
     * @throws NoSuchFieldException {@link NoSuchFieldException}
     * @throws IllegalAccessException
     */
    public void setAttributeValueByKey(String key, String value) {
        try {
            Field field = getClass().getDeclaredField(key);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Si la clé n'est pas trouvée ou si l'accès est refusé, définissez la valeur de l'attribut sur null
            e.printStackTrace(); // Gérer les exceptions si nécessaire
            // Définir la valeur de l'attribut sur null
            setAttributeValueNull(key);
        }
    }

    /**
     * Méthode pour définir la valeur de l'attribut sur null
     *
     * @param key {@link String}
     */
    private void setAttributeValueNull(String key) {
        try {
            Field field = getClass().getDeclaredField(key);
            field.setAccessible(true);
            field.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace(); // Gérer les exceptions si nécessaire
        }
    }

}
