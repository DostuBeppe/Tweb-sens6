<div class="sidebar" data-color="purple" data-background-color="white">
    <!--
      Tip 1: You can change the color of the sidebar using: data-color="purple | azure | green | orange | danger"

      Tip 2: you can also add an image using data-image tag
  -->
    <div class="logo">
        <a href="#home" class="simple-text logo-normal text-primary clicked">
            Dashboard
        </a>
    </div>
    <div class="sidebar-wrapper">
        <ul class="nav">
            <li class="nav-item text-center">
                <h4 class="text-primary">Prenota</h4>
            </li>
            <li class="nav-item clicked">
                <a class="nav-link" href="#calendario" id="buttonCalendar">
                    <i class="material-icons">calendar_today</i>
                    <p>Calendario</p>
                </a>
            </li>
            <li class="nav-item clicked">
                <a class="nav-link" href="#corsi" id="buttonCourses">
                    <i class="material-icons">view_list</i>
                    <p>Corsi</p>
                </a>
            </li>
            <li class="nav-divider pl-4 pr-4"><hr></li>
            <% if (session.getAttribute("username")!=null) {%>
            <li class="nav-item clicked">
                <a class="nav-link" href="#storico" id="buttonHistory">
                    <i class="material-icons">assignment</i>
                    <p>Storico Prenotazioni</p>
                </a>
            </li>
            <li class="nav-divider pl-4 pr-4"><hr></li>

            <% } %>
            <% if(session.getAttribute("admin")!=null && (boolean)session.getAttribute("admin")){%>

            <li class="nav-item text-center">
                <h4 class="text-primary">Amministrazione</h4>
            </li>
            <li class="nav-item clicked">
                <a class="nav-link" href="#listautenti" id="buttonUserList">
                    <p>Visualizza Utenti</p>
                </a>
            </li>
            <li class="nav-item clicked">
                <a class="nav-link" href="#listalezioni" id="buttonLessonList">
                    <p>Visualizza Lezioni</p>
                </a>
            </li>
            <li class="nav-item clicked">
                <a class="nav-link" href="#listacorsi" id="buttonCourseList">
                    <p>Visualizza Corsi</p>
                </a>
            </li>
            <li class="nav-item clicked">
                <a class="nav-link" href="#listadocenti" id="buttonTeacherList">
                    <p>Visualizza Docenti</p>
                </a>
            </li>
            <li class="nav-item clicked">
                <a class="nav-link" href="#listamaterie" id="buttonSubjectList">
                    <p>Visualizza Materie</p>
                </a>
            </li>
            <% }%>
            <!-- Visualizza Lezioni, Visualizza Utenti, Visualizza Materie, Visualizza Corsi -->
        </ul>
    </div>
</div>