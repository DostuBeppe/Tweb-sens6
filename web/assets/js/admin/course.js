var adminPageTableCourse = null;
$(document).ready(function () {
    fillTableCourse();
    fillSelectPickerSubject();
    fillSelectPickerTeacher();
});
//-----Course
function fillTableCourse() {
    getData("list-course", function (r) {
        var d = [];
        for (var i in r) {
            var active = '';
            if(r[i].active){
                active = '<button class="btn btn-sm btn-success button-cancel-course" onclick="cancelCourse(' + r[i].id + ')">Attivo</button>';
            } else {
                active = '<button class="btn btn-sm btn-error button-activate-course" onclick="activateCourse(' + r[i].id + ')">Disattivato</button>';
            }
            d.push([r[i].id, r[i].subject.name, r[i].teacher.surname+" "+r[i].teacher.name, active])
        }
        if (adminPageTableCourse == null) {
            adminPageTableCourse = $('#adminTableCourse').DataTable({data: d});
        } else {
            adminPageTableCourse.clear().draw().rows.add(d).draw();
        }
    });
}

function cancelCourse(courseId){
    doSimpleAction("course-cancel", courseId, function (res) {
        fillTableCourse();
    });
}

function activateCourse(courseId) {
    doSimpleAction("course-activate", courseId, function (res) {
        fillTableCourse();
    });
}

function fillSelectPickerSubject() {
    getData("list-subject-active", function (r) {
        for (subject in r) {
            $("#listSubjectActive").append("<option data-id='" + r[subject].id + "'>" + r[subject].name + "</option>");
        }
        $("#listSubjectActive").selectpicker();
    })
}

function fillSelectPickerTeacher() {
    getData("list-teacher-active", function (r) {
        for (teacher in r) {
            $("#listTeacherActive").append("<option data-id='" + r[teacher].id + "'>" + r[teacher].surname + " " + r[teacher].name + "</option>");
        }
        $("#listTeacherActive").selectpicker();
    })
}


$("#buttonAddCourse").click(function () {
    var subject = $("#listSubjectActive>option:selected").attr('data-id');
    var teacher = $("#listTeacherActive>option:selected").attr('data-id');
    $.ajax({
        method: "POST",
        url: "controller",
        data: {
            type: "action",
            action: "course-insert",
            idSubject: subject,
            idTeacher: teacher
        },
        success: function (res) {
            r = JSON.parse(res);
            alert(r.message);
            fillTableCourse();
            if(r.result==-1){
                console.log(r);
            }
        }
    })
});
