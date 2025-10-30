package org.example;

import org.example.models.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationFileTest {

    private ApplicationFile application;

    @BeforeAll
    public void setUp() {
        // Setup arguments for ApplicationFile
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@cmail.carleton.ca";
        String phoneNumber = "1234567890";
        String address = "1125 Colonel By Drive, Ottawa, Ontario";
        ApplicantPersonalInfo applicantPersonalInfo = new ApplicantPersonalInfo(
                firstName, lastName, email, phoneNumber, address
        );

        String fieldOfResearch = "Biomed";
        List<Professor> professors = new ArrayList<>();
        List<Document> documents = new ArrayList<>();

        application = new ApplicationFile(
                applicantPersonalInfo, fieldOfResearch, professors, documents);
    }

    @AfterAll
    public void tearDown() {
        application = null;
    }

    @Test
    public void setAndGetId() {
        application.setId(1L);
        assertEquals(1L, application.getId());
    }

    @Test
    public void getPersonalInfo() {
        assertNotNull(application.getPersonalInfo());
        assertInstanceOf(ApplicantPersonalInfo.class, application.getPersonalInfo());
    }

    @Test
    public void setAndGetPersonalInfo() {
        ApplicantPersonalInfo newPersonalInfo = new ApplicantPersonalInfo();
        newPersonalInfo.setId(2L);
        application.setPersonalInfo(newPersonalInfo);

        assertEquals(2L, application.getPersonalInfo().getId());
    }

    @Test
    public void setAndGetDocuments() {
        assertNotNull(application.getDocuments());
        assertInstanceOf(List.class, application.getDocuments());

        List<Document> testDocuments = new ArrayList<>();
        testDocuments.add(new Document());
        testDocuments.get(0).setId(5L);
        application.setDocuments(testDocuments);

        assertEquals(5L, application.getDocuments().get(0).getId());
    }

    @Test
    public void setAndGetProfessors() {
        assertNotNull(application.getProfessors());
        assertInstanceOf(List.class, application.getProfessors());

        List<Professor> testProfs = new ArrayList<>();
        testProfs.add(new Professor());
        testProfs.get(0).setId(5L);
        application.setProfessors(testProfs);

        assertEquals(5L, application.getProfessors().get(0).getId());
    }

    @Test
    public void setAndGetFieldOfResearch() {
        application.setFieldOfResearch("Biomed");
        assertEquals("Biomed", application.getFieldOfResearch());
    }

    @Test
    public void isAvailableToProfs() {
        application.setAvailableToProfs(true);
        assertTrue(application.isAvailableToProfs());
        application.setAvailableToProfs(false);
        assertFalse(application.isAvailableToProfs());
    }

    @Test
    public void setAndGetEvaluations() {
        List<ProfessorEvaluation> evaluations = new ArrayList<>();
        evaluations.add(new ProfessorEvaluation());
        evaluations.get(0).setId(6L);
        application.setEvaluations(evaluations);

        assertEquals(6L, application.getEvaluations().get(0).getId());
    }

    @Test
    public void addProfessorEvaluation() {
        ProfessorEvaluation professorEvaluation = new ProfessorEvaluation();
        int initialLength = application.getEvaluations().size();
        application.addProfessorEvaluation(professorEvaluation);
        int finalLength = application.getEvaluations().size();

        assertEquals(initialLength + 1, finalLength);
    }
}
