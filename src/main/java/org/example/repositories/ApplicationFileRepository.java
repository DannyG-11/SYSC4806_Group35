package org.example.repositories;

import org.example.models.ApplicationFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationFileRepository extends CrudRepository<ApplicationFile, Long> {
    ApplicationFile findById(long id);

    @Query("select a from ApplicationFile a join a.professors p where p.id = :professorId")
    List<ApplicationFile> findByProfessorId(@Param("professorId") Long professorId);

}