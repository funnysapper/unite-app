package com.Unite.UniteMobileApp.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ContactDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String userName;

}