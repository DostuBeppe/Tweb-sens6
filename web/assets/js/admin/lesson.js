var adminPageTableLesson = null;
$(document).ready(function () {
    fillTableLesson();
});
function fillTableLesson() {
    getData("list-lesson", function (r) {
        var d = [];
        for (var i in r) {
            var active  = '';
            if(r[i].schedule<=scheduleNow){
                active = '<span class="text-secondary">Svolta</span>';
            } else {
                active = '<span class="text-success">Prenotata</span>';
            }
            if(!r[i].active){
                active = '<span class="text-danger">Annullata</span>';
            }
            var schedule = getDay(r[i].schedule)+", "+ getTime(r[i].schedule)+" - "+(getTime(r[i].schedule)+1);
            d.push([r[i].id, r[i].user.username, r[i].course.subject.name,r[i].course.teacher.surname, r[i].course.teacher.name,schedule, active]);
        }
        if (adminPageTableLesson == null) {
            adminPageTableLesson = $('#adminTableLesson').DataTable({data: d});
        } else {
            adminPageTableLesson.clear().draw().rows.add(d).draw();
        }
    })
}