<%--
  Created by IntelliJ IDEA.
  User: matte
  Date: 07/03/2019
  Time: 09:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<%@ include file="components/header.html" %>
<body class="">
<div class="wrapper ">
    <div class="main-panel" style="width:100%">
        <%@ include file="components/navbar.jsp" %>
        <div class="content">
            <div class="container-fluid">
                <%
                    if (session.getAttribute("username") == null) {
                %>
                <div class="row justify-content-center">
                    <div class="col-md-6 col-lg-5">
                        <div class="card">
                            <div class="card-header card-header-primary">
                                <h4 class="card-title">Accesso</h4>
                                <p class="card-category">Inserisci le tue credenziali</p>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-7">
                                        <div class="form-group">
                                            <label class="bmd-label-floating">Inserisci la tua e-mail</label>
                                            <input id="inputEmail" type="email" class="form-control">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-7">
                                        <div class="form-group">
                                            <label class="bmd-label-floating">Inserisci la tua password</label>
                                            <input id="inputPassword" type="password" class="form-control">
                                        </div>
                                    </div>
                                </div>
                                <div class="row align-items-end">
                                    <div class="col-md-7">
                                        <label class="bmd-label-floating">Non hai un account? <a href="register.jsp">Registrati</a></label>
                                    </div>
                                    <div class="col-md-5">
                                        <button id="buttonLogin" class="btn btn-primary pull-right">Accedi</button>
                                    </div>
                                </div>

                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <% } else {
                out.println("Sei già loggato!");
            }
            %>
        </div>
        <%@ include file="components/footer.html" %>
    </div>
</div>
<script src="assets/js/main.js"></script>
<script src="assets/js/login.js"></script>
<script>
    $(document).ready(function () {
        $("a[href='"+window.location.hash+"']").click();
    });
</script>
</body>
</html>