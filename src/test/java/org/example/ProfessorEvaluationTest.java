package org.example;

import org.example.models.ApplicationFile;
import org.example.models.Professor;
import org.example.models.ProfessorEvaluation;
import org.example.models.ApplicationStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfessorEvaluationTest {

    private ProfessorEvaluation testEval;

    @BeforeAll
    public void setUp() {
        testEval = new ProfessorEvaluation();
    }

    @AfterAll
    public void tearDown() {
        testEval = null;
    }

    @Test
    public void setAndGetId() {
        testEval.setId(3L);
        assertEquals(3L, testEval.getId());
    }

    @Test
    public void setAndGetComments() {
        testEval.setComments("comments");
        assertEquals("comments", testEval.getComments());
    }

    @Test
    public void setAndGetRecommendation() {
        testEval.setRecommendation(ApplicationStatus.RECOMMENDED_NO_FUNDING);
        assertEquals(ApplicationStatus.RECOMMENDED_NO_FUNDING, testEval.getRecommendation());
    }

    @Test
    public void setAndGetApplicationFile() {
        ApplicationFile applicationFile = new ApplicationFile();
        applicationFile.setId(4L);
        testEval.setApplicationFile(applicationFile);
        assertEquals(4L, testEval.getApplicationFile().getId());
    }

    @Test
    public void setAndGetProfessor() {
        Professor prof = new Professor();
        prof.setId(3L);
        testEval.setProfessor(prof);
        assertEquals(3L, testEval.getProfessor().getId());
    }
}
