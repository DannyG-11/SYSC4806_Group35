package org.example.repositories;

import org.example.models.ApplicantPersonalInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicantPersonalInfoRepository extends CrudRepository<ApplicantPersonalInfo, Long> {

    ApplicantPersonalInfo findById(long id);

    Optional<ApplicantPersonalInfo> findByEmail(String email);
}