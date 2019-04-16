var scheduleNow = 9;
function getComponent(name,container,callback){
    $.ajax({
        method: "POST",
        url: "controller",
        data: {
            type: "component",
            component: name
        },
        success: function (res){
            $(container).html(res);
            if(callback!=null)
                callback(res);
        }
    });
}

function getData(name,callback){
    $.ajax({
        method: "POST",
        url: "controller",
        data: {
            type: "data",
            data: name
        },
        success: function (res) {
            var r = JSON.parse(res);
            if(r.result&&callback!=null){
                callback(r.data);
            } else {
                console.log(r);
            }
        }
    });
}

function doSimpleAction(action,id,callback){
    $.ajax({
        method: "POST",
        url: "controller",
        data: {
            type: "action",
            action: action,
            id: id
        },
        success: function (res) {
            var r = JSON.parse(res);
            alert(r.message);
            if(r.result&&callback!=null){
                callback(r.data);
            } else {
                console.log(r);
            }
        }
    })

}
var arrayDay=["Lunedì","Martedì","Mercoledì","Giovedì","Venerdì"];
function getDay(schedule){
    return arrayDay[Math.floor(schedule/4)];
}
function getTime(schedule){
    return 15+schedule%4;
}

$(document).ready(function () {
    getComponent("homepage","#mainContainer",null);
    var selectedButton = $('.logo');
    $('.clicked').click(function(){
        $(selectedButton).removeClass("active");
        $(this).addClass("active");
        selectedButton = $(this);
    });

    $("#buttonCalendar").click(function () {
        getComponent("calendar","#mainContainer",null);
    });
    $("#buttonHistory").click(function () {
        getComponent("history","#mainContainer",null);
    });
    $("#buttonCourses").click(function () {
        getComponent("courses","#mainContainer",null);
    });

});



