package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.GraduateAdmissionsController;
import org.example.models.ApplicantPersonalInfo;
import org.example.models.ApplicationFile;
import org.example.models.Professor;
import org.example.repositories.ApplicationFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class GraduateAdmissionsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationFileRepository applicationFileRepo;

    @Autowired
    private GraduateAdmissionsController controller;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        applicationFileRepo.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    // Test GET /api/applications (empty initially)
    @Test
    void getEmptyApplicationListTest() throws Exception {
        this.mockMvc.perform(get("/api/applications"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    // Test POST /api/applications (create new application)
    @Test
    void createApplicationTest() throws Exception {
        Professor professor = new Professor();
        professor.setFirstName("Babak");
        professor.setLastName("Esfandiari");
        professor.setEmail("babakesfandiari@carleton");

        List<Professor> professors = new ArrayList<>();
        professors.add(professor);
        ApplicantPersonalInfo applicantPersonalInfo = new ApplicantPersonalInfo("Some", "Applicant", "email", "123-456-7890", "0 Carlton St.");

        ApplicationFile applicationFile = new ApplicationFile();
        applicationFile.setFieldOfResearch("Scalability");
        applicationFile.setPersonalInfo(applicantPersonalInfo);
        applicationFile.setProfessors(professors);

        String json = objectMapper.writeValueAsString(applicationFile);

        this.mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

}
