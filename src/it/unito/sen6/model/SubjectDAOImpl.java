package it.unito.sen6.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAOImpl implements SubjectDAO {
    private static final String QUERY_INSERT_SUBJECT = "INSERT INTO subject(name) VALUES (?)";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM subject";
    private static final String QUERY_SELECT_ALL_ACTIVE = "SELECT * FROM subject WHERE active=TRUE";
    private static final String QUERY_SELECT_BY_ID = "SELECT * FROM subject WHERE id=?";
    private static final String QUERY_SELECT_BY_NAME = "SELECT * FROM subject WHERE name=?";
    private static final String QUERY_CANCEL_BY_ID = "UPDATE subject SET active=FALSE WHERE id=?";
    private static final String QUERY_ACTIVATE_BY_ID = "UPDATE subject SET active=TRUE WHERE id=?";

    @Override
    public int insertSubject(Subject subject) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_INSERT_SUBJECT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,subject.getName());
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
    public Subject selectSubjectById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Subject subject = null;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_ID);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
                subject = new Subject(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getBoolean(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return subject;
    }

    @Override
    public boolean subjectNameRegistered(String name) {
        return ProtectedMethods.alreadyExist(name,QUERY_SELECT_BY_NAME);
    }

    @Override
    public List<Subject> selectAllSubjects() {
        return getAll(QUERY_SELECT_ALL);
    }

    @Override
    public List<Subject> selectAllActiveSubjects() {
        return getAll(QUERY_SELECT_ALL_ACTIVE);
    }

    private List<Subject> getAll(String query) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Subject> subjects = new ArrayList<>();
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                subjects.add(new Subject(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getBoolean(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return subjects;
    }

    @Override
    public boolean cancelSubjectById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_ID);
    }

    @Override
    public boolean activateSubjectById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_ACTIVATE_BY_ID);
    }
}
