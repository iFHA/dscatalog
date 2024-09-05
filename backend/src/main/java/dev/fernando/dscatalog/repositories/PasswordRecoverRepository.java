package dev.fernando.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fernando.dscatalog.entities.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long>{
}
