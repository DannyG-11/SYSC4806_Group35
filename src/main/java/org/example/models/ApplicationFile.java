package org.example.models;

import jakarta.persistence.*;

import java.util.List;

import static org.example.models.RecommendationStatus.PENDING;

@Entity
public class ApplicationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private ApplicantPersonalInfo personalInfo;
    private String fieldOfResearch;

    @ManyToMany
    private List<Professor> professors;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id")
    private List<Document> documents;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfessorEvaluation> evaluations;

    private RecommendationStatus status;

    /**
     * The applicant files can be filtered/selected by an administrator. Those that are considered good enough are
     * routed to profs for evaluation by a deadline.
     * If true, it has been selected by an administrator for evaluation.
     */
    private boolean availableToProfs;

    public ApplicationFile() {
        status = PENDING;
    }

    public ApplicationFile(ApplicantPersonalInfo personalInfo, String fieldOfResearch, List<Professor> professors, List<Document> documents) {
        this.personalInfo = personalInfo;
        this.fieldOfResearch = fieldOfResearch;
        this.professors = professors;
        this.documents = documents;
        status = PENDING;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ApplicantPersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(ApplicantPersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public String getFieldOfResearch() {
        return fieldOfResearch;
    }

    public void setFieldOfResearch(String fieldOfResearch) {
        this.fieldOfResearch = fieldOfResearch;
    }

    public boolean isAvailableToProfs() {
        return availableToProfs;
    }

    public void setAvailableToProfs(boolean availableToProfs) {
        this.availableToProfs = availableToProfs;
    }

    public List<ProfessorEvaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<ProfessorEvaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public void addProfessorEvaluation(ProfessorEvaluation evaluation) {
        this.evaluations.add(evaluation);
    }

    public RecommendationStatus getStatus() {
        return status;
    }
    public void setStatus(RecommendationStatus status) {
        this.status = status;
    }
}
