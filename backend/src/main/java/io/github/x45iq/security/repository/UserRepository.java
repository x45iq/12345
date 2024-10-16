package io.github.x45iq.security.repository;

import io.github.x45iq.security.models.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserByUsername(String username);
}
