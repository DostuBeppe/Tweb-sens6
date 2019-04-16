package it.unito.sen6.model;

import java.util.List;

public interface SubjectDAO {
    int insertSubject(Subject subject);
    Subject selectSubjectById(int id);
    boolean subjectNameRegistered(String name);
    List<Subject> selectAllSubjects();
    List<Subject> selectAllActiveSubjects();
    boolean cancelSubjectById(int id);
    boolean activateSubjectById(int id);
}
