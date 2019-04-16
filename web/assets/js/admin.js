$(document).ready(function () {
    $("#buttonUserList").click(function () {
        getComponent("admin-table-user", "#mainContainer", null);
    });
    $("#buttonSubjectList").click(function () {
        getComponent("admin-table-subject", "#mainContainer", null);
    });
    $("#buttonTeacherList").click(function () {
        getComponent("admin-table-teacher", "#mainContainer", null);
    });
    $("#buttonCourseList").click(function () {
        getComponent("admin-table-course", "#mainContainer",null);
    });
    $("#buttonLessonList").click(function () {
        getComponent("admin-table-lesson", "#mainContainer", null);
    });
});

