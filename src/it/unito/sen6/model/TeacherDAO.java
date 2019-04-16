package it.unito.sen6.model;

import java.util.List;

public interface TeacherDAO {
    int insertTeacher(Teacher teacher);
    Teacher selectTeacherById(int id);
    List<Teacher> selectAllTeachers();
    List<Teacher> selectAllActiveTeachers();
    boolean cancelTeacherById(int id);
    boolean activateTeacherById(int id);
    boolean teacherRegistered(String name, String surname);
}
