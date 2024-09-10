package dev.fernando.dscatalog.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.fernando.dscatalog.entities.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long>{
    @Query("select obj from PasswordRecover obj where obj.token = :token and obj.expiration > :now")
    List<PasswordRecover> searchValidTokens(String token, Instant now);
}
