$(document).ready(function () {

    getShippingList();

    $("#cancelNewShipping").click(function () {
       $("#createNewShipping").reset();
    });

    $("#savaNewShipping").click(function () {
        createNewShiping();
    });

    function createNewShiping() {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/shipping/add.json",
            data:$("#createNewShipping").serialize(),
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
                getShippingList();
            }
        });
    }
    
    function deleteShipping(shippingId) {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/shipping/delete.json",
            data:"shippingId="+shippingId,
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
                getShippingList();
            }
        });
    }

    function updateShipping(shippingId) {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/shipping/update.json",
            data:$("#updateShipping").serialize()+"&id="+shippingId,
            error: function(request) {
                console.log("error");
                console.log(request);
            },
            success: function(data) {
                console.log("success");
                console.log(data);
                getShippingList();
                $('#myModal'+shippingId).modal('hide');
            }
        });
    }

    var shippingId=0;

    function getShippingList() {
        $.ajax({
            /*url: "./json/shipping/list.json",*/
            url: "http://"+urla+"/shipping/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                $("tbody").empty();
                var jsonLen=0;
                $.each(result,function (i,item) {
                    $.each(item.list,function (j,obj) {
                        var $tr=$("<tr id='tr"+obj.id+"'>" +
                            "<td>"+obj.receiverName+"</td>" +
                            "<td>"+obj.receiverProvince+obj.receiverCity+obj.receiverAddress+"</td>"+
                            "<td>"+obj.receiverZip+"</td>"+
                            "<td>"+obj.receiverPhone+"</td>"+
                            "<td><button class='btn btn-sm btn-warning' " +
                            "data-toggle='modal' data-target='#myModal"+obj.id+"' id='update"+obj.id+"'>编辑</button>" +
                            "<button class='btn btn-sm btn-danger' id='del"+obj.id+"'>删除</button></td>"+
                            "</tr>");
                        $("tbody").append($tr);
                        $("#update"+obj.id).click(function () {
                            $(".modal").attr("id","myModal"+obj.id);
                            shippingId=obj.id;
                        });
                        $("#del"+obj.id).click(function () {
                            deleteShipping(obj.id);
                        });
                        jsonLen++;
                    });
                });
                $("#shippingNum").text(jsonLen);
                $("#savaNewShippingU").click(function () {
                    updateShipping(shippingId);
                });
            }
        });
    }
});