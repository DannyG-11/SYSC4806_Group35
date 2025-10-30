package org.example.repositories;

import org.example.models.ApplicationFile;
import org.example.models.Professor;
import org.springframework.data.repository.CrudRepository;

public interface ProfessorRepository extends CrudRepository<Professor, Long> {
    Professor findById(long id);
    Professor findByEmail(String email);
}