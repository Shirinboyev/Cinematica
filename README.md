
# Cinematica

Cinematica is a registration application built with Spring Boot for the backend and a modern JavaScript framework for the frontend. The application allows users to register, log in, and manage their profiles.

## Features

- User registration and authentication
- Profile management
- Responsive frontend interface
- RESTful API design

## Technologies Used

- **Backend:**
  - Spring Boot
  - Spring Security
  - Hibernate/JPA
  - MySQL
- **Frontend:**
  - JavaScript (React/Vue/Angular)
  - HTML/CSS
  - Bootstrap/TailwindCSS

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Node.js 14+ (for frontend development)
- MySQL 8+

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Shirinboyev/Cinematica.git
   ```
2. **Backend Setup:**
   - Navigate to the backend directory:
     ```bash
     cd Cinematica/backend
     ```
   - Configure the `application.properties` file with your database credentials.
   - Run the application:
     ```bash
     mvn spring-boot:run
     ```

3. **Frontend Setup:**
   - Navigate to the frontend directory:
     ```bash
     cd ../frontend
     ```
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the development server:
     ```bash
     npm start
     ```

### Running the Application

After setting up both the backend and frontend, open your browser and navigate to:

```
http://localhost:3000
```

### API Documentation

API documentation is available via Swagger. Once the backend is running, visit:

```
http://localhost:8080/swagger-ui.html
```

## Contributing

Feel free to fork this repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Download

You can download this project directly from GitHub:

[Cinematica - GitHub Repository](https://github.com/Shirinboyev/Cinematica.git)
