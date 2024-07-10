package com.example.auth;

import com.example.auth.entities.Role;
import com.example.auth.entities.User;
import com.example.auth.exception.UserNotfoundException;
import com.example.auth.IServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "*") 
@RestController
@RequestMapping("/api/client/")
public class UserController {
    @Autowired
    IServiceUser iServiceUser;

    @GetMapping("")
    public List<User> getAllClients(){
        return iServiceUser.findAllClients();
    }

    @GetMapping("users")
    public List<User> getAllUsers(){
        return iServiceUser.findAllUsers();
    }

    @GetMapping("findClientById/{id}")
    public User getParId(@PathVariable UUID id){
        if(!iServiceUser.clientExist(id))throw new UserNotfoundException();
        return iServiceUser.findClientById(id);
    }
    @PostMapping("add")
    public User ajoutClient(@RequestBody User c){
        return iServiceUser.createClient(c);
    }

    @PutMapping("update")
    public User updateClient(@RequestBody User c){
        if(!iServiceUser.clientExist(c.getId()))throw new UserNotfoundException();
        return iServiceUser.updateClient(c);
    }

    @DeleteMapping("delete/{id}")
    public String delete (@PathVariable UUID id)
    {
        if(!iServiceUser.clientExist(id))throw new UserNotfoundException();
        iServiceUser.deleteClient(id);
        return "Client supprimé";
    }

    @GetMapping("findByEmail/{email}")
    public User findByEmail(@PathVariable String email){
        return iServiceUser.findByEmail(email);
    }

    @GetMapping("nbreClients")
    public long nbreClients(){
        return iServiceUser.nbreClients();
    }
    @GetMapping("filter/{prenom}")
    public List<User> rechercheUser(@PathVariable String prenom){
        return iServiceUser.filterUser(prenom);
    }
}
