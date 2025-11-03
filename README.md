# SYSC 4806 - Graduate Admissions System (Group 35)

A web-based graduate admissions management system built with Spring Boot and Thymeleaf for managing student applications, professor evaluations, and administrative oversight.

### ✅ Milestone 1 - Sprint 1 Progress (Due: November 3)

**Completed Features:**
- ✅ Project setup with Spring Boot and Maven
- ✅ Base entity models created (ApplicationFile, ApplicantPersonalInfo, Professor, Document, ProfessorEvaluation, RecommendationStatus)
- ✅ JPA repositories implemented
- ✅ REST API endpoints for applications and professors
- ✅ CI/CD pipeline with Azure Web Apps
- ✅ Unit tests for all models and repositories
- ✅ Working application deployed to Azure
- ✅ One operational use case: application submission with data collection from backend


### 📋 Next Sprint Plan - Sprint 2 (November 4-17)

**Planned Features:**
- Implement professor dashboard to view assigned applications
- Add evaluation submission workflow for professors
- Complete the applications viewing page with full details
- Add professor assignment functionality for admins
- Implement deadline tracking for evaluations
- Enhance admin dashboard with filtering and management capabilities

**Goals:**
- Make the system "somewhat usable" with several related features
- Ensure users can perform reasonably useful tasks
- No dangling links to non-implemented features

## UML Class Diagrams

The models are documented in the `/diagrams` folder:
- **UML Class Diagram**: [diagrams/M1-UML-Class-Diagram.mmd](diagrams/M1-UML-Class-Diagram.mmd)
- **Database Schema Diagram**: [diagrams/M1-Database-Scema-Diagram.mmd](diagrams/M1-Database-Scema-Diagram.mmd)

These diagrams are kept in sync with the code and updated as features are added.

## Database Schema

### Entity Relationship Diagram

```
┌─────────────────────────────┐
│ APPLICANT_PERSONAL_INFO     │
├─────────────────────────────┤
│ PK: bigint ID               │
│     string FIRST_NAME       │
│     string LAST_NAME        │
│     string EMAIL            │
│     string PHONE_NUMBER     │
│     string ADDRESS          │
└────────────┬────────────────┘
             │ 1:N
             │
┌────────────▼────────────────┐
│ APPLICATION_FILE             │
├─────────────────────────────┤
│ PK: bigint ID               │
│ FK: bigint PERSONAL_INFO_ID │
│     string FIELD_OF_RESEARCH│
│     boolean AVAILABLE_TO_PROF│
│     enum STATUS             │
└────┬─────────────┬──────────┘
     │ 1:N         │ M:N
     │             │
┌────▼────┐        │      ┌─────────────┐
│ DOCUMENT│        └──────│  PROFESSOR  │
├─────────┤        M:N    ├─────────────┤
│PK: ID   │◄──────────────│ PK: ID      │
│   TITLE │               │   FIRSTNAME │
│   LINK  │               │   LASTNAME  │
└─────────┘               │   EMAIL     │
     │                    └─────────────┘
     │                         │
     │                    ┌────▼────────────────┐
     │                    │ PROFESSOR_EVALUATION│
     │                    ├─────────────────────┤
     └────────────────────┤ PK: ID             │
                    1:N   │ FK: APPLICATION_ID  │
                          │ FK: PROFESSOR_ID    │
                          │     COMMENTS        │
                          │     RECOMMENDATION  │
                          └─────────────────────┘
```

### Key Relationships

1. **ApplicantPersonalInfo → ApplicationFile** (1:N): One applicant can have multiple applications
2. **ApplicationFile → Document** (1:N): Each application can have multiple documents
3. **ApplicationFile → Professor** (M:N): Applications can target multiple professors
4. **ApplicationFile → ProfessorEvaluation** (1:N): Multiple professors can evaluate one application
5. **Professor → ProfessorEvaluation** (1:N): One professor can write multiple evaluations

### RecommendationStatus Enum

- `PENDING` - Application pending review
- `NOT_RECOMMENDED` - Not recommended for admission
- `RECOMMENDED_NO_SUPERVISION` - Recommended but professor not interested in supervision
- `RECOMMENDED_NO_FUNDING` - Recommended but no funding available
- `RECOMMENDED_WITH_FUNDING` - Recommended with funding

## Features

### For Applicants
- Submit graduate school applications with personal information
- Upload supporting documents
- Specify field of research interests

### For Professors
- View assigned applications for evaluation
- Provide evaluations and recommendations
- Track evaluation status

### For Administrators
- Review submitted applications
- Assign applications to professors
- Manage application workflow
- Monitor application status

## Project Structure

```
SYSC4806_Group35/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── controllers/          # MVC controllers
│   │   │   │   ├── GraduateAdmissionsController.java
│   │   │   │   ├── GraduateAdmissionsViewController.java
│   │   │   │   └── ProfessorController.java
│   │   │   ├── models/               # Entity classes
│   │   │   │   ├── ApplicantPersonalInfo.java
│   │   │   │   ├── ApplicationFile.java
│   │   │   │   ├── Document.java
│   │   │   │   ├── Professor.java
│   │   │   │   ├── ProfessorEvaluation.java
│   │   │   │   └── RecommendationStatus.java
│   │   │   ├── repositories/         # JPA repositories
│   │   │   │   ├── ApplicationFileRepository.java
│   │   │   │   └── ProfessorRepository.java
│   │   │   └── Main.java            # Application entry point
│   │   └── resources/
│   │       ├── templates/            # Thymeleaf HTML templates
│   │       │   ├── index.html        # Home page
│   │       │   ├── apply.html        # Application form
│   │       │   ├── applications.html # View applications
│   │       │   ├── professor.html    # Professor dashboard
│   │       │   ├── professors.html   # Professor management
│   │       │   ├── admin.html        # Admin dashboard
│   │       │   └── evaluations.html  # Evaluation management
│   │       ├── static/               # Static assets
│   │       │   ├── css/             # Stylesheets
│   │       │   └── js/              # JavaScript files
│   │       └── application.properties # Configuration
│   └── test/
│       └── java/org/example/         # Unit tests
├── diagrams/                          # UML and database diagrams
│   ├── M1-Database-Scema-Diagram.mmd
│   └── M1-UML-Class-Diagram.mmd
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## Getting Started

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/DannyG-11/SYSC4806_Group35.git
   cd SYSC4806_Group35
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the application:
   - Navigate to `http://localhost:8080` in your web browser
   - H2 Console: `http://localhost:8080/h2-console`

### Running Tests

```bash
mvn test
```

## Team

Group 35 - SYSC 4806A Software Engineering Lab
- Amr Abdel-Rahman 101195243
- Colin Chen 
- Daniel Gaudet
- David Exinor
- Sebi Magyar-Samoila

## License

This project is developed for academic purposes as part of the SYSC 4806 course.

