package com.example.backendpfe.service;

import com.example.backendpfe.DTO.CommandeDTO;
import com.example.backendpfe.DTO.DetailsCommandeDTO;
import com.example.backendpfe.IService.IServiceDetailsCommande;
import com.example.backendpfe.entities.DetailsCommande;
import com.example.backendpfe.exception.DetailsCommandeNotfoundException;
import com.example.backendpfe.mapper.DetailsCommandeMapper;
import com.example.backendpfe.repository.DetailsCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceDetailsCommande implements IServiceDetailsCommande {
    @Autowired
    private DetailsCommandeRepository detailsCommandeRepository;
    @Autowired
    private DetailsCommandeMapper detailsCommandeMapper;

    /**
     * Créer un nouvel détail de commande.
     * @param detailsCommandeDTO {@link DetailsCommandeDTO}
     * @return DetailsCommandeDTO
     */
    @Override
    public DetailsCommandeDTO createDetailsCommande(DetailsCommandeDTO detailsCommandeDTO) {
        //mapper les données du DTO vers l'entité DetailsCommande avant de les sauvegarder
        DetailsCommande detailsCommande = detailsCommandeMapper.toEntity(detailsCommandeDTO);
        // Enregistrez le nouveau DetailsCommande dans le repository
        DetailsCommande dc = detailsCommandeRepository.save(detailsCommande);
        // Convertir et renvoyer le DetailsCommande créé
        return detailsCommandeMapper.toDTO(dc);
    }

    /**
     * Modifier un détail de commande.
     * @param detailsCommandeDTO {@link DetailsCommandeDTO}
     * @return DetailsCommandeDTO
     */
    @Override
    public DetailsCommandeDTO updateDetailsCommande(DetailsCommandeDTO detailsCommandeDTO) {
        DetailsCommande detailsCommande = detailsCommandeMapper.toEntity(detailsCommandeDTO);
        DetailsCommande dc = detailsCommandeRepository.save(detailsCommande);
        return detailsCommandeMapper.toDTO(dc);
    }

    /**
     * Trouve et retourne un détail de commande par son id.
     * @param id {@link UUID}
     * @return DetailsCommandeDTO
     */
    @Override
    public DetailsCommandeDTO findDetailsCommandeById(UUID id) {
        Optional<DetailsCommande> detailsCommandeOptional = detailsCommandeRepository.findById(id);
        if(detailsCommandeOptional.isPresent()){
            return detailsCommandeMapper.toDTO(detailsCommandeOptional.get());
        } else throw new DetailsCommandeNotfoundException();
    }

    /**
     * Retourne la liste de tous les détails des commandes.
     * @return {@link List<DetailsCommandeDTO>}
     */
    @Override
    public List<DetailsCommandeDTO> findAllDetailsCommandes() {
        return detailsCommandeMapper.toDTO(detailsCommandeRepository.findAll());
    }

    /**
     * Supprimer un détail de commande.
     * @param id {@link UUID}
     */
    @Override
    public void supprimerDetailsCommande(UUID id) {
        detailsCommandeRepository.deleteById(id);
    }

    /**
     * Vérifie si un détail existe.
     * @param id {@link UUID}
     * @return true si le détail existe, sinon false.
     */
    @Override
    public boolean DetailscommandeExist(UUID id) {
        return detailsCommandeRepository.existsById(id);
    }

    /**
     *Trouve et retourne la liste des détails d'une commande spécifique.
     * @param commande {@link CommandeDTO}
     * @return List<DetailsCommandeDTO>
     */
    @Override
    public List<DetailsCommandeDTO> findDetailsCommandeByCommande(CommandeDTO commande) {
        return detailsCommandeRepository.findDetailsCommandeByCommande(commande);
    }
}
