package com.example.auth;

import com.example.auth.entities.User;

import java.util.List;
import java.util.UUID;

public interface IServiceUser {
    public User createClient(User c);
    public User findClientById(UUID id);
    public User updateClient(User c);
    public List<User> findAllClients();
    public List<User> findAllUsers();
    public void deleteClient(UUID id);
    public boolean clientExist(UUID id);
    public User findByEmail(String email);
    public long nbreClients();
    public List<User> filterUser(String prenom);
}
