package com.example.projeck_cuoi_mon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabRoomTypeForm {

    @NotBlank
    @Size(max = 30)
    private String code;

    @NotBlank
    @Size(max = 150)
    private String name;

    private String description;

    private Boolean active = true;
}
