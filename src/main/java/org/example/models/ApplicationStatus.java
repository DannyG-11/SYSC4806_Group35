package org.example.models;

public enum ApplicationStatus {
    NEW("pending admin initial approval"),
    PENDING("pending recommendation"),
    NOT_RECOMMENDED("don’t recommend for admission"),
    RECOMMENDED_NO_SUPERVISION("recommend but not interested in supervision"),
    RECOMMENDED_NO_FUNDING("recommend but no funding"),
    RECOMMENDED_WITH_FUNDING("recommend and yes to funding"),
    ACCEPTED("admin has accepted recommendation"),
    REJECTED("admin has rejected recommendation or application");

    private String label;

    ApplicationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}

