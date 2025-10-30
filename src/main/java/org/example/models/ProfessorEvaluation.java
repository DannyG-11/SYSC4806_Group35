package org.example.models;

import jakarta.persistence.*;

@Entity
public class ProfessorEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    private ApplicationFile applicationFile;

    /**
     * Recommendation status of the application file. A prof may set the recommendation status once it is
     * available to them for the admins to view.
     */
    @Enumerated(EnumType.STRING)
    private RecommendationStatus recommendation;

    private String comments;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public RecommendationStatus getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(RecommendationStatus recommendation) {
        this.recommendation = recommendation;
    }

    public ApplicationFile getApplicationFile() {
        return applicationFile;
    }

    public void setApplicationFile(ApplicationFile applicationFile) {
        this.applicationFile = applicationFile;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}