$(document).ready(function () {

    var userId;

    getUserInfo();

    $("#saveEmail").click(function () {
        updateEmail();
    });

    $("#savePhone").click(function () {
        updatePhone();
    });

    function updateEmail() {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/users/info/update.json",
            data:"email="+$("#inputEmail").val()+"&id="+userId,
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
                getUserInfo();
                $('#changEmailModal').modal('hide');
            }
        });
    }

    function updatePhone() {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/users/info/update.json",
            data:"phone="+$("#inputPhone").val()+"&id="+userId,
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
                getUserInfo();
                $('#changPhoneModal').modal('hide');
            }
        });
    }

    function getUserInfo() {
        $.ajax({
            /*url: "./json/users/info.json",*/
            url: "http://"+urla+"/users/info.json",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                $("#username").text(result.data.username);
                $("#email").text(result.data.email);
                $("#phone").text(result.data.phone);
                userId=result.data.id;
            }
        });
    }
});