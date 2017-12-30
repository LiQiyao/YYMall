$(document).ready(function () {
    var pageNum=1;
    var total=0;

    $.ajax({
        /*url: "./json/order/list.json!pageNum=1.json",*/
        url: "http://" + urla + "/order/list.json?pageSize=500",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            console.log(result);
            total=result.data.total;
        }
    });
    getPageNum();
    function getPageNum() {
        if (total<=5){
            $("#Previous").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
            $("#Next").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
        }else if (pageNum==1){
            $("#Previous").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
            $("#Next").removeAttr("disabled");
        }else if (pageNum==Math.ceil(total/5)){
            $("#Next").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
            $("#Previous").removeAttr("disabled");
        }else {
            $("#Previous").removeAttr("disabled");
            $("#Next").removeAttr("disabled");
        }
    }

    $("#Previous").click(function () {
        console.log(pageNum);
        pageNum--;

        $(".tb-item").remove();
        $.ajax({
            /*url: "./json/order/list.json!pageNum=1.json",*/
            url: "http://" + urla + "/order/list.json?pageSize=5&pageNum="+pageNum,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
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
                            "<td colspan='1'><span>&nbsp&nbsp" + obj.createTime + "&nbsp&nbsp</span></td>" +
                            "<td colspan='3'><span>订单编号：" + obj.orderNo + "</span></td>" +
                            "<td colspan='1' style='text-align: right;font-size: 12px'>" +
                            "<a href='order_pay.html?orderNo=" + obj.orderNo + "'>查看详情>></a></td></tr></thead>");
                        var $tbody = $("<tbody id='tb" + obj.orderNo + "'></tbody>");
                        var orderNo = obj.orderNo;
                        $table.append($colgroup, $thead, $tbody);
                        $(".panel-body").append($table);
                        var num = obj.orderItemDTOList.length;
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
                            var $td5 = $("<td class='td-status' rowspan='" + num + "'>" +
                                "<div>" + obj.statusDesc + "</div></td>");
                            $("#tb" + orderNo).append($tr);
                            $tr.append($td1, $td2, $td3, $td4, $td5);
                        });
                        if ($("#tb" + orderNo).children("tr").length > 1) {
                            $("#tb" + orderNo + " tr").not(":first").find($(".td-total")).remove();
                            $("#tb" + orderNo + " tr").not(":first").find($(".td-status")).remove();
                        }
                    })
                });
            }
        });
        getPageNum();
    });




    $("#Next").click(function () {
        console.log(pageNum);
        pageNum++;
        $(".tb-item").remove();
        $.ajax({
            /*url: "./json/order/list.json!pageNum=1.json",*/
            url: "http://" + urla + "/order/list.json?pageSize=5&pageNum="+pageNum,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
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
                            "<td colspan='1'><span>&nbsp&nbsp" + obj.createTime + "&nbsp&nbsp</span></td>" +
                            "<td colspan='3'><span>订单编号：" + obj.orderNo + "</span></td>" +
                            "<td colspan='1' style='text-align: right;font-size: 12px'>" +
                            "<a href='order_pay.html?orderNo=" + obj.orderNo + "'>查看详情>></a></td></tr></thead>");
                        var $tbody = $("<tbody id='tb" + obj.orderNo + "'></tbody>");
                        var orderNo = obj.orderNo;
                        $table.append($colgroup, $thead, $tbody);
                        $(".panel-body").append($table);
                        var num = obj.orderItemDTOList.length;
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
                            var $td4 = $("<td class='td-total' rowspan='" + obj.orderItemDTOList.length + "'>" +
                                "<div>" +
                                "<span>¥</span>" +
                                "<span>" + obj.payment + "</span>" +
                                "<p>(优惠：¥0.00)</p></div></td>");
                            var $td5 = $("<td class='td-status' rowspan='" + obj.orderItemDTOList.length + "'>" +
                                "<div>" + obj.statusDesc + "</div></td>");
                            $("#tb" + orderNo).append($tr);
                            $tr.append($td1, $td2, $td3, $td4, $td5);
                        });
                        if ($("#tb" + orderNo).children("tr").length > 1) {
                            $("#tb" + orderNo + " tr").not(":first").find($(".td-total")).remove();
                            $("#tb" + orderNo + " tr").not(":first").find($(".td-status")).remove();
                        }
                    })
                });
            }
        });
        getPageNum();
    });


    showOrderList();

    function showOrderList() {
        $.ajax({
            /*url: "./json/order/list.json!pageNum=1.json",*/
            url: "http://" + urla + "/order/list.json?pageSize=5",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
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
                            "<td colspan='1'><span>&nbsp&nbsp" + obj.createTime + "&nbsp&nbsp</span></td>" +
                            "<td colspan='3'><span>订单编号：" + obj.orderNo + "</span></td>" +
                            "<td colspan='1' style='text-align: right;font-size: 12px'>" +
                            "<a href='order_pay.html?orderNo=" + obj.orderNo + "'>查看详情>></a></td></tr></thead>");
                        var $tbody = $("<tbody id='tb" + obj.orderNo + "'></tbody>");
                        var orderNo = obj.orderNo;
                        $table.append($colgroup, $thead, $tbody);
                        $(".panel-body").append($table);
                        var num = obj.orderItemDTOList.length;
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
                            var $td4 = $("<td class='td-total' rowspan='" + obj.orderItemDTOList.length + "'>" +
                                "<div>" +
                                "<span>¥</span>" +
                                "<span>" + obj.payment + "</span>" +
                                "<p>(优惠：¥0.00)</p></div></td>");
                            var $td5 = $("<td class='td-status' rowspan='" + obj.orderItemDTOList.length + "'>" +
                                "<div>" + obj.statusDesc + "</div></td>");
                            $("#tb" + orderNo).append($tr);
                            $tr.append($td1, $td2, $td3, $td4, $td5);
                        });
                        if ($("#tb" + orderNo).children("tr").length > 1) {
                            $("#tb" + orderNo + " tr").not(":first").find($(".td-total")).remove();
                            $("#tb" + orderNo + " tr").not(":first").find($(".td-status")).remove();
                        }
                    })
                });
            }
        });
    }


    /* var createProductList = function (id, num ,statusDesc) {
     $.ajax({
     url: "./json/order/list.json!pageNum=1.json",
     type: "GET",
     dataType: 'JSON',
     success: function (result) {
     $.each(result, function (i, item) {
     $.each(item.list, function (j, obj) {
     $.each(obj.orderItemVoList, function (x, t) {
     var $tr = $("<tr></tr>");
     var $td1 = $("<td>" +
     "<div class='product-info'>" +
     "<img src='image/item/01.jpg'>" +
     "<p>" + t.productName + "</p></div></td>");
     var $td2 = $("<td class='td-price'>" +
     "<div>" +
     "<span>￥</span>" +
     "<span>" + t.currentUnitPrice + "</span></div></td>");
     var $td3 = $("<td class='td-num'>" +
     "<div>" + t.quantity + "件</div></td>");
     var $td4 = $("<td class='td-total' rowspan='" + num + "'>" +
     "<div>" +
     "<span>¥</span>" +
     "<span>" + t.totalPrice + "</span>" +
     "<p>(优惠：¥0.09)</p></div></td>");
     var $td5=$("<td class='td-status' rowspan='" + num + "'>" +
     "<div>"+statusDesc+"</div></td>");
     $tr.append($td1,$td2,$td3,$td4,$td5);
     $("#tb"+id).append($tr);
     });
     });
     });
     }
     });
     }*/
});