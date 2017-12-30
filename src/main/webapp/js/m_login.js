$(document).ready(function () {
    //用户登录
    var btn_login=$("#btn_login");
    var InputUsername=$("#InputUsername");
    InputUsername.val("");
    var InputUserPsd=$("#InputUserPsd");
    InputUserPsd.val("");
    btn_login.attr("disabled", "disabled");
    btn_login.attr("class", "btn btn-block btn-lg ");
    $(document).bind('input propertychange', function(){
        var user=InputUsername.val();
        var psd=InputUserPsd.val();
        if (user==""||psd=="") {
            btn_login.attr("class", "btn btn-block btn-lg");
            btn_login.attr("disabled","disabled");
        }else {
            btn_login.removeAttr("disabled");
            btn_login.attr("class", "btn  btn-success btn-block btn-lg");
        }
    });

    btn_login.click(function () {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/manage/users/login.json",
            data:"username="+$("#InputUsername").val()+"&password="+$("#InputUserPsd").val(),
            error: function(request) {
                console.log(request);
            },
            success: function(data) {
                console.log(data);
                if (data.status==0){
                    console.log("登陆成功");
                    window.location.replace("m_index.html");
                }
            }
        });
    });


});