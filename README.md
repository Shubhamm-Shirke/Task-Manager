# Task Manager

## Description

This is a RESTful API for a task management system. It allows users to create, read, update, delete, and search tasks and categories. Tasks have properties like title, description, completion status, and creation date. Categories can be created and retrieved to organize tasks.

## Features

- Create, update, delete, and retrieve tasks
- Search tasks by title or completion status
- Pagination and sorting support
- Manage categories for tasks
- Input validation
- Global exception handling
- Application info endpoints
- Spring Boot Actuator for monitoring

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Boot Actuator
- Validation
- MYSQL Database
- Lombok
- Maven

## Prerequisites

- Java 21 or higher
- Maven 3.6+

## API Endpoints

All task endpoints are prefixed with `/api/v1/tasks`.

### Get All Tasks
- **GET** `/api/v1/tasks`
- Query Parameters:
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: "createdAt")
  - `sortDir` (default: "DESC")
- Returns paginated list of tasks.

### Get Task by ID
- **GET** `/api/v1/tasks/{id}`
- Path Variable: `id` (Long)
- Returns a single task.

### Create Task
- **POST** `/api/v1/tasks`
- Body: TaskRequest JSON
- Returns created task.

### Update Task
- **PUT** `/api/v1/tasks/{id}`
- Path Variable: `id` (Long)
- Body: TaskRequest JSON
- Returns updated task.

### Delete Task
- **DELETE** `/api/v1/tasks/{id}`
- Path Variable: `id` (Long)
- Returns 200 OK.

### Search Tasks
- **GET** `/api/v1/tasks/search`
- Query Parameters:
  - `title` (optional)
  - `completed` (optional, Boolean)
  - `page`, `size`, `sortBy`, `sortDir` (as above)
- Returns paginated search results.

### Get Tasks by Completion Status
- **GET** `/api/v1/tasks/taskStatus/{status}`
- Path Variable: `status` (boolean)
- Returns list of tasks.

### Search Tasks by Title
- **GET** `/api/v1/tasks/search-by-title`
- Query Parameter: `title`
- Returns list of tasks.

### Categories

All category endpoints are prefixed with `/api/v1/categories`.

#### Get Category by ID
- **GET** `/api/v1/categories?id={id}`
- Query Parameter: `id` (Long)
- Returns a category.

#### Create Category
- **POST** `/api/v1/categories`
- Body: Category JSON
- Returns created category.

### Info

#### Get App Info
- **GET** `/api/v1/info`
- Returns application name, version, and max tasks per page.


## Database

The application uses MYSQL database for development. 


## Monitoring

Spring Boot Actuator endpoints are exposed for monitoring and management. Available at `/actuator/*`, including:
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties
- `/actuator/logger` - Logger configuration
- `/actuator/beans` - Spring beans
- `/actuator/mappings` - Request mappings


