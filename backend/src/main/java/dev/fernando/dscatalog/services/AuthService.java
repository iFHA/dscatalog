package dev.fernando.dscatalog.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscatalog.dto.EmailDTO;
import dev.fernando.dscatalog.dto.NewPasswordDTO;
import dev.fernando.dscatalog.dto.RecoverTokenDTO;
import dev.fernando.dscatalog.entities.PasswordRecover;
import dev.fernando.dscatalog.entities.User;
import dev.fernando.dscatalog.repositories.PasswordRecoverRepository;
import dev.fernando.dscatalog.repositories.UserRepository;
import dev.fernando.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {

    @Value("${email.password-recover.token.expiration-in-minutes}")
    private Long tokenExpirationMinutes;
    @Value("${email.password-recover.uri}")
    private String passwordRecoverUri;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String createRecoverToken(RecoverTokenDTO dto) {
        var user = userRepository.findByEmail(dto.email());
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("E-mail %s não encontrado!".formatted(dto.email()));
        }

        PasswordRecover entity = new PasswordRecover(
            null,
            UUID.randomUUID().toString(),
            user.get(),
            Instant.now().plus(tokenExpirationMinutes, ChronoUnit.MINUTES)
        );

        passwordRecoverRepository.save(entity);

        String body = """
                Acesse o link %s para definir uma nova senha
                """.formatted(passwordRecoverUri + entity.getToken());
        emailService.send(new EmailDTO(dto.email(), "Recuperação de senha", body));

        return "";
    }
    
    @Transactional
    public void saveNewPassword(NewPasswordDTO dto) {
        var result = this.passwordRecoverRepository.searchValidTokens(dto.getToken(), Instant.now());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Token inválido!");
        }
        User user = result.get(0).getUser();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }
    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt principal = (Jwt) authentication.getPrincipal();
            String username = principal.getClaim("username");
            return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário %s não encontrado!".formatted(username)));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário inválido! " + e.getMessage());
        }
    }
}
