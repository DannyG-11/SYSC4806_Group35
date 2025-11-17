# SYSC 4806 Team 35: Graduate Admissions Management System

To use the system, access here: https://sysc4806-group35.azurewebsites.net/

Team members: 
- Amr Abdel-Rahman 101195243
- Colin Chen 101229162
- Daniel Gaudet 101231140
- David Exinor 10118298
- Sebi Magyar-Samoila 101223588

# Current Project State:
- Database schema has been created and set up
- CI/CD pipeline is working and deploying to Azure: https://sysc4806-group35.azurewebsites.net/
- Admin page has functionality for new, pending, and evaluated requests.
- Request page allows users to create a new request with filler for documents and assigned professors.
- Professors can view requests assigned to them and make decisions.
- Tests exist for repositories, models, and controllers.
- Users are able to select specific professors out of a list, instead of manually inputting their information.
- Websockets have been implemented to avoid race conditions in admin modifications.
- Separate admin page further for improved UX.
- Users can now login to accounts
- User access is determined by account roles

# Upcoming Sprint:
- Users should be able to upload real documents for submission.
- Users should be able to recieve email notifications when changes are made to applications.
- Finish implementation of accoutn registration

## UML Class Diagrams

The models are documented in the `/diagrams` folder:
- **UML Class Diagram**: [diagrams/M1-UML-Class-Diagram.mmd](diagrams/M1-UML-Class-Diagram.mmd)
- **Database Schema Diagram**: [diagrams/M2-Database-Schema-Diagram.mmd](diagrams/M1-Database-Schema-Diagram.mmd)

These diagrams are kept in sync with the code and updated as features are added.

### Installation to run locally

1. Clone the repository:
   ```bash
   git clone https://github.com/DannyG-11/SYSC4806_Group35.git
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
