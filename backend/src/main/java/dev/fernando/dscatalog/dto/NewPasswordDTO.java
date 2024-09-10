package dev.fernando.dscatalog.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public class NewPasswordDTO {
    @NotBlank
    private String token;
    @NotBlank
    @Length(min = 8, message = "Senha deve conter no mínimo 8 caracteres!")
    private String password;
    public NewPasswordDTO() {
    }
    public NewPasswordDTO(@NotBlank String token,
            @NotBlank @Length(min = 8, message = "Senha deve conter no mínimo 8 caracteres!") String password) {
        this.token = token;
        this.password = password;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
