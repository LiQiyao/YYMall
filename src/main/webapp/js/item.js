$(document).ready(function () {
    //获取url
    var loc = location.href;
    var n1 = loc.length;//地址的总长度
    var n2 = loc.indexOf("=");//取得=号的位置
    var id = decodeURI(loc.substr(n2+1, n1-n2));//从=号后面的内容
    var imgArr=new Array();

    var imgList=$(".list ul");


    $.ajax({
        /*url: "./json/product/"+"29"+"/detail.json",*/
        url: "http://"+urla+"/product/"+id+"/detail.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            console.log(result);
            $("title").text(result.data.name+"-筑紫优品");
            $(".name").text(result.data.name);
            $(".desc").text(result.data.subtitle);
            $(".num").text(result.data.price);
            $(".stocknum").text(result.data.stock);
            $(".panel-body").append(result.data.detail);
            imgArr=result.data.subImages.split(',');
            console.log(imgArr);

            $(".view img").attr("src","http://"+imgurl+"/"+imgArr[0]);

            for (var i=0;i<5;i++){
                var li=$("<li></li>");
                var img=$("<img src='http://"+imgurl+"/"+imgArr[i]+"'>");
                li.append(img);
                imgList.append(li);
            }

            imgList.on("mouseover",function (e) {
                var t=e.target;
                var child=imgList.children();
                for (var i=0;i<5;i++){
                    if (t.parentNode==child[i]){
                        $(".view img").attr("src","http://"+imgurl+"/"+imgArr[i]);
                        return;
                    }
                }
            });
        }
    });

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');

    $("#ordernum").bind('input propertychange',function(){
        if ($("#ordernum").val()>parseInt($(".stocknum").text())){
            /*alert("库存不足！");*/
            successModal.modal('show');
            successModal.css("padding-right","0");
            $("#ordernum").val($(".stocknum").text());
        }else if($("#ordernum").val()<1){
            /*alert("最少购买一件！");*/
            unknownModal.modal('show');
            unknownModal.css("padding-right","0");
            $("#ordernum").val(1);
        }
    });

    $(".less").click(function () {
        var i=parseInt($("#ordernum").val());
        if (i>1){
            $("#ordernum").val(--i);
        }else {
            /*alert("最少购买一件！");*/
            unknownModal.modal('show');
            unknownModal.css("padding-right","0");
            $("#ordernum").val(1);
        }
    });

    $(".more").click(function () {
        var i=parseInt($("#ordernum").val());
        if (i<$(".stocknum").text()){
            $("#ordernum").val(++i);
        }else {
            /*alert("库存不足！");*/
            successModal.modal('show');
            successModal.css("padding-right","0");
            $("#ordernum").val($(".stocknum").text());
        }
    });

    //加入购物车 POST

/*    $("").on("click",function (event) {
        event.preventDefault();//使a自带的方法失效
        $.ajax({
            type: "POST",
            url: "http://192.168.1.228:8080/cart/add.json",
            contentType:"application/json",
            data: JSON.stringify({count:$("#ordernum").val(),productId:id}),//参数列表
            dataType:"json",
            success: function(result){
                alert("ok");
            }
        });
    });*/
    $.ajax({
        url: "http://"+urla+"/users/info.json",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function(result){
            console.log(result);
            if (result.status==10){
                $("#addColleBtn").remove();
                $("#celColleBtn").remove();
                $("#buy").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
                $("#addIntoCart").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
            }else {
                $("#buy").on('click',function(){
                    window.location.href = "order_check.html?productId="+id+"&count="+$("#ordernum").val();
                }).removeAttr("disabled");
                $("#addIntoCart").on('click',function(){addIntoCart()}).removeAttr("disabled");
            }
        }
    });

    function addIntoCart() {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/cart/add.json",
            data:"count="+$("#ordernum").val()+"&productId="+id,
            error: function(request) {
                alert("Connection error");
            },
            success: function(data) {
                var x = 100;
                var y = 500;
                var num = Math.floor(Math.random() * 8 + 1);
                var index=$('.demo').children('img').length;
                var rand = parseInt(Math.random() * (x - y + 1) + y);

                $(".demo").append("<img class='demoimg' src=''>");
                $('.demoimg:eq(' + index + ')').attr('src','image/itemIcon/'+num+'.png');
                $(".demoimg").animate({
                    bottom:"800px",
                    opacity:"0",
                    left: rand
                },3000);
                console.log(data);
            }
        });
    }

/*    $("#addIntoCart").click(function () {

    });*/

    getColleStatus();

    $("#addColleBtn").click(function () {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/collection/add.json",
            data:"productId="+id,
            error: function(request) {
                alert("Connection error");
                console.log(request);
            },
            success: function(data) {
                console.log(data);
                getColleStatus();
            }
        });
    });

    $("#celColleBtn").click(function () {
        $.ajax({
            /*url: "./json/users/info.json",*/
            url: "http://" + urla + "/collection/cancel.json?productId="+id,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                console.log(id + "cancelok");
                getColleStatus();
            },
            error: function (result) {
                console.log(result);
                console.log(result + "error");
            }
        })
    });

    function getColleStatus() {
        $.ajax({
            /*url: "./json/users/info.json",*/
            url: "http://" + urla + "/collection/collection_status.json?productId="+id,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                if (result.data==true){
                    console.log("已收藏");
                    $("#addColleBtn").css("display","none");
                    $("#celColleBtn").css("display","block");
                }else {
                    console.log("未收藏");
                    $("#addColleBtn").css("display","block");
                    $("#celColleBtn").css("display","none");
                }
            },
            error: function (result) {
                console.log(result);
            }
        })
    }
});