# School Management Backend (Java/Spring Boot)

This is the backend for a modern School Management System, built using Java and Spring Boot. It provides secure REST APIs for managing admissions, enquiries, and administrative tasks. The backend supports file storage on AWS S3 and features a robust admin dashboard.

**Live Demo:** [http://13.202.116.118/](http://13.202.116.118/)  
---

## Features

- **Full Admission & Enquiry Management**  
  Store and manage student admission and enquiry data, including detailed parent and academic information.

- **Admin Dashboard**  
  Responsive dashboard for administrators to view, search, and manage all admissions and enquiries. Displays images and payment proofs stored in S3.

- **Secure Authentication**  
  Admin login with JWT-based authentication and role validation.

- **AWS S3 Integration**  
  All document and image uploads (child, parents, payment proof) are securely stored in S3 with presigned URL access.

- **RESTful API**  
  Well-structured endpoints for frontend and potential mobile app integration.

- **Dockerized Deployment**  
  Easily deployable using Docker.

---

## Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Security
- **Database:** (Configurable, e.g., MySQL/PostgreSQL)
- **File Storage:** AWS S3 (via AWS SDK)


---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- AWS S3 credentials (for file storage)

### Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Robert0-7/school-java-backend.git
   cd school-java-backend
   ```

2. **Configure Environment Variables**

   Set your AWS and database credentials in `application.properties` or as environment variables:
   ```
   # AWS S3 Configuration
   s3.bucket.name=your-bucket-name
   cloud.aws.credentials.access-key=YOUR_ACCESS_KEY
   cloud.aws.credentials.secret-key=YOUR_SECRET_KEY
   cloud.aws.region.static=ap-south-1

   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/schooldb
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

3. **Build the Project**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   Or using Docker:
   ```bash
   docker build -t school-backend .
   docker run -p 8080:8080 --env-file .env school-backend
   ```

---

## API Endpoints

- `POST /api/admin/auth/login` — Admin login (returns JWT token)
- `GET /api/admin/enquiries` — List all enquiries (admin only)
- `GET /api/admin/admissions` — List all admissions (admin only)
- File upload, image fetch, and more—see code or Swagger documentation (coming soon).

---

## Admin Dashboard

- **Authentication:**  
  Login required. JWT token stored in browser, checked for validity and role.

- **Displayed Data:**  
  - **Enquiries Table:** Enquiry number, student and parent details, contact info, query, source, etc.
  - **Admissions Table:** Admission ID, full student/parent details, academic info, S3-hosted images (child, father, mother), payment status, payment proof, etc.

- **Image Handling:**  
  - Images are uploaded to AWS S3.
  - Backend generates presigned URLs for secure, temporary access.
  - Images and proofs are shown as links or thumbnails in the dashboard.

---

## Security

- All admin endpoints require JWT-based authentication.
- Sensitive actions are logged.
- S3 URLs are presigned for limited-time access to prevent unauthorized downloads.

---

## Contribution

Pull requests and suggestions are welcome!

---

## License

[MIT](LICENSE) (add your license if you have one)

---

## Acknowledgements

- Spring Boot Documentation
- AWS SDK for Java
- Bootstrap, Chart.js (if used in frontend)
