$(document).ready(function () {
    $( "#buttonLogin" ).click(function() {
        var email=$("#inputEmail").val();
        var password=$("#inputPassword").val();
        console.log("login.js");
        $.ajax({
            method: "POST",
            url: "controller",
            data: {
                type: "action",
                action: "user-login",
                email: email,
                password: password
            },
            success:
                function (res){
                    var r=JSON.parse(res);
                    alert(r.message);
                    if(r.result==1){
                        console.log(res);
                        window.location.href="index.jsp";
                    } else{
                        console.log(res);
                    }
                }
        });


    });
});