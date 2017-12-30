$(document).ready(function () {

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

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');
    var btn_login=$("#registerSub");

    btn_login.on("click",function (event) {
        event.preventDefault();//使a自带的方法失效
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/users/register.json",
            data:$('#registerForm').serialize(),
            error: function(request) {
                alert("Connection error");
            },
            success: function(data) {
                if (data.status===0){
                    successModal.modal('show');
                    successModal.css("padding-right","0");
                    $("#successModal .alert").text(data.msg+"返回首页");
                    successModal.on('hide.bs.modal', function () {
                        window.location.replace("main.html");
                    })
                }else {
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
    var InputUserPsd2=$("#password2");
    InputUserPsd2.val("");
    var email=$("#email");
    email.val("");
    var phone=$("#phone");
    phone.val("");
    var question=$("#question");
    question.val("");
    var answer=$("#answer");
    answer.val("");
    btn_login.attr("disabled", "disabled");
    btn_login.attr("class", "btn btn-block btn-lg ");

    $(document).bind('input propertychange', function(){
        var user=InputUsername.val();
        var psd=InputUserPsd.val();
        var psd2=InputUserPsd2.val();
        var e=email.val();
        var p=phone.val();
        var q=question.val();
        var a=answer.val();

        if (user===""||psd===""||psd2===""||e===""||p===""||q===""||a===""||psd!==psd2) {
            btn_login.attr("class", "btn btn-block btn-lg");
            btn_login.attr("disabled","disabled");
        } else {
            btn_login.removeAttr("disabled");
            btn_login.attr("class", "btn  btn-success btn-block btn-lg");
        }
        if (psd!==psd2){
            $("#psdTip").show();
        }else {
            $("#psdTip").hide();
        }
    });

});