<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-transparent navbar-absolute fixed-top ">
    <div class="container-fluid">
        <div class="navbar-wrapper">
            <a class="navbar-brand" href="index.jsp"><img src="assets/img/logo_full.png" style="height:100%" alt="sen6 logo"/></a>
        </div>
        <button class="navbar-toggler" type="button" data-toggle="collapse" aria-controls="navigation-index" aria-expanded="false" aria-label="Toggle navigation">
            <span class="sr-only">Toggle navigation</span>
            <span class="navbar-toggler-icon icon-bar"></span>
            <span class="navbar-toggler-icon icon-bar"></span>
            <span class="navbar-toggler-icon icon-bar"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end">
            <ul class="navbar-nav">
                <%
                    String username= (String)session.getAttribute("username");
                    if (username!= null){
                %>
                <li class="nav-item dropdown">
                    <a class="nav-link" href="#" id="navbarDropdownProfile" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="material-icons">person</i>
                        <p class="d-lg-none d-md-block">
                            <%= username%>
                        </p>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownProfile">
                        <a class="dropdown-item" href="#">Profilo</a>
                        <a class="dropdown-item" href="#">Impostazioni</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#" id="buttonLogout">Log out</a>
                    </div>
                </li>
                <% } else { %>
                <li class="nav-item">
                    <a class="nav-link" href="register.jsp">Registrazione</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link btn btn-primary text-white" role="button" href="login.jsp">Login</a>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>