$(document).ready(function () {
    //获取url
    var loc = location.href;
    var n1 = loc.length;//地址的总长度
    var n2 = loc.indexOf("=");//取得=号的位置
    var id = decodeURI(loc.substr(n2 + 1, n1 - n2));//从=号后面的内容


    $.ajax({
        url: "http://" + urla + "/product/list.json?keyword=" + id,
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            console.log("ok");
            console.log(result);
            $.each(result, function (i, item) {
                $.each(item.list, function (x, obj) {
                    var $li=$("<li class='bi-item'></li>");
                    var $div=$("<div class='m-product'></div>");
                    var $hd=$("<div class='hd'>" +
                        "<a href='item.html?id="+obj.id+"'>" +
                        "<img src='http://"+imgurl+"/"+obj.mainImage+"'></a></a></div>");
                    var $bd=$("<div class='bd'>" +
                        "<h4 class='name'>" +
                        "<a href='item.html?id="+obj.id+"'>"+obj.name+"</a></h4>" +
                        "<p class='price'>" +
                        "<span class='retailPrice'>" +
                        "<span>￥</span>" +
                        "<span>"+obj.price+"</span></span></p></div>");
                    $div.append($hd,$bd);
                    $li.append($div);
                    $(".bi-list").append($li);
                });
            });
        },
        error: function (result) {
            console.log("error");
            console.log(result);
        }
    });

    $("#ascBtn").click(function () {
        ascSort();
    });
    $("#descBtn").click(function () {
        descSort();
    });

    function ascSort() {
        $.ajax({
            url: "http://" + urla + "/product/list.json?keyword=" + id+"&orderBy=price_asc",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log("ok");
                console.log(result);
                $(".bi-list").empty();
                $.each(result, function (i, item) {
                    $.each(item.list, function (x, obj) {
                        var $li=$("<li class='bi-item'></li>");
                        var $div=$("<div class='m-product'></div>");
                        var $hd=$("<div class='hd'>" +
                            "<a href='item.html?id="+obj.id+"'>" +
                            "<img src='http://"+imgurl+"/"+obj.mainImage+"'></a></a></div>");
                        var $bd=$("<div class='bd'>" +
                            "<h4 class='name'>" +
                            "<a href='item.html?id="+obj.id+"'>"+obj.name+"</a></h4>" +
                            "<p class='price'>" +
                            "<span class='retailPrice'>" +
                            "<span>￥</span>" +
                            "<span>"+obj.price+"</span></span></p></div>");
                        $div.append($hd,$bd);
                        $li.append($div);
                        $(".bi-list").append($li);
                    });
                });
            },
            error: function (result) {
                console.log("error");
                console.log(result);
            }
        });
    }

    function descSort() {
        $.ajax({
            url: "http://" + urla + "/product/list.json?keyword=" + id+"&orderBy=price_desc",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log("ok");
                console.log(result);
                $(".bi-list").empty();
                $.each(result, function (i, item) {
                    $.each(item.list, function (x, obj) {
                        var $li=$("<li class='bi-item'></li>");
                        var $div=$("<div class='m-product'></div>");
                        var $hd=$("<div class='hd'>" +
                            "<a href='item.html?id="+obj.id+"'>" +
                            "<img src='http://"+imgurl+"/"+obj.mainImage+"'></a></a></div>");
                        var $bd=$("<div class='bd'>" +
                            "<h4 class='name'>" +
                            "<a href='item.html?id="+obj.id+"'>"+obj.name+"</a></h4>" +
                            "<p class='price'>" +
                            "<span class='retailPrice'>" +
                            "<span>￥</span>" +
                            "<span>"+obj.price+"</span></span></p></div>");
                        $div.append($hd,$bd);
                        $li.append($div);
                        $(".bi-list").append($li);
                    });
                });
            },
            error: function (result) {
                console.log("error");
                console.log(result);
            }
        });
    }

});