package com.example.auth;

import com.example.auth.IServiceUser;
import com.example.auth.entities.Role;
import com.example.auth.entities.User;
import com.example.auth.exception.UserNotfoundException;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceUser implements IServiceUser {
    @Autowired
    UserRepository clientRepository;
    @Override
    public User createClient(User c) {
        return clientRepository.save(c);
    }

    @Override
    public User findClientById(UUID id) {
        return clientRepository.findById(id).get();
    }

    @Override
    public User updateClient(User c) {
        return clientRepository.save(c);
    }

    @Override
    public List<User> findAllUsers() {
        return clientRepository.findByRole(Role.USER);
    }

    @Override
    public List<User> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
    }

    @Override
    public boolean clientExist(UUID id) {
        return clientRepository.existsById(id);
    }

    @Override
    public User findByEmail(String email) {
        List<User> users = this.findAllClients();
       for (User user : users){
           if (user.getEmail().equals(email)){
               return  user;
           }
       }
       return null;
    }

    @Override
    public long nbreClients() {
        return clientRepository.findByRole(Role.USER).size();
    }

    @Override
    public List<User> filterUser(String prenom) {
        prenom = prenom.toLowerCase();

        List<User> listUsers = findAllUsers();
        List<User> userFitred = new ArrayList<>();
        for(User user:listUsers){
            String userPrenom = user.getPrenom().toLowerCase();
            if (userPrenom.contains(prenom)) {
                userFitred.add(user);
            }
        }
        return userFitred;
    }

}
