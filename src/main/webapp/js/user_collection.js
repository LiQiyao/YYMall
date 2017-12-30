$(document).ready(function () {
    $.ajax({
        /*url: "./json/users/info.json",*/
        url: "http://" + urla + "/collection/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            console.log(result);
            $.each(result.data, function (i, item) {
                var $li = $("<li class='bi-item' id='li" + item.id + "'></li>");
                var $div = $("<div class='m-product'></div>");
                var $hd = $("<div class='hd' id='hd" + item.id + "'>" +
                    "<a href='item.html?id=" + item.id + "'>" +
                    "<img src='http://"+imgurl+"/"+item.mainImage+"'></a>" +
                    "<a href='javascript:void(0)' class='cancel' id='cancelBtn" + item.id + "'>取消收藏</a></div>");
                var $bd = $("<div class='bd'></div>");
                var $h4 = $("<h4 class='name'>" +
                    "<a href='item.html?id=" + item.id + "'>" + item.name + "</a></h4>");
                var $p = $("<p class='price'>" +
                    "<span class='retailPrice'>" +
                    "<span>￥</span>" +
                    "<span>" + item.price + "</span></span></p>");
                $bd.append($h4, $p);
                $div.append($hd, $bd);
                $li.append($div);
                $(".bi-list").append($li);

                $("#hd" + item.id).mousemove(function () {
                    $("#cancelBtn" + item.id).css("display", "block");
                });
                $("#hd" + item.id).mouseout(function () {
                    $("#cancelBtn" + item.id).css("display", "none");
                });

                $("#cancelBtn" + item.id).click(function () {
                    console.log(item.id);
                    cancelColle(item.id);
                   /* $("#li"+item.id).remove();*/
                });
            });
        },
        error: function (data) {
            console.log(data);
        }
    });

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');

    function cancelColle(productId) {
        $.ajax({
            /*url: "./json/users/info.json",*/
            url: "http://" + urla + "/collection/cancel.json?productId="+productId,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                console.log(productId + "cancelok");
                successModal.modal('show');
                successModal.css("padding-right","0");
                successModal.on('hide.bs.modal', function () {
                    $("#li"+productId).remove();
                })
            },
            error: function (result) {
                console.log(result);
                console.log(result + "error");
                unknownModal.modal('show');
                unknownModal.css("padding-right","0");
                $("#unknownModal .alert").text(data.msg+"请重试");
            }
        })
    }

});