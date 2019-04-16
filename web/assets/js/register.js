$(document).ready(function () {
    $("#buttonRegistrazione").click(function () {
        var username=$("#inputUsername").val();
        var email = $("#inputEmail").val();
        var password = $("#inputPassword").val();
        var checkPassword=$("#inputConfermaPassword").val();

        if(password===checkPassword){
            $.ajax({
                method: "POST",
                url: "controller",
                data: {
                    type: "action",
                    action: "user-register",
                    username: username,
                    email: email,
                    password: password
                }
            }).done(function( res ) {
                var r=JSON.parse(res);
                alert(r.message);
                if (r.result==1)
                    window.location.href="login.jsp";
                else
                    console.log(r);
            });
        }
    });
});