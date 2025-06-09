# Spring Boot React S3 File Upload

A full-stack application that enables users to upload files to Amazon S3 using Spring Boot 3 backend and React.js frontend.

## Features

- **File Upload**: Seamless file upload functionality to AWS S3
- **RESTful API**: Clean and efficient backend API endpoints
- **Modern Frontend**: Responsive React.js user interface
- **AWS Integration**: Direct integration with Amazon S3 for cloud storage
- **Real-time Feedback**: Upload progress and status notifications

## Tech Stack

### Backend

- **Spring Boot 3**: Main framework for backend services
- **Java**: Programming language
- **Maven**: Dependency management
- **AWS SDK**: For S3 integration
- **Spring Web**: For REST API endpoints

### Frontend

- **React.js**: Frontend framework
- **JavaScript/TypeScript**: Programming language
- **Axios**: HTTP client for API calls
- **CSS/SCSS**: Styling

### Cloud Services

- **AWS S3**: File storage and management

## Prerequisites

- Java 17 or higher
- Node.js 16+ and npm
- AWS Account with S3 access
- Maven 3.6+
- IDE (IntelliJ IDEA, VS Code, etc.)

## Installation & Setup

### Backend Setup

1. Clone the repository

```bash
git clone <repository-url>
cd spring-boot-react-s3-upload
```

2. Navigate to backend directory

```bash
cd backend
```

3. Configure AWS credentials in `application.properties`

```properties
aws.access.key.id=your-access-key-id
aws.secret.access.key=your-secret-access-key
aws.s3.bucket.name=your-bucket-name
aws.s3.region=your-region
```

4. Install dependencies and run

```bash
mvn clean install
mvn spring-boot:run
```

The backend server will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to frontend directory

```bash
cd frontend
```

2. Install dependencies

```bash
npm install
```

3. Start the development server

```bash
npm start
```

The frontend will start on `http://localhost:3000`

## Project Structure

```
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/fileupload/
│   │   │   │       ├── FileUploadApplication.java
│   │   │   │       ├── controller/
│   │   │   │       ├── service/
│   │   │   │       └── config/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── services/
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
└── README.md
```

## API Endpoints

### File Upload

- **POST** `/api/v1/upload`
  - Upload file to S3
  - Returns file URL and metadata

### File Management

- **GET** `/api/v1/files`
  - List all uploaded files
- **DELETE** `/api/v1/files/{fileId}`
  - Delete specific file from S3

## AWS S3 Configuration

1. Create an S3 bucket in your AWS Console
2. Set up IAM user with S3 permissions
3. Configure CORS policy for your bucket:

```json
[
  {
    "AllowedHeaders": ["*"],
    "AllowedMethods": ["GET", "PUT", "POST", "DELETE"],
    "AllowedOrigins": ["http://localhost:3000"],
    "ExposeHeaders": []
  }
]
```

## Environment Variables

Create a `.env` file in the frontend directory:

```env
REACT_APP_API_BASE_URL=http://localhost:8080/api/v1
```

## Usage

1. Start both backend and frontend servers
2. Navigate to `http://localhost:3000`
3. Select files using the file picker
4. Click upload to send files to S3
5. View uploaded files in the file list

## Key Features Implementation

### File Upload Component

- Drag and drop functionality
- File type validation
- Upload progress tracking
- Error handling

### Backend Service

- Multipart file handling
- S3 client configuration
- File metadata management
- Exception handling

## Security Considerations

- Input validation on file types and sizes
- AWS IAM roles and permissions
- CORS configuration
- Environment variable management

## Deployment

### Backend Deployment

- Package as JAR: `mvn clean package`
- Deploy to AWS Elastic Beanstalk, EC2, or containerize with Docker

### Frontend Deployment

- Build production version: `npm run build`
- Deploy to AWS S3 static hosting, Netlify, or Vercel

## Troubleshooting

### Common Issues

**AWS Credentials Error**

- Verify AWS credentials in application.properties
- Check IAM permissions for S3 access

**CORS Issues**

- Update S3 bucket CORS configuration
- Verify frontend URL in allowed origins

**File Upload Fails**

- Check file size limits
- Verify S3 bucket permissions
- Review network connectivity

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.
