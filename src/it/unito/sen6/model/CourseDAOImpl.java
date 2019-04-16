package it.unito.sen6.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {
    private static final String QUERY_INSERT = "INSERT INTO course(idSubject, idTeacher) VALUES (?,?)";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM course";
    private static final String QUERY_SELECT_ALL_ACTIVE = "SELECT * FROM course WHERE active=TRUE";
    private static final String QUERY_SELECT_BY_ID = "SELECT * FROM  course WHERE id=?";
    private static final String QUERY_SELECT_BY_IDSUBJECT_IDTEACHER = "SELECT * FROM course WHERE idSubject=? AND idTeacher=?";
    private static final String QUERY_CANCEL_BY_ID = "UPDATE course SET active=FALSE WHERE id=?";
    private static final String QUERY_CANCEL_BY_IDSUBJECT = "UPDATE course SET active=FALSE WHERE idSubject=?";
    private static final String QUERY_CANCEL_BY_IDTEACHER = "UPDATE course SET active=FALSE WHERE idTeacher=?";
    private static final String QUERY_ACTIVATE_BY_ID = "UPDATE course SET active=TRUE WHERE id=?";

    @Override
    public int insertCourse(Course course) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,course.getSubject().getId());
            ps.setInt(2,course.getTeacher().getId());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return result;
    }

    @Override
    public Course selectCourseById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Course course = null;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_ID);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
                course = new Course(
                        rs.getInt(1),
                        new Subject(rs.getInt(2),"",true),
                        new Teacher(rs.getInt(3),"","",true),
                        rs.getBoolean(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return course;
    }

    @Override
    public boolean courseRegistered(Course course) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isPresent = false;
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_IDSUBJECT_IDTEACHER);
            ps.setInt(1,course.getSubject().getId());
            ps.setInt(2,course.getTeacher().getId());
            rs = ps.executeQuery();
            isPresent = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return isPresent;
    }

    @Override
    public List<Course> selectAllCourses() {
        return getList(QUERY_SELECT_ALL);
    }

    @Override
    public List<Course> selectAllActiveCourses() {
        return getList(QUERY_SELECT_ALL_ACTIVE);
    }

    private List<Course> getList(String query) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Course> courses = new ArrayList<>();
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                courses.add(new Course(
                        rs.getInt(1),
                        new Subject(rs.getInt(2),"",true),
                        new Teacher(rs.getInt(3),"","",true),
                        rs.getBoolean(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return courses;
    }

    @Override
    public boolean cancelCourseById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_ID);
    }

    @Override
    public boolean cancelCourseBySubjectId(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_IDSUBJECT);
    }

    @Override
    public boolean cancelCourseByTeacherId(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_IDTEACHER);
    }

    @Override
    public boolean activateCourseById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_ACTIVATE_BY_ID);
    }
}
