$(document).ready(function () {
    $(function () {
        $('#collapsed3').collapse('show')
    });

    //页面调整
    var widowheight = $(window).height() - 50;
    $("body").css("height", widowheight);

    var pageNum=1;
    var total=0;

    $.ajax({
        /*url: "./json/order/list.json!pageNum=1.json",*/
        url: "http://" + urla + "/manage/order/list.json?pageSize=500",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            total=result.data.total;
            console.log(total);
        }
    });

    getPageNum();

    function getPageNum() {
        if (total<=8){
            $("#Previous").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
            $("#Next").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
        }else if (pageNum==1){
            $("#Previous").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
            $("#Next").removeAttr("disabled");
        }else if (pageNum==Math.ceil(total/8)){
            $("#Next").on('click',function(){event.preventDefault()}).attr("disabled","disabled");
            $("#Previous").removeAttr("disabled");
        }else {
            $("#Previous").removeAttr("disabled");
            $("#Next").removeAttr("disabled");
        }
    }

    $("#Previous").click(function () {
        pageNum--;
        $("tbody").empty();
        $.ajax({
            /*url: "./json/order/list.json!pageNum=1.json",*/
            url: "http://" + urla + "/manage/order/list.json?pageSize=8&pageNum="+pageNum,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                $("tbody").empty();
                $.each(result, function (i, item) {
                    $.each(item.list, function (j, obj) {
                        var $td = $("<tr></tr><td>" + obj.orderNo + "</td>" +
                            "<td>" + obj.receiverName + "</td>" +
                            "<td>" + obj.payment + "</td>" +
                            "<td>" + obj.createTime + "</td>" +
                            "<td>" + obj.statusDesc + "</td>" +
                            "<td><div class='btn btn-sm btn-info' id='postBtn" + obj.orderNo + "'>发货</div>" +
                            "&nbsp<div class='btn btn-sm btn-success' " +
                            "id='showMoreBtn" + obj.orderNo + "' data-toggle='modal' " +
                            "data-target='#show" + obj.orderNo + "'>查看详情</div></td></tr>");
                        $("tbody").append($td);

                        if (obj.status != 20) {
                            $("#postBtn" + obj.orderNo).remove();
                        }
                        $("#postBtn" + obj.orderNo).click(function () {
                            console.log(obj.orderNo);
                            sendGood(obj.orderNo);
                        });

                        $("#showMoreBtn" + obj.orderNo).click(function () {
                            $(".modal").attr("id", "show" + obj.orderNo);
                            showMore(obj.orderNo);
                        });

                    })
                });
            }
        });
        getPageNum();
    });

    $("#Next").click(function () {
        pageNum++;
        $("tbody").empty();
        $.ajax({
            /*url: "./json/order/list.json!pageNum=1.json",*/
            url: "http://" + urla + "/manage/order/list.json?pageSize=8&pageNum="+pageNum,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                $("tbody").empty();
                $.each(result, function (i, item) {
                    $.each(item.list, function (j, obj) {
                        var $td = $("<tr></tr><td>" + obj.orderNo + "</td>" +
                            "<td>" + obj.receiverName + "</td>" +
                            "<td>" + obj.payment + "</td>" +
                            "<td>" + obj.createTime + "</td>" +
                            "<td>" + obj.statusDesc + "</td>" +
                            "<td><div class='btn btn-sm btn-info' id='postBtn" + obj.orderNo + "'>发货</div>" +
                            "&nbsp<div class='btn btn-sm btn-success' " +
                            "id='showMoreBtn" + obj.orderNo + "' data-toggle='modal' " +
                            "data-target='#show" + obj.orderNo + "'>查看详情</div></td></tr>");
                        $("tbody").append($td);

                        if (obj.status != 20) {
                            $("#postBtn" + obj.orderNo).remove();
                        }
                        $("#postBtn" + obj.orderNo).click(function () {
                            console.log(obj.orderNo);
                            sendGood(obj.orderNo);
                        });

                        $("#showMoreBtn" + obj.orderNo).click(function () {
                            $(".modal").attr("id", "show" + obj.orderNo);
                            showMore(obj.orderNo);
                        });


                    })
                });
            }
        });
        getPageNum();
    });

    $.ajax({
        url: "http://" + urla + "/manage/order/list.json?pageSize=8",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            console.log(result);
            $("tbody").empty();
            $.each(result, function (i, item) {
                $.each(item.list, function (j, obj) {
                    var $td = $("<tr></tr><td>" + obj.orderNo + "</td>" +
                        "<td>" + obj.receiverName + "</td>" +
                        "<td>" + obj.shippingDTO.receiverProvince+ obj.shippingDTO.receiverCity
                        + obj.shippingDTO.receiverAddress+ "</td>" +
                        "<td>" + obj.payment + "</td>" +
                        "<td>" + obj.createTime + "</td>" +
                        "<td>" + obj.statusDesc + "</td>" +
                        "<td><div class='btn btn-sm btn-info' id='postBtn" + obj.orderNo + "'>发货</div>" +
                        "&nbsp<div class='btn btn-sm btn-success' " +
                        "id='showMoreBtn" + obj.orderNo + "' data-toggle='modal' " +
                        "data-target='#show" + obj.orderNo + "'>查看详情</div></td></tr>");
                    $("tbody").append($td);

                    if (obj.status != 20) {
                        $("#postBtn" + obj.orderNo).remove();
                    }
                    $("#postBtn" + obj.orderNo).click(function () {
                        console.log(obj.orderNo);
                        sendGood(obj.orderNo);
                    });

                    $("#showMoreBtn" + obj.orderNo).click(function () {
                        $(".modal").attr("id", "show" + obj.orderNo);
                        showMore(obj.orderNo);
                    });


                })
            });

        },
        error: function (data) {
            console.log("X");
            console.log(data);
        }
    });

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');

    function sendGood(id) {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/manage/order/send_goods.json",
            data: "orderNo=" + id,
            error: function (request) {
                console.log(request);
                unknownModal.modal('show');
                unknownModal.css("padding-right","0");
                $("#unknownModal .alert").text(request.msg+"请重试");
                unknownModal.on('hide.bs.modal', function () {
                    location.reload();
                })
            },
            success: function (data) {
                console.log(data);
                /*alert("订单编号" + id + "发货成功");*/
                successModal.modal('show');
                successModal.css("padding-right","0");
                successModal.on('hide.bs.modal', function () {
                    location.reload();
                })
            }
        });
    }

    function showMore(id) {
        $.ajax({
            url: "http://" + urla + "/manage/order/detail.json?orderNo=" + id,
            //url: "./json/categories/parent/0/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                $(".address-box").empty();
                $(".cart-group").empty();

                $(".modal-title").text("订单编号:" + result.data.orderNo + "  " + result.data.createTime
                    + "  " + result.data.statusDesc+"  总金额:￥"+result.data.payment);

                var $span1 = $("<span>收货人：" + result.data.shippingDTO.receiverName + "&nbsp;&nbsp;" +
                    "电话：" + result.data.shippingDTO.receiverPhone + "&nbsp;&nbsp;" +
                    "收货地址: " + result.data.shippingDTO.receiverProvince +
                    result.data.shippingDTO.receiverCity + result.data.shippingDTO.receiverAddress + "</span>");
                $(".address-box").append($span1);

                if (result.data.status == 20) {
                    $("#payed").text("已支付：￥" + result.data.payment);
                }

                $.each(result.data.orderItemDTOList, function (i, item) {
                    var $i1 = $("<div class='i1'><div class='ck'><div class='w-chkbox'></div></div></div>");
                    var $i2 = $("<div class='i2'></div>");
                    var $pic = $("<div class='pic'><img src='image/item/01.jpg'></div>");
                    var $nameCon = $("<div class='nameCon'><p>" + item.productName + "</p></div>");
                    var $i3 = $("<div class='i3'><span>¥</span><span>" + item.currentUnitPrice + "</span></div>");
                    var $i4 = $("<div class='i4'><span>" + item.quantity + "件</span></div>");
                    var $i5 = $("<div class='i5'><p class='c-price'><span>¥</span><span>" + item.totalPrice
                        + "</span></p></div>");
                    var $cartitem = $("<div class='cart-item'></div>");
                    $i2.append($pic, $nameCon);
                    $cartitem.append($i1, $i2, $i3, $i4, $i5);
                    $(".cart-group").append($cartitem);
                })

            },
            error: function (data) {
                console.log("X");
                console.log(data);
            }
        });
    }

    $("#searchBtn").click(function () {
        $.ajax({
            url:"http://"+urla+"/manage/order/search.json?orderNo="+$("#inputSearch").val(),
            type:"GET",
            dataType: 'JSON',
            success: function(result){
                console.log(result);
                $("tbody").empty();
                $.each(result,function (i,item) {
                    $.each(item.list,function (x,obj) {
                        var $td = $("<tr></tr><td>" + obj.orderNo + "</td>" +
                            "<td>" + obj.receiverName + "</td>" +
                            "<td>" + obj.payment + "</td>" +
                            "<td>" + obj.createTime + "</td>" +
                            "<td>" + obj.statusDesc + "</td>" +
                            "<td><div class='btn btn-sm btn-info' id='postBtn" + obj.orderNo + "'>发货</div>" +
                            "&nbsp<div class='btn btn-sm btn-success' " +
                            "id='showMoreBtn" + obj.orderNo + "' data-toggle='modal' " +
                            "data-target='#show" + obj.orderNo + "'>查看详情</div></td></tr>");
                        $("tbody").append($td);

                        if (obj.status != 20) {
                            $("#postBtn" + obj.orderNo).remove();
                        }
                        $("#postBtn" + obj.orderNo).click(function () {
                            console.log(obj.orderNo);
                            sendGood(obj.orderNo);
                        });

                        $("#showMoreBtn" + obj.orderNo).click(function () {
                            $(".modal").attr("id", "show" + obj.orderNo);
                            showMore(obj.orderNo);
                        });
                    })
                })
            },
            error:function (data) {
                console.log("X");
                console.log(data);
            }
        })
    });


});