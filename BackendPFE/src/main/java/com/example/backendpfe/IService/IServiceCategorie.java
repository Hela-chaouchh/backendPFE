package com.example.backendpfe.IService;

import com.example.backendpfe.DTO.*;
import com.example.backendpfe.entities.Categorie;

import java.util.List;
import java.util.UUID;

public interface IServiceCategorie {
    public CategorieDTO createCategorie(CategorieDTO dto);

    public CategorieDTO updateCategorie(CategorieDTO dto);

    public CategorieDTO findCategorieById(UUID id);

    public List<CategorieDTO> findAllCategories();

    public void deleteCategorie(UUID id);

    public boolean CategorieExist(UUID id);
    public List<Categorie2DTO> getCategoriesByStoreApiNonPaginee(UUID storeId);

    void deleteAllCategoriesByStore(UUID id);

    public Categorie findCategorieByNom(String nom);

    public List<ProduitDTO> listProduitsParCategorie(String categorie, UUID storeId);

    Categorie findCategorieByStoreIdAndNom(UUID storeId, String categoryName);
    public List<Categorie2DTO> getCategoriesByStoreApiPaginee(UUID storeId);
    public List<Categorie2DTO> getCategoriesByStore(UUID storeId);
}
