package it.unito.sen6.model;

import java.util.List;

public interface CourseDAO {
    int insertCourse(Course course);
    Course selectCourseById(int id);
    boolean courseRegistered(Course course);
    List<Course> selectAllCourses();
    List<Course> selectAllActiveCourses();
    boolean cancelCourseById(int id);
    boolean cancelCourseBySubjectId(int id);
    boolean cancelCourseByTeacherId(int id);
    boolean activateCourseById(int id);
}
