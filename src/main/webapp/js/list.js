$(document).ready(function () {
    //获取url
    /*var loc = location.href;
    var n1 = loc.length;//地址的总长度
    var n2 = loc.indexOf("=");//取得=号的位置
    var id = decodeURI(loc.substr(n2+1, n1-n2));//从=号后面的内容*/


    //获取url中的参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
    var parentId = getUrlParam('parentId');
    var categoryId = getUrlParam('categoryId');


    $.ajax({
        url: "http://"+urla+"/categories/parent/"+parentId+"/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            var height2=0;
            $.each(result, function (i, item) {
                $.each(item, function (x, obj) {
                    var $a=$("<a href='javascript:void(0)' id='"+obj.id+"'>"+obj.name+"</a>");
                    $(".categoryGroup").append($a);
                    var $level2Category=$("<div class='level2Category' id='l2id"+obj.id+"'></div>");
                    var $l2hd=$("<div class='l2hd'>" +
                        "<p><span class='glyphicon glyphicon-bookmark'>&nbsp;</span><span>"+obj.name+"</span></p></div>");
                    var $ul=$("<ul class='bi-list' id='bl"+obj.id+"'></ul>");
                    creatProductItem(obj.id);
                    $level2Category.append($l2hd);
                    $level2Category.append($ul);
                    $(".content").append($level2Category);

                    /*$("#"+obj.id).click(function () {
                        var height=$("#l2id"+obj.id).offset().top;
                        $('body,html').animate({'scrollTop':height-50},200);
                    });*/
                    $("#"+obj.id).click(function () {
                        moveScroll(obj.id);
                    });
                });
            });

            /*height2=$("#l2id"+categoryId).offset().top;
            if (categoryId!=null){
                console.log(height2);
                $('body,html').animate({'scrollTop':height2-50},200);
            }*/
            function moveScroll(id) {
                var height=$("#l2id"+id).offset().top;
                $('body,html').animate({'scrollTop':height-50},200);
                console.log(height);
            }

            if (categoryId!==null){
                moveScroll(categoryId);
            }
        }
    });

    function creatProductItem(id) {
        $.ajax({
            url: "http://"+urla+"/product/list.json?categoryId="+id,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
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
                        $("#bl"+id).append($li);
                    });
                });
            }
        });
    }
});

