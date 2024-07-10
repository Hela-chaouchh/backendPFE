package com.example.backendpfe.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigAttributsApiPaginee {
    String totalElements = "";
    String pageSize = "";
    String pageNumber = "";
}
