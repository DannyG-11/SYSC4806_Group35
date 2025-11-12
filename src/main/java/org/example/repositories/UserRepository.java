package org.example.repositories;

import org.example.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User findById(long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}