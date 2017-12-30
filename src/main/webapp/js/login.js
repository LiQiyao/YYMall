$(document).ready(function () {

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');
    var btn_login=$("#loginSub");

    $.ajax({
        url: "http://"+urla+"/users/info.json",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function(result){
            console.log(result);
            if (result.status!==10){
                window.location.replace("main.html");
            }
        }
    });

    btn_login.on("click",function (event) {
        event.preventDefault();//使a自带的方法失效
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/users/login.json",
            data:$('#loginForm').serialize(),
            error: function(request) {
                alert("Connection error");
            },
            success: function(data) {
                console.log(data);
                if (data.status===0){
                    successModal.modal('show');
                    successModal.css("padding-right","0");
                    successModal.on('hide.bs.modal', function () {
                        self.location=document.referrer;
                    })
                }else if(data.status===1){
                    unknownModal.modal('show');
                    unknownModal.css("padding-right","0");
                    $("#unknownModal .alert").text(data.msg+"请重试");
                    unknownModal.on('hide.bs.modal', function () {
                        location.reload();
                    })
                }
            }
        });
    });


    var InputUsername=$("#username");
    InputUsername.val("");
    var InputUserPsd=$("#password");
    InputUserPsd.val("");
    btn_login.attr("disabled", "disabled");
    btn_login.attr("class", "btn btn-block btn-lg ");

    $(document).bind('input propertychange', function(){
        var user=InputUsername.val();
        var psd=InputUserPsd.val();
        if (user===""||psd==="") {
            btn_login.attr("class", "btn btn-block btn-lg");
            btn_login.attr("disabled","disabled");
        }else {
            btn_login.removeAttr("disabled");
            btn_login.attr("class", "btn  btn-success btn-block btn-lg");
        }
    });


});