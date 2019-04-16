<%--
  Created by IntelliJ IDEA.
  User: Beppe
  Date: 07/03/2019
  Time: 09:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<%@ include file="components/header.html" %>
<body class="">
<div class="wrapper">
    <div class="main-panel" style="width:100%">
        <%@include file="components/navbar.jsp" %>
        <div class="content">
            <div class="container-fluid">
                <div class="row justify-content-md-center"><!-- align-items-center h100 da riprovare-->
                    <div class="col-md-6 col-lg-5">
                        <div class="card">
                            <div class="card-header card-header-primary">
                                <h4 class="card-title">Registrazione</h4>
                                <p class="card-category">Completa tutti i campi per registrarti</p>
                            </div>
                            <div class="card-body">
                                <div>
                                    <div class="row">
                                        <div class="col-md-7">
                                            <div class="form-group">
                                                <label class="bmd-label-floating">Username</label>
                                                <input type="text" id="inputUsername" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-7">
                                            <div class="form-group">
                                                <label class="bmd-label-floating">Indirizzo Email</label>
                                                <input type="email" id="inputEmail" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-7">
                                            <div class="form-group">
                                                <label class="bmd-label-floating">Password</label>
                                                <input type="password" id="inputPassword" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-7">
                                            <div class="form-group">
                                                <label class="bmd-label-floating">Conferma Password</label>
                                                <input type="password" id="inputConfermaPassword" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row align-items-end">
                                        <div class="col-md-6">
                                            <label class="bmd-label-floating">Hai già un Account? <a href="login.jsp">Accedi</a></label>
                                        </div>
                                        <div class="col-md-6">
                                        <button type="submit" id="buttonRegistrazione" class="btn btn-primary pull-right">
                                            Registrati
                                        </button>
                                        </div>
                                    </div>

                                    <div class="clearfix"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="components/footer.html" %>
        <script src="assets/js/main.js"></script>
        <script src="assets/js/register.js"></script>
        <script>
            $(document).ready(function () {
                $("a[href='"+window.location.hash+"']").click();
            });
        </script>
    </div>
</div>

</body>

</html>

