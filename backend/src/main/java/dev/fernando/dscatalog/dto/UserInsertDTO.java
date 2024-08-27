package dev.fernando.dscatalog.dto;

import dev.fernando.dscatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Campo senha obrigatório")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
