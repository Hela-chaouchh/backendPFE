package com.example.backendpfe.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
}