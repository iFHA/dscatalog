package dev.fernando.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fernando.dscatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
