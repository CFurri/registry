# Registry

## Time Control Application

A comprehensive employee time management system developed with Java and Kotlin, using MySQL database for data persistence. This application helps organizations track employee work hours, manage shifts, and record training activities.

## Features

- Employee management with detailed profiles
- Time logging for tracking work hours
- Shift scheduling and assignment
- Training record management
- JPA-based data persistence
- JDBC support for database operations

## Project Structure

The project is organized into several modules:

- **app**: Main application module
- **jdbc**: JDBC implementation for database access
- **jpa**: JPA implementation for ORM functionality
- **model**: Core domain models and interfaces
- **repository**: Data access repositories
- **utilities**: Common utility functions

## Installation

### Prerequisites

- Java 11 or higher
- MySQL 8.0 or higher
- Gradle 7.0 or higher

### Steps

1. Clone the repository:
   ```
   git clone https://github.com/CFurri/registry.git
   cd registry
   ```

2. Configure the database connection in `jpa/src/main/resources/META-INF/persistence.xml`

3. Build the project:
   ```
   ./gradlew build
   ```

## Usage

### Running the Application

```
./gradlew run
```

### Example Code

Below is a simplified example of how to use the API:

```
// Import necessary classes
import jpa.model.cat.uvic.teknos.registry.repositories.JpaEmployee;
import jpa.model.cat.uvic.teknos.registry.repositories.JpaTimeLog;
import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.TimeLog;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Create a new employee
Employee employee = new JpaEmployee();
employee.setFirstName("John");
employee.setLastName("Doe");
employee.setEmail("john.doe@example.com");
employee.setHireDate(LocalDate.now());

// Add a time log for the employee
TimeLog timeLog = new JpaTimeLog();
timeLog.setCheckInTime(LocalDateTime.now());
employee.addTimeLog(timeLog);
```

## Testing

Run the tests with:

```
./gradlew test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Versions

| Version | Description           |
|---------|-----------------------|
| 1.0.0   | First edition         |
| 1.1.0   | Server-Client edition |

## License

This project is licensed under the MIT License - see the LICENSE file for details.
