This is the README file for my command-line Java application for Book Tracker.

Book Tracker is an application that mimics the book tracker website, where users cna track what books they have read and what they might want to read. The application uses and manages a dataset with some data abojt the reading habbits of users

The application iscreated using Java and SQL and has the following functionalities: 
1. Provide the ability to add a user to the database
2. Provide all the reading habit data for a certain user
3. Provide the ability to change the title of a book in the database
4. Provide the ability to delete a record/row from the ReadingHabit table
5. Provide the mean age of the users
6. Provide the total number of users that have read pages from a specific book
7. Provide the total number of pages read by all users
8. Provide the total number of users that have read more than one book
9. Add a column to the User table named "Name" which contains TEXT data

The files included are: 
- Main.java - the main program
- booktracker.db the SQLite database
- sqlite-jdbc-3.43.2.0.jar SQLite JDBC driver
- slf4j-api-2.0.9.jar, slf4j-nop-2.0.9.jar logging dependencies

To run it, you have to 
1. Open the project in IntelliJ
2. Add te 3 JAR files to your classpath (Example here https://www.dhiwise.com/post/the-best-guide-on-how-to-add-jar-to-classpath-intellij)
3. Run the Java file
4. Use the command-line menu to interact and test the functionalities