package com.example.projeck_cuoi_mon.dto.response;

import com.example.projeck_cuoi_mon.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class SessionUser implements Serializable {

    private Long id;
    private String email;
    private String fullName;
    private UserRole role;
}
