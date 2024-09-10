package dev.fernando.dscatalog.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fernando.dscatalog.dto.NewPasswordDTO;
import dev.fernando.dscatalog.dto.RecoverTokenDTO;
import dev.fernando.dscatalog.services.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("auth")
public class AuthResource {

    private final AuthService authService;
    public AuthResource(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("recover-token")
    public ResponseEntity<Void> recoverToken(@RequestBody @Valid RecoverTokenDTO dto) {
        this.authService.createRecoverToken(dto);
        
        return ResponseEntity.noContent().build();
    }
    @PutMapping("new-password")
    public ResponseEntity<Void> newPassword(@RequestBody @Valid NewPasswordDTO dto) {
        this.authService.saveNewPassword(dto);
        
        return ResponseEntity.noContent().build();
    }
    
}
