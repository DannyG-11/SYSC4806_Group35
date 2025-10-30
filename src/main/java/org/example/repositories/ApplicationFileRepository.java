package org.example.repositories;

import org.example.models.ApplicationFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationFileRepository extends CrudRepository<ApplicationFile, Long> {
    ApplicationFile findById(long id);
    List<ApplicationFile> findByProfessors_Id(Long professorId);

}