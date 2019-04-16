var availableCourses = null;
var userLessons = null;
var selectedSchedule = null;
var pageTableSchedule = null;
var selected = null;
$(document).ready(function () {
    getAvailableCourses();
    fillCardTitle(selectedSchedule);
});
function getAvailableCourses() {
    getData("list-available-courses", function (r) {
        availableCourses=r;
        fillTableCalendar();
        if (selectedSchedule!=null)
            fillTableSchedule(selectedSchedule);
    });
}
function fillCardTitle(schedule) {
    if (schedule==null)
        $('#schedule').text("Giorno ed Orario");
    else{
        $('#schedule').text(getDay(schedule) + " " + getTime(schedule)+"-"+(getTime(schedule)+1));
        fillTableSchedule(schedule);
    }
}
function fillTableSchedule(schedule) {
    var d = availableCourses[schedule];
    var s = [];
    for (var i in d) {
        var active = '<button class="btn btn-sm btn-success button-insert-lesson" data-idCourse="' + d[i].id + '" data-s="'+schedule+'">Prenota</button>';
        s.push([d[i].subject.name, d[i].teacher.surname,d[i].teacher.name,active]);
    }
    if (pageTableSchedule == null) {
        pageTableSchedule = $('#scheduleTable').DataTable({data: s});
    } else {
        pageTableSchedule.clear().draw().rows.add(s).draw();
    }
    $(".button-insert-lesson").click(function () {
        insertLesson($(this).attr("data-idCourse"),$(this).data("s"));
    })
}
function fillTableCalendar() {
    var l=availableCourses;
    for(var i in l){
        var cell = $('[data-schedule='+i+']');
        cell.removeClass();
        if(i<scheduleNow){
            cell.addClass("btn-secondary disabled");
        } else {
            if(l[i].length>0){
                cell.addClass("btn-success enabled");
                cell.text(l[i].length);
            } else {
                cell.addClass("btn-danger disabled");
            }
        }
    }
    getData("list-user-active-lesson",function (r) {
        userLessons = r;
        for(var i in r){
            if(r[i].schedule>=scheduleNow){
                var cell = $('[data-schedule='+r[i].schedule+']');
                cell.removeClass();
                cell.addClass("btn-warning enabled");
            }
        }
    });
    $(".enabled").click(function () {
        var data=$(this).attr("data-schedule");
        $(selected).removeClass("active");
        selected=this;
        $(selected).addClass("active");
        fillCardTitle(data);
        selectedSchedule=data;
    });
}

function insertLesson(idCourse,schedule){
    $.ajax({
        method: "POST",
        url: "controller",
        data: {
            type: "action",
            action: "lesson-insert",
            idCourse: idCourse,
            schedule: schedule
        },
        success: function (res) {
            var r = JSON.parse(res);
            alert(r.message);
            getAvailableCourses();
        }
    })

}