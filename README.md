### How to Start the Frontend
1. Run the Backend through Gradle (Gradle tab > Tasks > application > run)
   - Frontend will work if you don't do this, but backend api requests won't load obviously
2. Navigate to frontend directory in terminal with `cd frontend`
3. `npm run dev` should run the frontend on http://localhost:5173/

### How to Start the Backend
1. Clone the repository 
2. Navigate to Main.java (under src/main/java)
4. Run "Main.java"
5. Interact with the program with its built in menu:
   1. Create a new profile
   2. Login (required before modification)
   3. Create a new schedule
   4. Remove a schedule
   5. View calendar
   6. Add course to schedule
   7. Remove course from schedule
   8. Search for a course
   9. Print all schedules
   10. Exit
6. Selecting each option will have subsequent menu options that will appear

Note: 
- saved courses page is not complete (not part of MVP)
- react fetching classes is not fully functional (not part of MVP)
