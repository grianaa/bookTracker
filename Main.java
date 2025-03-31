import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String DB_URL = "jdbc:sqlite:C:/Users/Ana/Documents/Ana/ADSAI/Block3/ISE/As2/booktracker.db";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to the database.");

            // Menu loop
            while (true) {
                System.out.println("\nBook Tracker Menu:");
                System.out.println("1. Add new user");
                System.out.println("2. View reading habits for user");
                System.out.println("3. Change book title");
                System.out.println("4. Delete a reading habit record");
                System.out.println("5. Show users mean age");
                System.out.println("6. Count users who read a specific book");
                System.out.println("7. Show total number of pages read");
                System.out.println("8. Count users who read more than one book");
                System.out.println("9. Exit");

                System.out.print("Choose option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addUser(conn, scanner);
                    case 2 -> viewReadingHabits(conn, scanner);
                    case 3 -> changeBookTitle(conn, scanner);
                    case 4 -> deleteReadingHabit(conn, scanner);
                    case 5 -> showMeanUserAge(conn);
                    case 6 -> countUsersByBook(conn, scanner);
                    case 7 -> showTotalPagesRead(conn);
                    case 8 -> countUsersWithMultipleBooks(conn);
                    case 9 -> {
                        System.out.println("Goodbye!");
                        conn.close();
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }




            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Feature 1: Add a new user
    public static void addUser(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter user's name: ");
            String name = scanner.nextLine();

            System.out.print("Enter user's age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.print("Enter user's gender (m/f): ");
            String gender = scanner.nextLine();

            String sql = "INSERT INTO user (age, gender, name) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, age);
            pstmt.setString(2, gender);
            pstmt.setString(3, name);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("New user added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }

            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while adding user: " + e.getMessage());
        }
    }

    public static void viewReadingHabits(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter the userID you want to search for: ");
            int userID = scanner.nextInt();
            scanner.nextLine(); // consume newline

            String sql = "SELECT habitID, book, pagesRead, submissionMoment FROM reading_habit WHERE userID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);

            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            System.out.println("\nReading Habits for User ID " + userID + ":");
            System.out.println("------------------------------------------------");

            while (rs.next()) {
                found = true;
                int habitID = rs.getInt("habitID");
                String book = rs.getString("book");
                int pages = rs.getInt("pagesRead");
                String date = rs.getString("submissionMoment");

                System.out.println("Habit ID: " + habitID);
                System.out.println("Book: " + book);
                System.out.println("Pages Read: " + pages);
                System.out.println("Submitted On: " + date);
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No reading habits found for this user.");
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while viewing habits: " + e.getMessage());
        }
    }

    // Feature 3: Change book title
    public static void changeBookTitle(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter the current book title: ");
            String oldTitle = scanner.nextLine();

            System.out.print("Enter the new book title: ");
            String newTitle = scanner.nextLine();

            String sql = "UPDATE reading_habit SET book = ? WHERE book = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newTitle);
            pstmt.setString(2, oldTitle);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book title updated successfully. " + rowsAffected + " record(s) affected.");
            } else {
                System.out.println("No records found with that book title.");
            }

            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while updating book title: " + e.getMessage());
        }
    }

    // Feature 4: Delete a reading habit record
    public static void deleteReadingHabit(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter the habitID of the record to delete: ");
            int habitID = scanner.nextInt();
            scanner.nextLine(); // consume newline

            String sql = "DELETE FROM reading_habit WHERE habitID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, habitID);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Reading habit record deleted successfully.");
            } else {
                System.out.println("No record found with habitID " + habitID + ".");
            }

            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while deleting record: " + e.getMessage());
        }
    }
    public static void showMeanUserAge(Connection conn) {
        try {
            String sql = "SELECT AVG(age) AS mean_age FROM user";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double meanAge = rs.getDouble("mean_age");
                System.out.printf("The average age of users is: %.2f years\n", meanAge);
            } else {
                System.out.println("No users found.");
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while calculating mean age: " + e.getMessage());
        }
    }
    public static void countUsersByBook(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter the book title: ");
            String bookTitle = scanner.nextLine();

            String sql = "SELECT COUNT(DISTINCT userID) AS total_users FROM reading_habit WHERE book = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookTitle);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total_users");
                System.out.println("Total number of users who read \"" + bookTitle + "\": " + total);
            } else {
                System.out.println("No users found for that book.");
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while counting users: " + e.getMessage());
        }
    }
    public static void showTotalPagesRead(Connection conn) {
        try {
            String sql = "SELECT SUM(pagesRead) AS total_pages FROM reading_habit";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total_pages");
                System.out.println("Total number of pages read by all users: " + total);
            } else {
                System.out.println("No reading data found.");
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while calculating total pages: " + e.getMessage());
        }
    }
    public static void countUsersWithMultipleBooks(Connection conn) {
        try {
            String sql = """
                SELECT COUNT(*) AS total_users
                FROM (
                    SELECT userID
                    FROM reading_habit
                    GROUP BY userID
                    HAVING COUNT(*) > 1
                ) AS sub;
                """;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total_users");
                System.out.println("Number of users who have read more than one book: " + total);
            } else {
                System.out.println("No users found.");
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error while counting multi-book users: " + e.getMessage());
        }
    }



}
