package com.example.backendpfe.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "auth-microservice")
public interface ServiceFeignUser {

    @GetMapping("/api/client/")
    List<User> getAllClients();

    @GetMapping("/api/client/findClientById/{id}")
    User getClientById(@PathVariable UUID id);

}
