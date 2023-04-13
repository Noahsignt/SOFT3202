package week6;

import java.io.File;
import java.sql.*;

public class HelloSQL {
    private static final String dbName = "test.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    public static void createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            System.out.println("Database already created");
            return;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            // If we get here that means no exception raised from getConnection - meaning it worked
            System.out.println("A new database has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void removeDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            boolean result = dbFile.delete();
            if (!result) {
                System.out.println("Couldn't delete existing db file");
                System.exit(-1);
            } else {
                System.out.println("Removed existing DB file.");
            }
        } else {
            System.out.println("No existing DB file.");
        }
    }

    public static void setupDB() {
        String createStudentTableSQL =
                """
                CREATE TABLE IF NOT EXISTS students (
                    id integer PRIMARY KEY,
                    first_Name text NOT NULL,
                    last_Name text NOT NULL,
                    wam REAL NOT NULL
                );
                """;

        String createUnitTableSQL =
                """
                CREATE TABLE IF NOT EXISTS units (
                    id integer PRIMARY KEY,
                    code text NOT NULL,
                    name text NOT NULL
                );
                """;

        String createStudentUnitTableSQL =
                """
                CREATE TABLE IF NOT EXISTS student_units (
                    student_id integer NOT NULL,
                    unit_id integer NOT NULL,
                    PRIMARY KEY (student_id, unit_id),
                    FOREIGN KEY (student_id)
                        REFERENCES students (id)
                            ON DELETE CASCADE,
                    FOREIGN KEY (unit_id)
                        REFERENCES units (id)
                            ON DELETE CASCADE
                );
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
                Statement statement = conn.createStatement()) {
            statement.execute(createStudentTableSQL);
            statement.execute(createUnitTableSQL);
            statement.execute(createStudentUnitTableSQL);

            System.out.println("Created tables");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void addStartingData() {
        String addStudentDataSQL =
                """
                INSERT INTO students(first_name, last_name, wam) VALUES
                    ("Bob", "McBob", 74.9),
                    ("Jane", "McJane", 90.1),
                    ("Tee", "McTee", 44.3),
                    ("Marty", "McMarty", 68.0)
                """;

        String addUnitDataSQL =
                """
                INSERT INTO units(code, name) VALUES
                    ("SOFT2201", "SCD1"),
                    ("SOFT3202", "SCD2"),
                    ("SOFT2412", "Agile"),
                    ("SOFT3888", "Capstone")
                """;

        // Dangerous to do it this way - I rely on id values I shouldn't. Better to use an internal SELECT.
        String addStudentUnitDataSQL =
                """
                INSERT INTO student_units(student_id, unit_id) VALUES
                    (1, 2),
                    (1, 3),
                    (1, 4),
                    (2, 1),
                    (2, 3),
                    (3, 2),
                    (4, 2)
                """;

        // Like this
        String addStudentUnitDataLookupSQL =
                """
                INSERT INTO student_units(student_id, unit_id) VALUES
                    ((SELECT id FROM students WHERE first_name = 'Marty'), (SELECT id FROM units WHERE code = 'SOFT3888'))
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(addStudentDataSQL);
            statement.execute(addUnitDataSQL);
            statement.execute(addStudentUnitDataSQL);
            statement.execute(addStudentUnitDataLookupSQL);

            System.out.println("Added starting data");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void addDataFromQuestionableSource(String firstName, String lastName, double wam) {
        String addSingleStudentWithParametersSQL =
                """
                INSERT INTO students(first_name, last_name, wam) VALUES
                    (?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addSingleStudentWithParametersSQL)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setDouble(3, wam);
            preparedStatement.executeUpdate();

            System.out.println("Added questionable data");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void queryDataSimple(double minWAM, double maxWAM) {
        String studentRangeSQL =
                """
                SELECT first_name, last_name
                FROM students WHERE wam > ? AND wam < ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(studentRangeSQL)) {
            preparedStatement.setDouble(1, minWAM);
            preparedStatement.setDouble(2, maxWAM);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                System.out.println(
                        results.getString("first_name") + " " +
                        results.getString("last_name"));
            }

            System.out.println("Finished simple query");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void queryDataWithJoin(String uos) {
        String enrolmentSQL =
                """
                SELECT first_name, last_name
                FROM students AS s
                INNER JOIN student_units AS su ON s.id = su.student_id
                INNER JOIN units as u ON su.unit_id = u.id
                WHERE u.code = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(enrolmentSQL)) {
            preparedStatement.setString(1, uos);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                System.out.println(
                        results.getString("first_name") + " " +
                                results.getString("last_name"));
            }

            System.out.println("Finished join query");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        removeDB();
        createDB();
        setupDB();
        addStartingData();
        addDataFromQuestionableSource("New", "Student", 110.0);
        queryDataSimple(65.0, 75.0);
        queryDataWithJoin("SOFT3202");
    }
}
