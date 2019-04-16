var adminPageTableTeacher = null;
$(document).ready(function () {
    fillTableTeacher();
});
function fillTableTeacher() {
    getData("list-teacher", function (r) {
        var d = [];
        for (var i in r) {
            var active = '';
            if(r[i].active){
                active = '<button class="btn btn-sm btn-success button-cancel-teacher" onclick="cancelTeacher(' + r[i].id + ')">Attivo</button>';
            } else {
                active = '<button class="btn btn-sm btn-error button-activate-teacher" onclick="activateTeacher(' + r[i].id + ')">Disattivata</button>';
            }
            d.push([r[i].id, r[i].name, r[i].surname, active])
        }
        if (adminPageTableTeacher == null) {
            adminPageTableTeacher = $('#adminTableTeacher').DataTable({data: d});
        } else {
            adminPageTableTeacher.clear().draw().rows.add(d).draw();
        }
    });
}

function cancelTeacher(teacherId) {
    doSimpleAction("teacher-cancel", teacherId, function (res) {
        fillTableTeacher();
    });
}
function activateTeacher(teacherId) {
    doSimpleAction("teacher-activate", teacherId, function (res) {
        fillTableTeacher();
    });
}

$("#buttonAddTeacher").click(function () {
    var name = $("#inputAddNameTeacher").val();
    var surname = $("#inputAddSurnameTeacher").val();
    $.ajax({
        method: "POST",
        url: "controller",
        data: {
            type: "action",
            action: "teacher-insert",
            name: name,
            surname : surname
        },
        success: function (res){
            r=JSON.parse(res);
            alert(r.message);
            fillTableTeacher();
            if(r.result==1) {
                $("#inputAddNameTeacher").val("");
                $("#inputAddSurnameTeacher").val("");
            } else{
                console.log(r);
            }
        }
    })
});