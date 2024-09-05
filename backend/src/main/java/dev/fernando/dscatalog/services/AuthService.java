package dev.fernando.dscatalog.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscatalog.dto.EmailDTO;
import dev.fernando.dscatalog.dto.RecoverTokenDTO;
import dev.fernando.dscatalog.entities.PasswordRecover;
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
}
