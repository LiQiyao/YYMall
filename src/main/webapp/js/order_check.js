$(document).ready(function () {
    //获取url中的参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
    var productId = getUrlParam('productId');
    var count = getUrlParam('count');

    var urlf=document.referrer;  //之前的页面

    function createNewShiping() {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/shipping/add.json",
            data:$("#addShipping").serialize(),
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
            }
        });
    }
    $("#savaNewShipping").click(function () {
        createNewShiping();
        window.location.reload();
    });




    $.ajax({
        /*url: "./json/shipping/list.json",*/
        url: "http://"+urla+"/shipping/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function(result){
            console.log(result);
            var shippingId;
            /*var $formGroup=$("<form action='' method='POST' id='formGroup'></form>");*/
            $.each(result,function(i,item){
                $.each(item.list,function(x,obj){
                    var $input=$("<label for='check"+obj.id+"' class='radio_label'>" +
                        "<input type='radio' name='ads' class='radio' value='' id='check"+obj.id+"'></label>");
                    var $span=$("<span class='glyphicon glyphicon-map-marker'></span>");
                    var $div=$("<div class='address-info'></div>");
                    var $li=$("<li class='address-box'></li>");
                    var $span1=$("<span>收货人："+obj.receiverName+"</span>");
                    var $span2=$("<span class='pull-right'>"+obj.receiverPhone+"</span>");
                    var $p=$("<p class='address'>收货地址： "+obj.receiverProvince+" "
                        +obj.receiverCity+" "+obj.receiverAddress+"</p>");
                    $div.append($span1,$span2,$p);
                    $li.append($input,$span,$div);
                    /*$formGroup.append($li);*/
                    $(".address-list").append($li);

                    $("#check" + obj.id).click(function () {
                        var isChecked = $(this).prop("checked");
                        $(this).prop("checked", isChecked);
                        if ($(this).is(':checked')) {
                            console.log(obj.id + "ok");
                            shippingId=obj.id;
                        } else {
                            console.log(obj.id + "no");
                        }
                    });
                });
            });
            /*$(".address-box:nth-child(1) input").attr("checked","checked");*/
            $("input[type='radio'][name='ads']:eq(0)").click();
            $("input[type='radio'][name='ads']:eq(0)").click();
            $(".address-box:nth-child(1) label").attr("class","radio_label checked");
            //单项商品提交

            //购物车提交
            $("#orderSub").on("click",function (event) {
                /*$.ajax({
                    type: "POST",
                    cache: true,
                    url:"http://"+urla+"/order/create.json",
                    data:"shippingId="+shippingId,
                    error: function(request) {
                        console.log("error");
                        console.log(request);
                    },
                    success: function(data) {
                        console.log("success");
                        console.log(data);
                        window.location.replace("order_pay.html?orderNo="+data.data.orderNo);
                    }
                });*/
                if (urlf.indexOf('cart.html')>=0){
                    postCartItem(shippingId);
                }else{
                    postSingleItem(shippingId);
                }
            });
            //给所有的单选按钮点击添加处理
            $("input[type='radio']").click(function(){
                //找出和当前name一样的单选按钮对应的label，并去除选中的样式的class
                $("input[type='radio'][name='"+$(this).attr('name')+"']").parent().removeClass("checked");
                //给自己对应的label
                $(this).parent().addClass("checked");
            });
        },
        error:function (data) {
            console.log(data);
        }
    });

    function postCartItem(shippingId) {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/order/create.json",
            data:"shippingId="+shippingId,
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
                window.location.replace("order_pay.html?orderNo="+data.data.orderNo);
            }
        });
    }

    function postSingleItem(shippingId) {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/order/immediate_create.json",
            data:"shippingId="+shippingId+"&productId="+productId+"&count="+count,
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
                window.location.replace("order_pay.html?orderNo="+data.data.orderNo);
            }
        });
    }


    if (urlf.indexOf('cart.html')>=0){
        showCartItem();
    }else{
        showSingleItem();
    }

    function showSingleItem() {
        $.ajax({
            /*url: "./json/order/cart-checked-product.json",*/
            url: "http://"+urla+"/order/selected_product.json?productId="+productId+"&count="+count,
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                console.log(result);
                $.each(result,function(i,item){
                    $.each(item.orderItemDTOList,function(x,obj){
                        var $i1=$("<div class='i1'><div class='ck'><div class='w-chkbox'></div></div></div>");
                        var $i2=$("<div class='i2'></div>");
                        var $pic=$("<div class='pic'><img src='http://"+imgurl+"/"+obj.productImage+"'></div>");
                        var $nameCon=$("<div class='nameCon'><a href='item.html?id="+obj.productId+"'>"+obj.productName+"</a></div>");
                        var $i3=$("<div class='i3'><span>¥</span><span>"+obj.currentUnitPrice+"</span></div>");
                        var $i4=$("<div class='i4'><span>"+obj.quantity+"件</span></div>");
                        var $i5=$("<div class='i5'><p class='c-price'><span>¥</span><span>"+obj.totalPrice+"</span></p></div>");
                        var $cartitem=$("<div class='cart-item'></div>");
                        $i2.append($pic,$nameCon);
                        $cartitem.append($i1,$i2,$i3,$i4,$i5);
                        $(".cart-group").append($cartitem);
                    });
                });
                $("#priceNum").text(result.data.productTotalPrice);
            },
            error:function (data) {
                console.log(data);
            }
        });
    }
    
    function showCartItem() {
        $.ajax({
            /*url: "./json/order/cart-checked-product.json",*/
            url: "http://"+urla+"/order/cart-checked-product.json",
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                console.log(result);
                $.each(result,function(i,item){
                    $.each(item.orderItemDTOList,function(x,obj){
                        var $i1=$("<div class='i1'><div class='ck'><div class='w-chkbox'></div></div></div>");
                        var $i2=$("<div class='i2'></div>");
                        var $pic=$("<div class='pic'><img src='http://"+imgurl+"/"+obj.productImage+"'></div>");
                        var $nameCon=$("<div class='nameCon'><a href='#'>"+obj.productName+"</a></div>");
                        var $i3=$("<div class='i3'><span>¥</span><span>"+obj.currentUnitPrice+"</span></div>");
                        var $i4=$("<div class='i4'><span>"+obj.quantity+"件</span></div>");
                        var $i5=$("<div class='i5'><p class='c-price'><span>¥</span><span>"+obj.totalPrice+"</span></p></div>");
                        var $cartitem=$("<div class='cart-item'></div>");
                        $i2.append($pic,$nameCon);
                        $cartitem.append($i1,$i2,$i3,$i4,$i5);
                        $(".cart-group").append($cartitem);
                    });
                });
                $("#priceNum").text(result.data.productTotalPrice);
            },
            error:function (data) {
                console.log(data);
            }
        });
    }
});