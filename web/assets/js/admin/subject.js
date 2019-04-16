var adminPageTableSubject = null;
$(document).ready(function () {
    fillTableSubject();
});

function fillTableSubject() {
    getData("list-subject", function (r) {
        var d = [];
        for (var i in r) {
            var active = '';
            if(r[i].active) {
                active = '<button class="btn btn-sm btn-success button-cancel-subject" onclick="cancelSubject(' + r[i].id + ')">Attiva</button>';
            } else {
                active = '<button class="btn btn-sm btn-error button-activate-subject" onclick="activateSubject( ' + r[i].id + ')">Disattivata</button>';
            }
            d.push([r[i].id, r[i].name, active]);
        }
        if (adminPageTableSubject == null) {
            adminPageTableSubject = $('#adminTableSubject').DataTable({data: d});
        } else {
            adminPageTableSubject.clear().draw().rows.add(d).draw();
        }
    });
}
function cancelSubject(subjectId) {
    doSimpleAction("subject-cancel", subjectId, function (res) {
        fillTableSubject();
    });
}
function activateSubject(subjectId) {
    doSimpleAction("subject-activate", subjectId, function (res) {
        fillTableSubject();
    });
}


$("#buttonAddSubject").click(function () {
    var name = $("#inputAddSubject").val();
    $.ajax({
        method: "POST",
        url: "controller",
        data: {
            type: "action",
            action: "subject-insert",
            name: name
        },
        success: function (res){
            r=JSON.parse(res);
            alert(r.message);
            fillTableSubject();
            if(r.result==1) {
                $("#inputAddSubject").val("");
            }
        }
    })
});

