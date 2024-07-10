package com.example.backendpfe.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigAttributsParamsApiPageable {
    private String pageSize = "";
    private String pageNumber = "";
}
