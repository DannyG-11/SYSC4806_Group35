# SYSC 4806 - Graduate Admissions System (Group 35)

A web-based graduate admissions management system built with Spring Boot and Thymeleaf for managing student applications, professor evaluations, and administrative oversight.

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
│   │   │   └── Main.java             # Application entry point
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
│   │       │   ├── css/              # Stylesheets
│   │       │   └── js/               # JavaScript files
│   │       └── application.properties # Configuration
│   └── test/
│       └── java/org/example/         # Unit tests
├── diagrams/                         # UML and database diagrams
│   ├── M1-Database-Scema-Diagram.mmd
│   └── M1-UML-Class-Diagram.mmd
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## Getting Started

### Prerequisites

- **Java 21** (JDK)
- **Maven 3.6+**

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

## Application Endpoints

| Endpoint | Description |
|----------|-------------|
| `/` | Home page |
| `/apply` | Application submission form |
| `/applications` | View submitted applications |
| `/professor` | Professor dashboard |
| `/professors` | Manage professors |
| `/evaluations` | View evaluations |
| `/admin` | Administrator dashboard |
| `/h2-console` | H2 database console |

## Team

Group 35 - SYSC 4806A Software Engineering Lab
- Amr Abdel-Rahman 101195243
- Colin Chen 101229162
- Daniel Gaudet
- David Exinor
- Sebi Magyar-Samoila

## License

This project is developed for academic purposes as part of the SYSC 4806 course.

