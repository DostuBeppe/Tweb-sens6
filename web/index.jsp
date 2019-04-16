<%--
  Created by IntelliJ IDEA.
  User: stefa
  Date: 05/03/2019
  Time: 10:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<%@ include file="components/header.html" %>
<body class="">
<div class="wrapper ">
    <%@ include file="components/sidebar.jsp" %>

    <div class="main-panel">
        <%@ include file="components/navbar.jsp" %>
        <div class="content">
            <div class="container-fluid" id="mainContainer">

            </div>
        </div>
        <%@ include file="components/footer.html" %>
        <script src="assets/js/register.js"></script>
        <script src="assets/js/login.js"></script>
        <% if(session.getAttribute("email") != null)%>
            <script src="assets/js/logout.js"></script>
    </div>
</div>
<script src="assets/js/main.js"></script>
<script src="assets/js/admin.js"></script>
<script>
    $(document).ready(function () {
        $("a[href='"+window.location.hash+"']").click();
    });
    window.onhashchange = function() {
        $("a[href='"+window.location.hash+"']").click();
    }
</script>

</body>
</html>
