package it.unito.sen6.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/Sen6";
    private static final String USR = "root";
    private static final String PWD = "";

    /**
     * Metodo per Registrare il driver
     */

    public static void registerDriver(){
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metodo per creare una connessione sul DB MySQL
     *
     * @return l'oggetto Connection.
     */

    public static Connection createConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USR, PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static UserDAO getUserDAO(){
        return new UserDAOImpl();
    }

    public static SubjectDAO getSubjectDAO(){ return new SubjectDAOImpl(); }

    public static TeacherDAO getTeacherDAO(){ return new TeacherDAOImpl(); }

    public static CourseDAO getCourseDAO(){ return new CourseDAOImpl(); }

    public static LessonDAO getLessonDAO(){ return new LessonDAOImpl(); }
}
