$(document).ready(function () {
    //获取url
    var loc = location.href;
    var n1 = loc.length;//地址的总长度
    var n2 = loc.indexOf("=");//取得=号的位置
    var id = decodeURI(loc.substr(n2 + 1, n1 - n2));//从=号后面的内容


    var orderNo;
    $.ajax({
        /*url: "./json/order/create.json!shippingId=7.json",*/
        url: "http://" + urla + "/order/detail.json?orderNo=" + id,
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            console.log(result);
            orderNo = result.data.orderNo;
            $.each(result, function (i, item) {
                $.each(item.orderItemDTOList, function (x, obj) {
                    var $i1 = $("<div class='i1'><div class='ck'><div class='w-chkbox'></div></div></div>");
                    var $i2 = $("<div class='i2'></div>");
                    var $pic = $("<div class='pic'><img src='http://"+imgurl+"/"+obj.productImage+"'></div>");
                    var $nameCon = $("<div class='nameCon'><a href='item.html?id="+obj.productId+"'>" + obj.productName + "</a></div>");
                    var $i3 = $("<div class='i3'><span>¥</span><span>" + obj.currentUnitPrice + "</span></div>");
                    var $i4 = $("<div class='i4'><span>" + obj.quantity + "件</span></div>");
                    var $i5 = $("<div class='i5'><p class='c-price'><span>¥</span><span>" + obj.totalPrice + "</span></p></div>");
                    var $cartitem = $("<div class='cart-item'></div>");
                    $i2.append($pic, $nameCon);
                    $cartitem.append($i1, $i2, $i3, $i4, $i5);
                    $(".cart-group").append($cartitem);
                });
            });
            if (result.data.status==20){
                $("#info-bottom").remove();
            }
            $(".orderNo").text("订单编号: " + result.data.orderNo);
            $(".orderStatus").text("订单状态: " + result.data.statusDesc);
            $(".address-info span:nth-child(1)").text("收货人：" + result.data.shippingDTO.receiverName);
            $(".address-info span:nth-child(2)").text(result.data.shippingDTO.receiverMobile);
            $(".address").text("收货地址： " + result.data.shippingDTO.receiverProvince + " "
                + result.data.shippingDTO.receiverCity + " " + result.data.shippingDTO.receiverAddress);
            $("#priceNum").text(result.data.payment);
            if (result.data.status==20){
                $("#payed").text("已支付：￥"+result.data.payment);
            }

        }
    });

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');

    //ajax轮询
    function request() {
        $.ajax({
            type: "GET",
            url: "http://" + urla + "/order/pay_status.json?orderNo="+orderNo,
            dataType: 'JSON',
            success: function (data) {
                console.log("success");
                console.log(data.data);
                if (data.data === true) {
                    /*alert("付款成功，跳转至订单页！");*/
                    $("#myModal").modal('hide');
                    successModal.modal('show');
                    successModal.css("padding-right","0");
                    successModal.on('hide.bs.modal', function () {
                        window.location.replace("user_order.html");
                    })
                } else {
                    console.log(data);
                }
            },
            error: function (result) {
                console.log("error");
                console.log(result);
            }
        });
    }

    $("#paySub").click(function () {
        $.ajax({
            type: "GET",
            url: "http://" + urla + "/order/pay.json?orderNo=" + orderNo,
            dataType: 'JSON',
            success: function (data) {
                console.log("success");
                console.log(data);
                $("#payCanvas").attr("src", data.data.qrURL);
                window.setInterval(request, 2000);
            },
            error: function (result) {
                console.log("error");
                console.log(result);
            }
        });
    });



});