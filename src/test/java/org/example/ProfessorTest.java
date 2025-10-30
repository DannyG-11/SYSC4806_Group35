package org.example;

import org.example.models.Professor;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfessorTest {

    private Professor testProfessor;

    @BeforeAll
    public void setUp() {
        testProfessor = new Professor("Babak", "E", "babakE@sce.carleton.ca");
    }

    @AfterAll
    public void tearDown() {
        testProfessor = null;
    }

    @Test
    public void setAndGetId() {
        testProfessor.setId(9L);
        assertEquals(9L, testProfessor.getId());
    }

    @Test
    public void setAndGetFirstName() {
        testProfessor.setFirstName("Bob");
        assertEquals("Bob", testProfessor.getFirstName());
    }

    @Test
    public void setAndGetLastName() {
        testProfessor.setLastName("Q");
        assertEquals("Q", testProfessor.getLastName());
    }

    @Test
    public void setAndGetEmail() {
        testProfessor.setEmail("babak@cmail.ca");
        assertEquals("babak@cmail.ca", testProfessor.getEmail());
    }
}
