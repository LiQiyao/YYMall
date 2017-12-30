$(document).ready(function () {
    $('.carousel').carousel({
        interval: 4000
    });

    $(".navbar-brand").attr("href","main.html");
    $(".logo").attr("href","main.html");

    $(".navbar-brand").text("曜岩商城");

    var top = $(".bannerTab").offset().top;
    $(window).scroll(function () {
        if ($(window).scrollTop() >= top+200) {
            $(".bannerTab").css({
                "position":"fixed",
                "top":"0",
                "height":"60px"
            });
            $(".logo").css("display","none");
            $(".menuRow").css("top","20px");
            $(".searchWarp").css("top","20px");
            $(".cartWarp").css("top","20px");
        } else {
            $(".bannerTab").removeAttr("style");
            $(".logo").css("display","block");
            $(".menuRow").css("top","153px");
            $(".searchWarp").css("top","75px");
            $(".cartWarp").css("top","75px");
        }
    }).trigger("scroll");

    $("#backTop").click(function () {
        $('body,html').animate({'scrollTop':0},200);
    });

    $.ajax({
        url: "http://"+urla+"/users/info.json",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function(result){
            var $userNav=$("#userNav");
            if (result.status==0){
                $("#loginBtn").remove();
                $("#registerBtn").remove();
                $("#toOrderBtn").remove();
                /*$("#toOrderBtn").attr('href','user_order.html');*/
                $(".cartWarp a").attr('href','cart.html');
                var $user=$("<li><a href='user_center.html' id='toUserBtn'>" +
                    "<span class='glyphicon glyphicon-user'>&nbsp;</span>"+result.data.username+"</a></li>");
                var $myorder=$("<li><a href='user_order.html' id='toOrderBtn'>我的订单</a></li>");
                var $logout=$("<li><a href='javascript:javascript:void(0)' id='logout'>退出</a></li>");
                $userNav.append($user,$myorder,$logout);
            }else {
                var $login=$("<li><a href='login.html' id='loginBtn'>登录</a></li>");
                var $register=$("<li><a href='register.html' id='registerBtn'>注册</a></li>");
                $userNav.append($login,$register);
            }
            $("#logout").click(function () {
                logout();
            })
        }
    });

    function logout() {
        $.ajax({
            url: "http://"+urla+"/users/logout.json",
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                console.log("out!");
                console.log(result);
                alert("退出成功，返回首页");
                window.location.replace("main.html");
            },
            error: function(result){
                console.log("error!"+result);
            }
        });
    }

    $.ajax({
        url: "http://"+urla+"/categories/parent/0/list.json",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function(result){
            $.each(result,function(i,item){
                $.each(item,function(x,obj){
                    //导航条
                    var $a=$("<a href='' id='a"+obj.id+"' class='dropdown-toggle' data-toggle='dropdown'>"+obj.name+"</a>" );
                    var $li=$("<li class='li-item text-center dropdown'></li>");
                    var $ul=$("<ul class='dropdown-menu' id='p"+obj.id+"'></ul>");
                    createChildCategory(obj.id);
                    $li.append($a,$ul);
                    $(".menuRow").append($li);
                    //商品横幅
                    var $small=$("<small>&nbsp;&nbsp;</small>");
                    var $hright=$("<div class='h-right'><a href='list.html?parentId="+obj.id+"'>查看更多 &rsaquo;</a></div>");
                    var $hltitle=$("<a class='hl-title' href='list.html?parentId="+obj.id+"'>"+obj.name+"</a>");
                    var $hleft=$("<div class='h-left' id='i"+obj.id+"'></div>");
                    var $biheader=$("<div class='bi-header'></div>");
                    var $bilist=$("<ul class='bi-list' id='b"+obj.id+"'></ul>");
                    var $blockItem=$("<div class='blockItem'></div>");
                    createChildCrumb(obj.id);
                    createListItem(obj.id);
                    $hleft.append($hltitle,$small);
                    $biheader.append($hleft,$hright);
                    $blockItem.append($biheader,$bilist);
                    $("#blockGroup").append($blockItem);
                    $("#a"+obj.id).click(function () {
                        window.location.href = "list.html?parentId="+obj.id;
                    });
                });
            });
        }
    });

    //导航条二级分类
    function createChildCategory(id){
        $.ajax({
            //url: "./json/categories/parent/"+id+"/list.json",
            url: "http://"+urla+"/categories/parent/"+id+"/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                $.each(result,function(i,item){
                    $.each(item,function(x,obj){
                        var $cli=$("<li><a href='list.html?parentId="+obj.parentId+"&categoryId="+obj.id+"'>"+obj.name+"</a></li>");
                        $("#p"+id).append($cli);
                    });
                });
            }
        });
    }
    //商品横幅二级分类
    function createChildCrumb(id) {
        $.ajax({
            //url: "./json/categories/parent/"+id+"/list.json",
            url: "http://"+urla+"/categories/parent/"+id+"/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                $.each(result,function(i,item){
                    $.each(item,function(x,obj){
                        var $ca=$("<a href='list.html?parentId="+obj.parentId+"&categoryId="+obj.id+"'>"+obj.name+"</a><span>&nbsp;/&nbsp;</span>");
                        $("#i"+id).append($ca);
                    });
                });
            }
        });
    }

    function createListItem(id) {
        $.ajax({
            //url: "./json/product/list.json!categoryId="+id+".json",
            url: "http://"+urla+"/product/list.json?categoryId="+id+"&pageSize=4",
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                $.each(result,function(i,item){
                    $.each(item.list,function(x,obj){
                        var $img=$("<img src='http://"+imgurl+"/"+obj.mainImage+"'>");
                        var $ha=$("<a href='item.html?id="+obj.id+"'>"+obj.name+"</a>");
                        var $span1=$("<span>￥</span>");
                        var $span2=$("<span>"+obj.price+"</span>");
                        var $retailPrice=$("<span class='retailPrice'></span>");
                        var $a=$("<a href='item.html?id="+obj.id+"'></a>");
                        var $h4=$("<h4 class='name'></h4>");
                        var $price=$("<p class='price'></p>");
                        var $hd=$("<div class='hd'></div>");
                        var $bd=$("<div class='bd'></div>");
                        var $mproduct=$("<div class='m-product'></div>");
                        var $biitem=$("<li class='bi-item'></li>");
                        $retailPrice.append($span1,$span2);
                        $a.append($img);
                        $h4.append($ha);
                        $price.append($retailPrice);
                        $hd.append($a);
                        $bd.append($h4,$price);
                        $mproduct.append($hd,$bd);
                        $biitem.append($mproduct);
                        $("#b"+id).append($biitem);
                    });
                });
            }
        });
    }

    //搜索
    $("#searchBtn").on("click",function () {
        $.ajax({
            url: "http://"+urla+"/product/list.json?keyword="+$("#searchInput").val(),
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                console.log(result);
                window.location.replace("search.html?keyword="+$("#searchInput").val());
            },
            error:function (result) {
                console.log(result);
            }
        });
    });
});

