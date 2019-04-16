var availableLessons = null;
var pageTableCourses = null;
var pageTableSchedule = null;
var userLesson = null;

$(document).ready(function () {
    getAvailableLessons();
});

function getAvailableLessons() {
    getData("list-available-lessons", function (r) {
        availableLessons=r;
        fillTableCourses();
        fillTableSchedule(-1);
    });
}

function fillTableCourses(){
    getData("list-user-active-lesson",function (r) {
        l = availableLessons;
        console.log(r);
        console.log(l);
        var d = [];
        for(i in l){
            for(j in r){
                if(l[i].schedule[r[j].schedule]){
                    l[i].schedule[r[j].schedule]=false;
                    l[i].availableLessons--;
                }
            }
            button= '<button class="btn-sm btn btn-success btn-block" onclick=fillTableSchedule('+i+')>'+l[i].availableLessons+'</button>';
            if(l[i].availableLessons<=0){
                button= '<button class="btn-sm btn btn-danger btn-block" onclick=fillTableSchedule(-1)>'+r[i].availableLessons+'</button>';
            }
            d.push([l[i].course.subject.name, l[i].course.teacher.surname, l[i].course.teacher.name,button]);
        }
        if (pageTableCourses == null) {
            pageTableCourses = $('#coursesTable').DataTable({data: d});
        } else {
            pageTableCourses.clear().draw().rows.add(d).draw();
        }
    });

}

function fillTableSchedule(j){
    console.log(availableLessons[j]);
    if(j<0){
        $("#scheduleTitle").html("Seleziona un corso disponibile...");
    } else {
        var l = availableLessons[j];
        console.log(l);
        $("#scheduleTitle").html(l.course.subject.name + " di " + l.course.teacher.surname + " " + l.course.teacher.name);
        var d = [];
        for(var i in l.schedule){
            var button = '<button class="btn btn-sm btn-success btn-block" onclick=insertLesson('+l.course.id+','+i+')>Prenota</button>';
            if(l.schedule[i]){
                d.push(['<span class="text-hide">'+Math.floor(i/4)+'</span>' + getDay(i),getTime(i),button]);
            }
        }
        if (pageTableSchedule == null) {
            pageTableSchedule = $('#scheduleTable').DataTable({data: d});
        } else {
            pageTableSchedule.clear().draw().rows.add(d).draw();
        }
    }
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
            console.log(r);
            getAvailableLessons();
        }
    })
}