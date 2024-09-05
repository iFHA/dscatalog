package dev.fernando.dscatalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecoverTokenDTO(
    @NotBlank
    @Email
    String email
) {
}
