package it.unito.sen6.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDAOImpl implements LessonDAO{
    private static final String QUERY_INSERT = "INSERT INTO lesson(idCourse, idUser, schedule) VALUES (?,?,?)";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM lesson";
    private static final String QUERY_SELECT_ALL_ACTIVE = "SELECT * FROM lesson WHERE active=TRUE";
    private static final String QUERY_SELECT_ALL_BY_IDUSER = "SELECT * FROM lesson WHERE idUser=?";
    private static final String QUERY_SELECT_ALL_ACTIVE_BY_IDUSER = "SELECT * FROM lesson WHERE idUser=? AND active=TRUE";
    private static final String QUERY_SELECT_ALL_BY_SCHEDULE = "SELECT * FROM lesson WHERE schedule=?";
    private static final String QUERY_SELECT_BY_ID = "SELECT * FROM lesson WHERE id=?";
    private static final String QUERY_SELECT_BY_IDCOURSE_IDUSER_SCHEDULE = "SELECT * FROM lesson WHERE ((idCourse=? AND schedule=?)OR(idUser=? AND schedule=?)) AND active=TRUE";
    private static final String QUERY_CANCEL_BY_ID = "UPDATE lesson SET active=FALSE WHERE id=?";
    private static final String QUERY_CANCEL_BY_IDCOURSE = "UPDATE lesson SET active=FALSE WHERE idCourse=?";
    private static final String QUERY_CANCEL_BY_IDUSER = "UPDATE lesson SET active=FALSE WHERE idUser=?";
    private static final String QUERY_CANCEL_BY_IDTEACHER = "UPDATE lesson, course SET lesson.active=FALSE WHERE lesson.idCourse = course.id AND course.idTeacher=?";
    private static final String QUERY_CANCEL_BY_IDSUBJECT = "UPDATE lesson, course SET lesson.active=FALSE WHERE lesson.idCourse = course.id AND course.idSubject=?";
    private static final String QUERY_ACTIVATE_BY_ID = "UPDATE lesson SET active=TRUE WHERE id=?";

    @Override
    public int insertLesson(Lesson lesson) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,lesson.getCourse().getId());
            ps.setInt(2,lesson.getUser().getId());
            ps.setInt(3,lesson.getSchedule());
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
    public Lesson selectLessonById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Lesson lesson = null;
        try{
            conn=DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_ID);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
                lesson = new Lesson(
                        rs.getInt(1),
                        new Course(rs.getInt(2),null,null,true),
                        new User(rs.getInt(3),"","","",true,true),
                        rs.getInt(4),
                        rs.getBoolean(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return lesson;
    }

    @Override
    public boolean lessonRegistered(Lesson lesson) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isPresent = false;
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(QUERY_SELECT_BY_IDCOURSE_IDUSER_SCHEDULE);
            ps.setInt(1,lesson.getCourse().getId());
            ps.setInt(2,lesson.getSchedule());
            ps.setInt(3,lesson.getUser().getId());
            ps.setInt(4,lesson.getSchedule());
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
    public List<Lesson> selectAllLessons() {
        return getList(QUERY_SELECT_ALL);
    }

    @Override
    public List<Lesson> selectAllActiveLessons() {
        return getList(QUERY_SELECT_ALL_ACTIVE);
    }

    @Override
    public List<Lesson> selectAllLessonsByUserId(int idUser) { return getListWithAttr(idUser,QUERY_SELECT_ALL_BY_IDUSER); }

    @Override
    public List<Lesson> selectAllActiveLessonsByUserId(int idUser) { return getListWithAttr(idUser,QUERY_SELECT_ALL_ACTIVE_BY_IDUSER); }

    @Override
    public List<Lesson> selectAllLessonsBySchedule(int schedule) { return getListWithAttr(schedule,QUERY_SELECT_ALL_BY_SCHEDULE); }

    private List<Lesson> getList(String query) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Lesson> lessons = new ArrayList<>();
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            buildLessonList(rs, lessons);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return lessons;
    }

    private List<Lesson> getListWithAttr(int attr, String querySelectAll) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Lesson> lessons = new ArrayList<>();
        try{
            conn= DAOFactory.createConnection();
            ps = conn.prepareStatement(querySelectAll);
            ps.setInt(1,attr);
            rs = ps.executeQuery();
            buildLessonList(rs, lessons);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ProtectedMethods.closeAll(conn, ps, rs);
        }
        return lessons;
    }

    private void buildLessonList(ResultSet rs, List<Lesson> lessons) throws SQLException {
        while (rs.next()){
            lessons.add(new Lesson(
                    rs.getInt(1),
                    new Course(rs.getInt(2),null,null,true),
                    new User(rs.getInt(3),"","","",true,true),
                    rs.getInt(4),
                    rs.getBoolean(5)));
        }
    }

    @Override
    public boolean cancelLessonById(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_ID);
    }

    @Override
    public boolean cancelLessonByCourseId(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_IDCOURSE);
    }

    @Override
    public boolean cancelLessonByUserId(int id) {
        return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_IDUSER);
    }

    @Override
    public boolean cancelLessonByTeacherId(int id) { return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_IDTEACHER); }

    @Override
    public boolean cancelLessonBySubjectId(int id) { return ProtectedMethods.toggleActive(id,QUERY_CANCEL_BY_IDSUBJECT); }

}
