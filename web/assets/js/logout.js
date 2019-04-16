$(document).ready(function () {
    $("#buttonLogout" ).click(function() {
        console.log("logout.js");
        $.ajax({
            method: "POST",
            url: "controller",
            data: {
                type: "action",
                action:"user-logout"
            },
            success:
                function (res){
                    r=JSON.parse(res);
                    alert(r.message)
                    if(r.result==1){
                        console.log(r);
                        window.location.href="index.jsp";
                    } else{
                        console.log(r);
                    }
                }
        });
    });
});