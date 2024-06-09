# Task Management Android App

## Overview
This is an Android application designed for task management. Users can register, authenticate, and manage their tasks including creating, reading, updating, and deleting tasks. The app utilizes Firebase for authentication and database services.

## Features

### 1. Register/Authentication
- Implemented using Firebase Authentication service.
- Users can create an account and login.
![Register](https://github.com/yourusername/repository-name/blob/main/images/register.png)

### 2. CRUD Operations
- Users can manage their tasks (Create, Read, Update, Delete).
- Task information includes ( image, title, description, status (done or not done), user, and date ).
  - **Add Task**
  - **Update Task**
  - **Task Information**
  - **Show All Tasks**
 
  - ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/a3ed962a-2148-4004-b4dc-a5077638d33d)


### 3. Search Functionality
- Users can search for specific tasks.
![Search](https://github.com/yourusername/repository-name/blob/main/images/search.png)

### 4. Task Sharing
- Users can share their tasks with others.
- Shared tasks can be private or public.
- Task sharing mechanism involves sending a task using its ID. The recipient needs the ID to add it to their tasks.
  - For private tasks, a password is required to exchange tasks.

### 5. Settings/Profile Management
- Users can manage their profile settings.

### 6. Database
- The app uses Firestore for database services.
- Three main collections:
  - **Task Collection**
  - **Shared Tasks Collection**
  - **User Task Collection**
- Firebase Storage is used for storing images.

## Installation

### For Developers
1. Clone the repository using Android Studio's Version Control with the URL of this repository.
2. Create an account on Firebase (if you don't have one) and set up a new project.
3. Connect your app to the Firebase project.
4. Insert the `google-services.json` file into the `app` folder. This file can be obtained from Firebase and contains the credentials and information about the project.

## How to Use
1. **Clone the Repository:**
   ```sh
   git clone https://github.com/yourusername/repository-name.git
