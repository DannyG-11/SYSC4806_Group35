package org.example;

import org.assertj.core.api.Assertions;
import org.example.models.ApplicantPersonalInfo;
import org.example.models.ApplicationFile;
import org.example.models.Professor;
import org.example.repositories.ApplicationFileRepository;
import org.example.repositories.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class RepositoryTests {

    @Autowired
    private ApplicationFileRepository applicationFileRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    @Test
    public void testAssignFetchProfessor() {
        Professor professor = new Professor();
        professor.setFirstName("Babak");
        professor.setLastName("Esfandiari");
        professor.setEmail("babakesfandiari@carleton");
        professorRepository.save(professor);

        List<Professor> professors = new ArrayList<>();
        professors.add(professor);
        ApplicantPersonalInfo applicantPersonalInfo = new ApplicantPersonalInfo("Some", "Applicant", "email", "123-456-7890", "0 Carlton St.");

        ApplicationFile applicationFile = new ApplicationFile();
        applicationFile.setFieldOfResearch("Scalability");
        applicationFile.setPersonalInfo(applicantPersonalInfo);
        applicationFile.setProfessors(professors);
        applicationFileRepository.save(applicationFile);

        List<ApplicationFile> queriedProfessorApplicationFiles = applicationFileRepository.findByProfessors_Id(professor.getId());

        Assertions.assertThat(queriedProfessorApplicationFiles.size()).isEqualTo(1);
    }
}
