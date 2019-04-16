package it.unito.sen6.controller;

import com.google.gson.Gson;
import it.unito.sen6.model.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@WebServlet(name = "Controller", asyncSupported = true)
public class Controller extends HttpServlet {
    private static int SCHEDULE_SIZE = 20;
    private static int SCHEDULE_NOW = 9;
    @Override
    public void init() throws ServletException {
        super.init();
        DAOFactory.registerDriver();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        UserDAO userDAO;
        SubjectDAO subjectDAO;
        TeacherDAO teacherDAO;
        CourseDAO courseDAO;
        LessonDAO lessonDAO;
        ServletContext context = getServletContext();
        HttpSession session = request.getSession();
        User user;

        int userId = -1;
        if(session.getAttribute("id")!=null)
            userId=Integer.parseInt(session.getAttribute("id").toString());
        boolean admin = false;
        if(session.getAttribute("admin")!=null)
            admin=Boolean.parseBoolean(session.getAttribute("admin").toString());


        String type = request.getParameter("type");
        String res = "";
        if (type == null) {
            res = getJSONResponse(-1,"Request Type Mancante",null);
        } else if (type.equals("component")) {
            String component = request.getParameter("component");
            if(component==null){
                res=getJSONResponse(-1,"Request Component Mancante",null);
            } else {
                switch (component){
                    case "homepage":
                        res=getPage(context,"components/homepage.html");
                        break;
                    case "admin-table-user":
                        res=getPage(context,"components/admin/table-user.html");
                        break;
                    case "admin-table-subject":
                        res=getPage(context,"components/admin/table-subject.html");
                        break;
                    case "admin-table-teacher":
                        res=getPage(context,"components/admin/table-teacher.html");
                        break;
                    case "admin-table-course":
                        res=getPage(context,"components/admin/table-course.html");
                        break;
                    case "admin-table-lesson":
                        res=getPage(context,"components/admin/table-lesson.html");
                        break;
                    case "calendar":
                        res=getPage(context,"components/calendar.html");
                        break;
                    case "history":
                        res=getPage(context,"components/history.html");
                        break;
                    case "courses":
                        res=getPage(context,"components/courses.html");
                        break;

                    default:
                        res=getJSONResponse(-1,"Request Component Inesistente",null);
                }
            }
        } else if (type.equals("data")) {
            String data = request.getParameter("data");
            if(data==null){
                res=getJSONResponse(-1,"Request Data Mancante",null);
            } else {
                switch (data){
                    case "list-user":
                        userDAO = DAOFactory.getUserDAO();
                        res = getJSONResponse(1,"Lista Utenti Completa", userDAO.selectAllUsers());
                    break;
                    case "list-subject":
                        subjectDAO = DAOFactory.getSubjectDAO();
                        res = getJSONResponse(1,"Lista Materie Completa", subjectDAO.selectAllSubjects());
                    break;
                    case "list-subject-active":
                        subjectDAO = DAOFactory.getSubjectDAO();
                        res = getJSONResponse(1,"Lista Materie Attive",subjectDAO.selectAllActiveSubjects());
                    break;
                    case "list-teacher":
                        teacherDAO = DAOFactory.getTeacherDAO();
                        res = getJSONResponse(1,"Lista Materie Completa",teacherDAO.selectAllTeachers());
                        break;
                    case "list-teacher-active":
                        teacherDAO = DAOFactory.getTeacherDAO();
                        res = getJSONResponse(1,"Lista Materie Completa",teacherDAO.selectAllActiveTeachers());
                        break;
                    case "list-course":
                        courseDAO = DAOFactory.getCourseDAO();
                        teacherDAO = DAOFactory.getTeacherDAO();
                        subjectDAO = DAOFactory.getSubjectDAO();
                        List<Course> newList = courseDAO.selectAllCourses();
                        for (Course course : newList) {
                            course.setTeacher(teacherDAO.selectTeacherById(course.getTeacher().getId()));
                            course.setSubject(subjectDAO.selectSubjectById(course.getSubject().getId()));
                        }
                        res = getJSONResponse(1,"Lista Materie Completa",newList);
                        break;
                    case "list-course-active":
                        courseDAO = DAOFactory.getCourseDAO();
                        teacherDAO = DAOFactory.getTeacherDAO();
                        subjectDAO = DAOFactory.getSubjectDAO();
                        List<Course> newActiveList = courseDAO.selectAllActiveCourses();
                        for (Course course : newActiveList) {
                            course.setTeacher(teacherDAO.selectTeacherById(course.getTeacher().getId()));
                            course.setSubject(subjectDAO.selectSubjectById(course.getSubject().getId()));
                        }
                        res = getJSONResponse(1,"Lista Materie Attive",newActiveList);
                        break;
                    case "list-lesson":
                        lessonDAO = DAOFactory.getLessonDAO();

                        List<Lesson> newLessonList = lessonDAO.selectAllLessons();
                        getFullList(newLessonList);
                        res = getJSONResponse(1,"Lista Lezioni Completa",newLessonList);
                        break;
                    case "list-lesson-active":
                        lessonDAO = DAOFactory.getLessonDAO();
                        List<Lesson> newActiveLessonList = lessonDAO.selectAllActiveLessons();
                        getFullList(newActiveLessonList);
                        res = getJSONResponse(1,"Lista Lezioni Attive",newActiveLessonList);
                        break;
                    case "list-user-lesson":
                        if(userId>0) {
                            lessonDAO = DAOFactory.getLessonDAO();
                            List<Lesson> newUserLessonList = lessonDAO.selectAllLessonsByUserId(userId);
                            getFullList(newUserLessonList);
                            res = getJSONResponse(1, "Lista lezioni dell'utente",newUserLessonList);
                        } else {
                            res = getJSONResponse(-1,"Utente non loggato",null);
                        }
                        break;
                    case "list-user-active-lesson":
                        if(userId>0) {
                            lessonDAO = DAOFactory.getLessonDAO();
                            List<Lesson> newUserActiveLessonList = lessonDAO.selectAllActiveLessonsByUserId(userId);
                            getFullList(newUserActiveLessonList);
                            res = getJSONResponse(1, "Lista lezioni attive dell'utente",newUserActiveLessonList);
                        } else {
                            res = getJSONResponse(1,"Utente non loggato",null);
                        }
                        break;
                    case "list-available-courses":
                        res = getJSONResponse(1,"Lista Corsi Disponibili", getAvailableCourses());
                        break;
                    case "list-available-lessons":
                        res = getJSONResponse(1,"Lista Lezioni Disponibili", getAvailableLessons());
                        break;
                    default:
                        res=getJSONResponse(-1,"Request Data Mancante",null);
                }
            }

        } else if (type.equals("action")) {
            String action = request.getParameter("action");
            if(action==null){
                res=getJSONResponse(-1,"Request Action Mancante",null);
            } else {
                switch (action){
                    case "user-register":
                        userDAO = DAOFactory.getUserDAO();
                        user = new User(0,request.getParameter("username"),request.getParameter("email"),request.getParameter("password"),false,false);
                        if(userDAO.emailRegistered(user.getEmail())||userDAO.usernameRegistered(user.getUsername())){
                            res = getJSONResponse(-1,"Username o Email già Registrati", user);
                        } else {
                            user.setId(userDAO.insertUser(user));
                            if(user.getId()<0){
                                res=getJSONResponse(-1,"Impossibile Registrare",user);
                            } else {
                                res=getJSONResponse(1,"Utente Registrato",user);
                            }
                        }
                        break;
                    case "user-login":
                        userDAO = DAOFactory.getUserDAO();
                        user = userDAO.selectUserByEmailPassword(request.getParameter("email"),request.getParameter("password"));
                        if(user==null||!user.isActive()){
                            res = getJSONResponse(-1,"Login Fallito", user);
                        } else {
                            session.setAttribute("username",user.getUsername());
                            session.setAttribute("id",user.getId());
                            session.setAttribute("admin",user.isAdmin());
                            res = getJSONResponse(1,"Login Riuscito", user);
                        }
                        break;
                    case "user-logout":
                        session.invalidate();
                        res = getJSONResponse(1,"Logout Avvenuto Con Successo", null);
                        break;
                    case "user-cancel":
                        if(!((boolean) session.getAttribute("admin")))
                            res = getJSONResponse(-1,"Accesso negato", session.getAttribute("admin"));
                        else {
                            if (request.getParameter("id") == null) {
                                res = getJSONResponse(-1, "Cancellazione Utente Fallita, id mancante", null);
                            } else {
                                userDAO = DAOFactory.getUserDAO();
                                if (!userDAO.cancelUserById(Integer.parseInt(request.getParameter("id")))) {
                                    res = getJSONResponse(-1, "Cancellazione Utente Fallita", request.getParameter("id"));
                                } else {
                                    res = getJSONResponse(1, "Cancellazione Utente Riuscita", request.getParameter("id"));
                                    lessonDAO = DAOFactory.getLessonDAO();
                                    if(!lessonDAO.cancelLessonByUserId(Integer.parseInt(request.getParameter("id")))){
                                        res = getJSONResponse(-1,"Cancellazione Utente Riuscita, lezioni cancellate: 0",request.getParameter("id"));
                                    }
                                }
                            }
                        }
                        break;
                    case "user-activate":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Attivazione Utente Fallita, id mancante", null);
                        } else {
                            userDAO = DAOFactory.getUserDAO();
                            if(!userDAO.activateUserById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Attivazione Utente Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Attivazione Utente Riuscita",request.getParameter("id"));
                            }
                        }
                        break;
                    case "subject-insert":
                        subjectDAO = DAOFactory.getSubjectDAO();
                        Subject newSubject = new Subject(-1,request.getParameter("name"),true);
                        if(subjectDAO.subjectNameRegistered(newSubject.getName())){
                            res = getJSONResponse(-1,"Materia già Registrata",newSubject);
                        } else {
                            newSubject.setId(subjectDAO.insertSubject(newSubject));
                            if(newSubject.getId()<0){
                                res=getJSONResponse(-1,"Impossibile Inserire Materia",newSubject);
                            } else {
                                res=getJSONResponse(1,"Materia Inserita",newSubject);
                            }
                        }
                        break;
                    case "subject-cancel":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Cancellazione Materia Fallita, id mancante", null);
                        } else {
                            subjectDAO = DAOFactory.getSubjectDAO();
                            if(!subjectDAO.cancelSubjectById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Cancellazione Materia Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Cancellazione Materia Riuscita",request.getParameter("id"));
                                courseDAO = DAOFactory.getCourseDAO();
                                lessonDAO = DAOFactory.getLessonDAO();
                                if(!courseDAO.cancelCourseBySubjectId(Integer.parseInt(request.getParameter("id")))){
                                    res = getJSONResponse(-1,"Cancellazione Materia Riuscita, 0 corsi cancellati",request.getParameter("id"));
                                } else if(!lessonDAO.cancelLessonBySubjectId(Integer.parseInt(request.getParameter("id")))){
                                    res = getJSONResponse(-1,"Cancellazione Materia e Corsi Riuscita, 0 lezioni cancellate",request.getParameter("id"));
                                }
                            }
                        }
                        break;
                    case "subject-activate":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Attivazione Materia Fallita, id mancante", null);
                        } else {
                            subjectDAO = DAOFactory.getSubjectDAO();
                            if(!subjectDAO.activateSubjectById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Attivazione Materia Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Attivazione Materia Riuscita",request.getParameter("id"));
                            }
                        }
                        break;
                    case "teacher-insert":
                        teacherDAO = DAOFactory.getTeacherDAO();
                        Teacher newTeacher = new Teacher(-1,request.getParameter("name"),request.getParameter("surname"),true);
                        if (teacherDAO.teacherRegistered(newTeacher.getName(),newTeacher.getSurname()))
                            res = getJSONResponse(-1,"Docente già Inserito",newTeacher);
                        else {
                            newTeacher.setId(teacherDAO.insertTeacher(newTeacher));
                            if (newTeacher.getId() < 0) {
                                res = getJSONResponse(-1, "Impossibile Inserire Docente", newTeacher);
                            } else {
                                res = getJSONResponse(1, "Docente Inserito", newTeacher);
                            }
                        }
                        break;
                    case "teacher-cancel":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Cancellazione Docente Fallita, id mancante", null);
                        } else {
                            teacherDAO = DAOFactory.getTeacherDAO();
                            if(!teacherDAO.cancelTeacherById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Cancellazione Docente Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Cancellazione Docente Riuscita",request.getParameter("id"));
                                courseDAO = DAOFactory.getCourseDAO();
                                lessonDAO = DAOFactory.getLessonDAO();
                                if(!courseDAO.cancelCourseByTeacherId(Integer.parseInt(request.getParameter("id")))){
                                    res = getJSONResponse(-1,"Cancellazione Docente Riuscita, cancellazione corsi fallita",request.getParameter("id"));
                                } else if(!lessonDAO.cancelLessonByTeacherId(Integer.parseInt(request.getParameter("id")))){
                                    res = getJSONResponse(-1,"Cancellazione Docente e Corsi Riuscita, cancellazione lezioni fallita",request.getParameter("id"));
                                }
                            }
                        }
                        break;
                    case "teacher-activate":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Attivazione Docente Fallita, id mancante", null);
                        } else {
                            teacherDAO = DAOFactory.getTeacherDAO();
                            if(!teacherDAO.activateTeacherById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Attivazione Docente Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Attivazione Docente Riuscita",request.getParameter("id"));
                            }
                        }
                        break;
                    case "course-insert":
                        if(request.getParameter("idSubject")==null||request.getParameter("idSubject").equals("")||request.getParameter("idTeacher")==null||request.getParameter("idTeacher").equals("")){
                            res = getJSONResponse(-1,"Creazione Corso Fallita, parametri mancanti",request.getParameter("idSubject") +" " + request.getParameter("idTeacher"));
                        } else {
                            courseDAO = DAOFactory.getCourseDAO();
                            Course newCourse = new Course(-1, new Subject(Integer.parseInt(request.getParameter("idSubject")),"",true), new Teacher(Integer.parseInt(request.getParameter("idTeacher")),"","",true), true);
                            if(courseDAO.courseRegistered(newCourse)){
                                res = getJSONResponse(-1,"Corso già Presente",newCourse);
                            } else {
                                newCourse.setId(courseDAO.insertCourse(newCourse));
                                if (newCourse.getId() < 0) {
                                    res = getJSONResponse(-1, "Impossibile Inserire Corso", newCourse);
                                } else {
                                    res = getJSONResponse(1, "Corso Inserito", newCourse);
                                }
                            }
                        }
                        break;
                    case "course-cancel":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Cancellazione Corso Fallita, id mancante", null);
                        } else {
                            courseDAO = DAOFactory.getCourseDAO();
                            if(!courseDAO.cancelCourseById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Cancellazione Corso Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Cancellazione Corso Riuscita",request.getParameter("id"));
                                lessonDAO = DAOFactory.getLessonDAO();
                                if(!lessonDAO.cancelLessonByCourseId(Integer.parseInt(request.getParameter("id")))){
                                    res = getJSONResponse(-1,"Cancellazione Corso Riuscita, cancellazione lezioni fallita",request.getParameter("id"));
                                }
                            }
                        }
                        break;
                    case "course-activate":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Attivazione Corso Fallita, id mancante", null);
                        } else {
                            courseDAO = DAOFactory.getCourseDAO();
                            teacherDAO = DAOFactory.getTeacherDAO();
                            subjectDAO = DAOFactory.getSubjectDAO();
                            Course activatedCourse = courseDAO.selectCourseById(Integer.parseInt(request.getParameter("id")));
                            if(!teacherDAO.selectTeacherById(activatedCourse.getTeacher().getId()).isActive()){
                                res = getJSONResponse(-1,"Attivazione Corso Fallita, docente inattivo", activatedCourse);
                            } else if(!subjectDAO.selectSubjectById(activatedCourse.getSubject().getId()).isActive()){
                                res = getJSONResponse(-1,"Attivazione Corso Fallita, materia inattiva", activatedCourse);
                            } else if(!courseDAO.activateCourseById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Attivazione Corso Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Attivazione Corso Riuscita",request.getParameter("id"));
                            }
                        }
                        break;
                    case "lesson-insert":
                        if(userId<0){
                            res = getJSONResponse(-1,"Effettua il login per poter poter prenotare",null);
                        } else if(request.getParameter("idCourse")==null||request.getParameter("schedule")==null){
                            res = getJSONResponse(-1,"Creazione Lezione Fallita, parametri mancanti",request.getParameter("idCourse") + " " + request.getParameter("schedule"));
                        } else {
                            lessonDAO = DAOFactory.getLessonDAO();
                            Lesson newLesson = new Lesson(-1, new Course(Integer.parseInt(request.getParameter("idCourse")),null,null,true), new User(userId,"","","",false,true),Integer.parseInt(request.getParameter("schedule")),true);
                            if(lessonDAO.lessonRegistered(newLesson)){
                                res = getJSONResponse(-1,"Lezione non prenotabile",newLesson);
                            } else {
                                newLesson.setId(lessonDAO.insertLesson(newLesson));
                                if (newLesson.getId() < 0) {
                                    res = getJSONResponse(-1, "Impossibile Inserire Lezione", newLesson);
                                } else {
                                    res = getJSONResponse(1, "Lezione Prenotata", newLesson);
                                }
                            }
                        }
                        break;
                    case "lesson-cancel":
                        if(request.getParameter("id")==null){
                            res = getJSONResponse(-1,"Cancellazione Lezione Fallita, id mancante", null);
                        } else {
                            lessonDAO = DAOFactory.getLessonDAO();
                            if(!lessonDAO.cancelLessonById(Integer.parseInt(request.getParameter("id")))){
                                res = getJSONResponse(-1,"Cancellazione Lezione Fallita",request.getParameter("id"));
                            } else {
                                res = getJSONResponse(1,"Cancellazione Lezione Riuscita",request.getParameter("id"));
                            }
                        }
                        break;
                    default:
                        res=getJSONResponse(-1,"Request Action Errata",null);
                }
            }
        } else {
            getJSONResponse(-1,"Request Type Errata",null);
        }


        response.setContentType("text/html");
        PrintWriter writer=response.getWriter();
        writer.write(res);
        writer.close();

    }

    private void getFullList(List<Lesson> newLessonList) {
        CourseDAO courseDAO = DAOFactory.getCourseDAO();
        SubjectDAO subjectDAO = DAOFactory.getSubjectDAO();
        TeacherDAO teacherDAO = DAOFactory.getTeacherDAO();
        UserDAO userDAO = DAOFactory.getUserDAO();
        for(Lesson l: newLessonList){
            l.setUser(userDAO.selectUserById(l.getUser().getId()));
            l.setCourse(courseDAO.selectCourseById(l.getCourse().getId()));
            l.getCourse().setSubject(subjectDAO.selectSubjectById(l.getCourse().getSubject().getId()));
            l.getCourse().setTeacher(teacherDAO.selectTeacherById(l.getCourse().getTeacher().getId()));
        }
    }

    private String getJSONResponse(int res, String msg, Object data){
        Gson g = new Gson();
        return "{\"result\":"+res+",\"message\":\""+msg+"\",\"data\":"+g.toJson(data)+"}";
    }

    private String getPage(ServletContext ctx, String page) throws IOException{
        InputStream is = ctx.getResourceAsStream(page);
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String buffer;
            StringBuilder text = new StringBuilder();
            while ((buffer = reader.readLine()) != null)
                text.append(buffer);
            return text.toString();
        }
        else return "Missing Component";
    }

    private List<List<Course>> getAvailableCourses() {
        CourseDAO courseDAO = DAOFactory.getCourseDAO();
        TeacherDAO teacherDAO = DAOFactory.getTeacherDAO();
        SubjectDAO subjectDAO = DAOFactory.getSubjectDAO();
        LessonDAO lessonDAO = DAOFactory.getLessonDAO();
        List<List<Course>> list = new ArrayList<>();
        List<Course> activeCourses = courseDAO.selectAllActiveCourses();
        for(Course c: activeCourses){
            c.setTeacher(teacherDAO.selectTeacherById(c.getTeacher().getId()));
            c.setSubject(subjectDAO.selectSubjectById(c.getSubject().getId()));
        }
        List<Lesson> activeLessons = lessonDAO.selectAllActiveLessons();
        for(Lesson l: activeLessons){
            l.setCourse(courseDAO.selectCourseById(l.getCourse().getId()));
        }
        for(int i=0;i<SCHEDULE_SIZE;i++){
            list.add(new ArrayList<>(activeCourses));
        }
        for(Lesson l: activeLessons){
            List<Course> remove = new ArrayList<>();
            for(Course c: list.get(l.getSchedule())) {
                if(c.getId()==l.getCourse().getId()||c.getTeacher().getId()==l.getCourse().getTeacher().getId()){
                    remove.add(c);
                }
            }
            list.get(l.getSchedule()).removeAll(remove);
        }
        return list;
    }

    private List<Scheduler> getAvailableLessons(){
        CourseDAO courseDAO = DAOFactory.getCourseDAO();
        TeacherDAO teacherDAO = DAOFactory.getTeacherDAO();
        SubjectDAO subjectDAO = DAOFactory.getSubjectDAO();
        LessonDAO lessonDAO = DAOFactory.getLessonDAO();
        List<Scheduler> schedulers = new ArrayList<>();
        List<List<Course>> list = new ArrayList<>();
        List<Course> activeCourses = courseDAO.selectAllActiveCourses();
        for(Course c: activeCourses){
            c.setTeacher(teacherDAO.selectTeacherById(c.getTeacher().getId()));
            c.setSubject(subjectDAO.selectSubjectById(c.getSubject().getId()));
            schedulers.add(new Scheduler(c));
        }
        List<Lesson> activeLessons = lessonDAO.selectAllActiveLessons();
        for(Lesson l: activeLessons){
            for(Scheduler s: schedulers){
                if(l.getCourse().getId()==s.getCourse().getId()){
                    s.setFalse(l.getSchedule());
                }
            }
        }
        for(Scheduler s: schedulers){
            for(int i=0;i<=SCHEDULE_NOW;i++){
                s.setFalse(i);
            }
        }
        return  schedulers;
    }


}