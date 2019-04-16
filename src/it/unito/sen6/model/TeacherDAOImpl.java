package it.unito.sen6.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAOImpl implements TeacherDAO {
    private static final String QUERY_INSERT_TEACHER = "INSERT INTO teacher(name,surname) VALUES (?,?)";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM teacher";
    private static final String QUERY_SELECT_ALL_ACTIVE = "SELECT * FROM teacher WHERE active=TRUE";
    private static final String QUERY_SELECT_BY_ID = "SELECT * FROM teacher WHERE id=?";
    private static final String QUERY_SELECT_BY_NAME_SURNAME = "SELECT * FROM teacher WHERE name=? AND surname=?";
    private static final String QUERY_CANCEL_BY_ID = "UPDATE teacher SET active=FALSE WHERE id=?";
    private static final String QUERY_ACTIVATE_BY_ID = "UPDATE teacher SET active=TRUE WHERE id=?";

    @Override
    public int insertTeacher(Teacher teacher) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_INSERT_TEACHER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,teacher.getName());
            ps.setString(2,teacher.getSurname());
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
    public Teacher selectTeacherById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Teacher teacher = null;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_ID);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
                teacher = new Teacher(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBoolean(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return teacher;
    }

    @Override
    public List<Teacher> selectAllTeachers() {
        return getList(QUERY_SELECT_ALL);
    }

    private List<Teacher> getList(String querySelectAll) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Teacher> teachers = new ArrayList<>();
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(querySelectAll);
            rs = ps.executeQuery();
            while (rs.next()){
                teachers.add(new Teacher(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBoolean(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return teachers;
    }

    @Override
    public List<Teacher> selectAllActiveTeachers() {
        return getList(QUERY_SELECT_ALL_ACTIVE);
    }

    @Override
    public boolean teacherRegistered(String name, String surname) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isPresent = false;
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_NAME_SURNAME);
            ps.setString(1,name);
            ps.setString(2,surname);
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
    public boolean cancelTeacherById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_ID);
    }

    @Override
    public boolean activateTeacherById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_ACTIVATE_BY_ID);
    }


}
