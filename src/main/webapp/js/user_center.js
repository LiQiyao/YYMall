$(document).ready(function () {
    $.ajax({
        /*url: "./json/users/info.json",*/
        url: "http://"+urla+"/users/info.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            $("#username").text(result.data.username);
        }
    });

    $.ajax({
        /*url: "./json/order/list.json!pageNum=1.json",*/
        url: "http://"+urla+"/order/list.json?pageNum=1&pageSize=5",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            $.each(result, function (i, item) {
                $.each(item.list, function (j, obj) {
                    var $table = $("<table class='table tb-item'></table>");
                    var $colgroup = $("<colgroup>" +
                        "<col class='col-lg-4'>" +
                        "<col class='col-lg-2'>" +
                        "<col class='col-lg-2'>" +
                        "<col class='col-lg-2'>" +
                        "<col class='col-lg-2'></colgroup>");
                    var $thead = $("<thead><tr>" +
                        "<td colspan='8'>" +
                        "<span>&nbsp&nbsp" + obj.createTime + "&nbsp&nbsp</span>" +
                        "<span>订单编号：" + obj.orderNo + "</span></td></tr></thead>");
                    var $tbody = $("<tbody id='tb" + obj.orderNo + "'></tbody>");
                    var orderNo=obj.orderNo;
                    $table.append($colgroup, $thead, $tbody);
                    $(".panel-body").append($table);
                    var num=obj.orderItemDTOList.length;
                    $.each(obj.orderItemDTOList, function (x, t) {
                        var $tr = $("<tr></tr>");
                        var $td1 = $("<td>" +
                            "<div class='product-info'>" +
                            "<img src='http://"+imgurl+"/"+t.productImage+"'>" +
                            "<a href='item.html?id="+t.productId+"'>" + t.productName + "</a></div></td>");
                        var $td2 = $("<td class='td-price'>" +
                            "<div>" +
                            "<span>￥</span>" +
                            "<span>" + t.currentUnitPrice + "</span></div></td>");
                        var $td3 = $("<td class='td-num'>" +
                            "<div>" + t.quantity + "件</div></td>");
                        var $td4 = $("<td class='td-total' rowspan='" + num + "'>" +
                            "<div>" +
                            "<span>¥</span>" +
                            "<span>" + obj.payment + "</span>" +
                            "<p>(优惠：¥0.00)</p></div></td>");
                        var $td5=$("<td class='td-status' rowspan='" + num + "'>" +
                            "<div>"+obj.statusDesc+"</div></td>");
                        $("#tb"+orderNo).append($tr);
                        $tr.append($td1,$td2,$td3,$td4,$td5);
                    });
                    if ($("#tb"+orderNo).children("tr").length>1){
                        $("#tb"+orderNo+" tr").not(":first").find($(".td-total")).remove();
                        $("#tb"+orderNo+" tr").not(":first").find($(".td-status")).remove();
                    }
                })
            });

        }
    });
});