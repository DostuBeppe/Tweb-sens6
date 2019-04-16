package it.unito.sen6.model;

import java.util.List;

public interface LessonDAO {
    int insertLesson(Lesson lesson);
    Lesson selectLessonById(int id);
    boolean lessonRegistered(Lesson lesson);
    List<Lesson> selectAllLessons();
    List<Lesson> selectAllActiveLessons();
    List<Lesson> selectAllLessonsByUserId(int idUser);
    List<Lesson> selectAllActiveLessonsByUserId(int idUser);
    List<Lesson> selectAllLessonsBySchedule(int schedule);
    boolean cancelLessonById(int id);
    boolean cancelLessonByCourseId(int id);
    boolean cancelLessonByUserId(int id);
    boolean cancelLessonByTeacherId(int id);
    boolean cancelLessonBySubjectId(int id);
}
