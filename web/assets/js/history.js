var pageTableHistory = null;
function setNull(){
    pageTableHistory = null;
}
$(document).ready(function () {
    setNull();
    fillTableHistory();
});
function fillTableHistory() {
    getData("list-user-lesson", function (r) {
        var d = [];
        for (var i in r) {
            var active = r[i].active;
            if(!active)
                active = '<label class="text-danger">Annullata</label>';
            else if (r[i].schedule>scheduleNow)
                active = '<button class="btn btn-sm btn-success button-cancel-lesson" onclick="deleteLesson(' + r[i].id + ')">Annulla Prenotazione</button>';
            else active = '<label class="text-secondary">Effettuata</label>';
            var day = '<span class="text-hide">'+Math.floor(r[i].schedule/4)+'</span>' + getDay(r[i].schedule) + ', ' + getTime(r[i].schedule) + ':00';
            d.push([r[i].course.subject.name, r[i].course.teacher.surname, r[i].course.teacher.name, day, active]);
        }
        if (pageTableHistory == null) {
            pageTableHistory = $('#historyTable').DataTable({data: d});
        } else {
            pageTableHistory.clear().draw().rows.add(d).draw();
        }
    });
}

function deleteLesson(id) {
    doSimpleAction("lesson-cancel",id,function () {
        fillTableHistory();
    });
}