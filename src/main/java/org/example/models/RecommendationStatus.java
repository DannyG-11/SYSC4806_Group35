package org.example.models;

public enum RecommendationStatus {
    PENDING("pending recommendation"),
    NOT_RECOMMENDED("don’t recommend for admission"),
    RECOMMENDED_NO_SUPERVISION("recommend but not interested in supervision"),
    RECOMMENDED_NO_FUNDING("recommend but no funding"),
    RECOMMENDED_WITH_FUNDING("recommend and yes to funding");

    private String label;

    RecommendationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}

