# Logbook App

Author: Krystof Jelínek (jelk10@vse.cz)


## Project Description
Logbook is a tool for scuba divers to log their dives. It allows users to record details about their dives, including location, depth, duration, and conditions. The application provides a user-friendly interface for managing dive logs and viewing dive history.
Users can add, edit, view, and delete dive entries. They can also add dive sites for other divers to discover and use. The application supports multiple users, allowing each user to maintain their own dive log with dive sites being shared across all users.

### Technologies Used
- **Java 21**: The application is developed using Java version 21.
- **JavaFX**: The user interface is built using JavaFX.
- **Maven**: The project uses Maven for dependency management and build automation.
- **H2 Database**: The application uses an embedded H2 database to store user accounts, dive logs, and dive sites.
---

## Compilation and Build
The project is built using **Java 21** and **Maven**. Follow these steps to compile and build the project:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/krystofjelinek/LogbookApp.git
   cd LogbookApp
   ```

2. **Compile and build**:
   Run the following command to build the project and create an executable JAR file:
   ```bash
   mvn clean package
   ```

   The resulting JAR file will be located in the `target` directory under the name `LogbookApp-1.0.jar`.

3. **Run the app**:
   Start the app using:
   ```bash
   java -jar target/LogbookApp-1.0.jar
   ```
   For login use email: john.doe@example.com

## Deviations from specifications
**User Interface**:
   - Sign up form should be added to the application, allowing users to create an account. Now the application only supports users generated from the database on startup.
   - Profile page should be added to the application, allowing users to view and edit their profile information.
   - Dive site search functionality should be added to the application, allowing users to search for dive sites by name or location.
