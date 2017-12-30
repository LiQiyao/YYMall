$(document).ready(function () {

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');
    $.ajax({
        /*url: "./json/cart/list.json",*/
        url: "http://" + urla + "/cart/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            $.each(result, function (i, item) {
                var productArr=new Array;
                $.each(item.cartProductDTOList, function (x, obj) {
                    console.log(obj);
                    var $checkbox = $("<input type='checkbox' name='option' id='check" + obj.productId + "'>");
                    var $wchkbox = $("<div class='w-chkbox'></div>");
                    var $ck = $("<div class='ck'></div>");
                    var $i1 = $("<div class='i1'></div>");
                    $wchkbox.append($checkbox);
                    $ck.append($wchkbox);
                    $i1.append($ck);
                    var $img = $("<img src='http://"+imgurl+"/"+obj.productMainImage+"'>");
                    var $pic = $("<div class='pic'></div>");
                    var $a = $("<a href='item.html?id="+obj.productId+"'>" + obj.productName + "</a>");
                    var $nameCon = $("<div class='nameCon'></div>");
                    var $i2 = $("<div class='i2'></div>");
                    $pic.append($img);
                    $nameCon.append($a);
                    $i2.append($pic, $nameCon);
                    var $i3p = $("<p><span>¥</span><span>" + obj.productPrice + "</span></p>");
                    var $i3 = $("<div class='i3'></div>");
                    $i3.append($i3p);
                    var $less = $("<span class='less' id='less" + obj.productId + "'><a>" +
                        "<span class='glyphicon glyphicon-minus small'></span></a></span>");
                    var $ordernum = $("<input id='ordernum" + obj.productId + "' type='txt' value='"+obj.quantity+"'>");
                    var $more = $("<span class='more' id='more" + obj.productId + "'>" +
                        "<a><span class='glyphicon glyphicon-plus small'></span></a></span>");
                    var $selnum = $("<div class='selnum'></div>");
                    var $stock = $("<span class='stock'>（库存<span class='stocknum' id='snum" + obj.productId + "'>"
                        + obj.productStock + "</span>件）</span>");
                    var $i4 = $("<div class='i4'></div>");
                    $selnum.append($less, $ordernum, $more);
                    $i4.append($selnum, $stock);
                    var $i5p = $("<p class='c-price'><span>¥</span><span class='cpricenum' id='cnum" + obj.productId + "'>" +
                        obj.productTotalPrice + "</span></p>");
                    var $i5 = $("<div class='i5'></div>");
                    $i5.append($i5p);
                    /*var $operate1 = $("<div class='operate'><a href='javascript:void(0)'>移入收藏夹</a></div>");*/
                    var $operate2 = $("<div class='operate'><a href='javascript:void(0)' id='del" + obj.productId + "'>删除</a></div>");
                    var $i6 = $("<div class='i6'></div>");
                    $i6.append($operate2);
                    var $cartitem = $("<div class='cart-item' id='ci" + obj.productId + "'></div>");
                    $cartitem.append($i1, $i2, $i3, $i4, $i5, $i6);
                    $(".cart-group").append($cartitem);

                    //更改数量
                    $("#ordernum" + obj.productId).bind('input propertychange', function () {
                        if ($(this).val() > parseInt($("#snum" + obj.productId).text())) {
                            /*alert("库存不足！");*/
                            successModal.modal('show');
                            successModal.css("padding-right","0");
                            $(this).val($("#snum" + obj.productId).text());
                            update(obj.productId, $("#ordernum" + obj.productId).val());
                            /*$("#cnum" + obj.productId).text(obj.productTotalPrice);*/
                        } else if ($("#ordernum" + obj.productId).val() < 1) {
                            /*alert("最少购买一件！");*/
                            unknownModal.modal('show');
                            unknownModal.css("padding-right","0");
                            $(this).val(1);
                            update(obj.productId, $("#ordernum" + obj.productId).val());
                            /*$("#cnum" + obj.productId).text(obj.productTotalPrice);*/
                        } else {
                            update(obj.productId, $("#ordernum" + obj.productId).val());
                            /*$("#cnum" + obj.productId).text(obj.productTotalPrice);*/
                        }
                    });

                    $("#less" + obj.productId).click(function () {
                        var ordernum=$("#ordernum" + obj.productId);
                        var i = parseInt(ordernum.val());
                        if (i > 1) {
                            ordernum.val(--i);
                            update(obj.productId, ordernum.val());
                            /*$("#cnum" + obj.productId).text(obj.productTotalPrice);*/
                        } else {
                            /*alert("最少购买一件！");*/
                            unknownModal.modal('show');
                            unknownModal.css("padding-right","0");
                            ordernum.val(1);
                            update(obj.productId, ordernum.val());
                            /*$("#cnum" + obj.productId).text(obj.productTotalPrice);*/
                        }
                    });

                    $("#more" + obj.productId).click(function () {
                        var ordernum=$("#ordernum" + obj.productId);
                        var i = parseInt(ordernum.val());
                        if (i < $("#snum" + obj.productId).text()) {
                            ordernum.val(++i);
                            update(obj.productId, ordernum.val());
                            /*$("#cnum" + obj.productId).text(obj.productTotalPrice);*/
                        } else {
                            /*alert("库存不足！");*/
                            successModal.modal('show');
                            successModal.css("padding-right","0");
                            ordernum.val($("#snum" + obj.productId).text());
                            update(obj.productId, ordernum.val());
                            /*$("#cnum" + obj.productId).text(obj.productTotalPrice);*/
                        }
                    });

                    $("#check" + obj.productId).click(function () {
                        var isChecked = $(this).prop("checked");
                        $(this).prop("checked", isChecked);
                        if ($(this).is(':checked')) {
                            console.log(obj.productId + "ok");
                            check(obj.productId);
                        } else {
                            console.log(obj.productId + "no");
                            un_check(obj.productId);
                        }
                    });

                    //删除单个商品
                    $("#del" + obj.productId).click(function () {
                        deleteProduct("productIds="+obj.productId);
                        $("#ci" + obj.productId).remove();
                    });

                    //删除全部商品
                    $("#delAll").click(function () {
                        productArr.push(obj.productId);
                        var productStr=productArr.join(',');
                        console.log(productStr);
                        deleteProduct("productIds="+productStr);
                        $(".cart-group").empty();
                    });

                });

                $("#allCheck").click(function () {
                    var isChecked = $(this).prop("checked");
                    $("input[name='option']").prop("checked", isChecked);
                });

                $("#submitBtn").click(function () {
                    window.location.replace("order_check.html");
                })
            });
        },
        error: function (request) {
        }
    });

    //改变总价
    function changeTotalPrice() {
        $.ajax({
            /*url: "./json/cart/list.json",*/
            url: "http://" + urla + "/cart/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                $.each(result, function (i, item) {
                    console.log(item.cartTotalPrice);
                    $(".realPrice").text(item.cartTotalPrice);
                });
                console.log(result);
            },
            error:function (data) {
                console.log(data);
            }
        })
    }

    //改变小计
    function changeProductTotalPrice() {
        $.ajax({
            /*url: "./json/cart/list.json",*/
            url: "http://" + urla + "/cart/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                $.each(result, function (i, item) {
                    $.each(item.cartProductDTOList, function (x, obj) {
                        $("#cnum" + obj.productId).text(obj.productTotalPrice);
                    });
                });
                console.log(result);
            },
            error:function (data) {
                console.log(data);
            }
        })
    }

    $("#allCheck").click(function () {
        if ($(this).is(':checked')) {
            console.log("ok");
            check_all();
        } else {
            console.log("no");
            un_check_all();
        }
    });


    function check_all() {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/cart/check_all.json",
            data: "",
            error: function (request) {
                alert("Connection error");
            },
            success: function (data) {
                changeTotalPrice();
                console.log(data);
            }
        });
    }

    function un_check_all() {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/cart/un_check_all.json",
            data: "",
            error: function (request) {
                alert("Connection error");
            },
            success: function (data) {
                changeTotalPrice();
                console.log(data);
            }
        });
    }

    function check(productId) {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/cart/check.json",
            data: "productId=" + productId,
            error: function (request) {
                alert("Connection error");
            },
            success: function (data) {
                changeTotalPrice();
                console.log(data);
            }
        });
    }

    function un_check(productId) {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/cart/un_check.json",
            data: "productId=" + productId,
            error: function (request) {
                alert("Connection error");
            },
            success: function (data) {
                changeTotalPrice();
                console.log(data);
            }
        });
    }

    function update(productId, count) {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/cart/update.json",
            data: "productId=" + productId + "&count=" + count,
            error: function (request) {
                alert("Connection error");
            },
            success: function (data) {
                /*changeTotalPrice();*/

                var check=$("#check" + productId);
                var isChecked = check.prop("checked");
                check.prop("checked", isChecked);
                if (check.is(':checked')) {
                    console.log(productId + "ok");
                    changeTotalPrice();
                    changeProductTotalPrice();
                } else {
                    console.log(productId + "no");
                    changeProductTotalPrice();
                }
            }
        });
    }

    //删除商品 批量 单个
    function deleteProduct(productIds) {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/cart/products/delete.json",
            data: productIds,
            error: function (request) {
                alert("Connection error");
                console.log(data);
            },
            success: function (data) {
                console.log(data);
                changeTotalPrice();
            }
        });
    }
});