package com.example.backendpfe.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigAttributsProduit {
    private String id = "";
    private String name = "";
    private String price = "";
    private String remise = "";
    private String quantity = "";
    private String image = "";
    private String categorie = "";



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
     * @param key {@link String}
     * @throws NoSuchFieldException {@link NoSuchFieldException}
     * @throws IllegalAccessException
     */
    private void setAttributeValueNull(String key) {
        try {
            Field field = getClass().getDeclaredField(key);
            field.setAccessible(true);
            field.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
