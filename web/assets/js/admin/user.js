var adminPageTableUser = null;
$(document).ready(function () {
    fillTableUser();
});
function fillTableUser() {
    getData("list-user", function (r) {
        var d = [];
        for (var i in r) {
            var active = r[i].active ? '<button class="btn btn-sm btn-success button-cancel-user" onclick="cancelUser(' + r[i].id + ')">Attivo</button>' : '<button class="btn btn-sm btn-error button-activate-user" onclick="activeteUser(' + r[i].id + ')">Disattivato</button>';
            d.push([r[i].id, r[i].username, r[i].email, r[i].admin, active]);
        }
        if (adminPageTableUser == null) {
            adminPageTableUser = $('#adminTableUser').DataTable({data: d});
        } else {
            adminPageTableUser.clear().draw().rows.add(d).draw();
        }
    })
}

function cancelUser(userId){
    doSimpleAction("user-cancel", userId, function (res) {
        console.log(res);
        fillTableUser();
    });
}
function activeteUser(userId) {
    doSimpleAction("user-activate", userId, function (res) {
        console.log(res);
        fillTableUser();
    });
}