# Task Management Android App

## Overview
This is an Android application designed for task management.

## Features

### 1. Register/Authentication
- Implemented using Firebase Authentication service.
- Users can create an account and login.

![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/92bc59d2-b52b-4ed0-8891-baff6e1f61b8)

![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/703c48d0-5ad2-4784-a006-812b4509e67b)

### 2. CRUD Operations
- Users can manage their tasks (Create, Read, Update, Delete).
- Task information includes ( image, title, description, status (done or not done), user, and date ).
  - ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/3983dfdb-c7da-4021-9f3a-eea8b04ee543)
  
      - **Add Task**
    
  - ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/84ff28b7-dc2c-412d-9271-ee0c73d3f6ee)
    
      - **Update Task**
    
  - ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/07db6061-996f-4261-91c2-160afe0d449c)
    
     -**Task Information**
    
  - ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/3fa6dbee-bf03-44b2-b86d-d349e657695a)
    
     - **Show All Tasks**
  
- to store simple data i use firestore database and storage service from firebase to store the images

![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/3dbb1ea1-16bb-49be-b580-7eb4760028cc)

![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/98c844c4-1569-4a5e-b128-1dd85e40c0ff)

### 3. Search Functionality
- Users can search for specific tasks.

### 4. Task Sharing
- Users can share their tasks with others.
- Shared tasks can be private or public.
- Task sharing mechanism involves sending a task using its ID. The recipient needs the ID to add it to their tasks.
- For private tasks, a password is required to exchange tasks.

![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/a1d1577c-1cf7-4c65-a795-07977a644c45)
![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/db3ff66b-52af-48d9-95a7-b6a24c222c89)
  - to share an task
- ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/1ccac23e-a140-468e-913a-e159c20e81b1)
  - The receiver of task use the shared task id to receive task, and add it to his tasks.
  
### 5. Settings/Profile Management
- Users can manage their profile settings.
- ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/c18aa621-1a17-4597-8545-e3bb11afe072)


### 6. Database
- The app uses Firestore for database services.
- Three main collections:
- ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/1294afb9-3125-4f91-a38a-91534d5fbf39)
  - **Task Collection**
  - ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/6f5add0e-e56c-40ad-acc0-1fd1e249a888)
  - **Shared Tasks Collection**
  - ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/f3b26e6f-1c02-4647-a7fc-665bc1b2a20a)
  - **User Task Collection**
- Firebase Storage is used for storing images.
- ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/d02f9ead-c280-497c-a8f2-2600a0917616)


## Installation

### For Developers
1. Clone the repository using Android Studio's Version Control with the URL of this repository.
  ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/b87fb488-f844-402a-b744-04f288ff2e0e)
2. Create an account on Firebase (if you don't have one) and set up a new project.
3. Connect your app to the Firebase project.
  ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/cd888edf-5aa0-44c5-821e-3b35ccdc9f11)
4. Insert the `google-services.json` file into the `app` folder. This file can be obtained from Firebase and contains the credentials and information about the project.

  ![image](https://github.com/abdelaziz-web/TaskManagement/assets/67710015/8f9ffeb2-7fe9-40d7-b9de-c5b4514b8e8a)



## How to Use
1. **Clone the Repository:**
   ```sh
   git clone 
