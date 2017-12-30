$(document).ready(function () {
    $(function () {
        $('#collapsed1').collapse('show')
    });

    var widowheight = $(window).height() - 50;
    $("body").css("height", widowheight);

    var pageNum=1;
    var total=0;

    $.ajax({
        /*url: "./json/order/list.json!pageNum=1.json",*/
        url: "http://" + urla + "/manage/product/list.json?pageSize=500",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            total=result.data.total;
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
            url: "http://" + urla + "/manage/product/list.json?pageSize=8&pageNum="+pageNum,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                $("tbody").empty();
                $.each(result,function (i,item) {
                    $.each(item.list,function (x,obj) {
                        var $tr=$("<tr>" +
                            "<td>"+obj.id+"</td>" +
                            "<td>"+obj.categoryId+"</td>"+
                            "<td>"+obj.name+"</td>" +
                            "<td>"+obj.price+"</td>" +
                            "<td>"+obj.stock+"</td>" +
                            "<td ><a class='btn btn-sm btn-warning' data-toggle='modal' " +
                            "data-target='#change"+obj.id+"' id='changeBtn"+obj.id+"'>修改信息</a>&nbsp;"+
                            "<a class='btn btn-sm btn-danger' id='pullOffBtn"+obj.id+"'>下架</a>" +
                            "<a class='btn btn-sm btn-success' id='pullOnBtn"+obj.id+"'>上架</a></td></tr>");
                        $("tbody").append($tr);

                        if (obj.status==1){
                            $("#pullOnBtn"+obj.id).remove();
                        }else {
                            $("#pullOffBtn"+obj.id).remove();
                        }

                        $("#pullOnBtn"+obj.id).click(function () {
                            pullOnProduct(obj.id);
                        });
                        $("#pullOffBtn"+obj.id).click(function () {
                            pullOffProduct(obj.id);
                        });

                        $("#changeBtn"+obj.id).click(function () {
                            $(".modal").attr("id","change"+obj.id);
                            $(".modal-title").text("修改商品信息,编号"+obj.id);
                            changInfo(obj.id);
                        });

                    })
                })
            }
        });
        getPageNum();
    });

    $("#Next").click(function () {
        pageNum++;
        $("tbody").empty();
        $.ajax({
            /*url: "./json/order/list.json!pageNum=1.json",*/
            url: "http://" + urla + "/manage/product/list.json?pageSize=8&pageNum="+pageNum,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                $("tbody").empty();
                $.each(result,function (i,item) {
                    $.each(item.list,function (x,obj) {
                        var $tr=$("<tr>" +
                            "<td>"+obj.id+"</td>" +
                            "<td>"+obj.categoryId+"</td>"+
                            "<td>"+obj.name+"</td>" +
                            "<td>"+obj.price+"</td>" +
                            "<td>"+obj.stock+"</td>" +
                            "<td ><a class='btn btn-sm btn-warning' data-toggle='modal' " +
                            "data-target='#change"+obj.id+"' id='changeBtn"+obj.id+"'>修改信息</a>&nbsp;"+
                            "<a class='btn btn-sm btn-danger' id='pullOffBtn"+obj.id+"'>下架</a>" +
                            "<a class='btn btn-sm btn-success' id='pullOnBtn"+obj.id+"'>上架</a></td></tr>");
                        $("tbody").append($tr);

                        if (obj.status==1){
                            $("#pullOnBtn"+obj.id).remove();
                        }else {
                            $("#pullOffBtn"+obj.id).remove();
                        }

                        $("#pullOnBtn"+obj.id).click(function () {
                            pullOnProduct(obj.id);
                        });
                        $("#pullOffBtn"+obj.id).click(function () {
                            pullOffProduct(obj.id);
                        });

                        $("#changeBtn"+obj.id).click(function () {
                            $(".modal").attr("id","change"+obj.id);
                            $(".modal-title").text("修改商品信息,编号"+obj.id);
                            changInfo(obj.id);
                        });

                    })
                })
            }
        });
        getPageNum();
    });

    $("#searchBtn").click(function () {
        $.ajax({
            url:"http://"+urla+"/manage/product/search.json?productName="+$("#inputSearch").val(),
            type:"GET",
            dataType: 'JSON',
            success: function(result){
                console.log(result);
                $("tbody").empty();
                $.each(result,function (i,item) {
                    $.each(item.list,function (x,obj) {
                        var $tr=$("<tr>" +
                            "<td>"+obj.id+"</td>" +
                            "<td>"+obj.categoryId+"</td>"+
                            "<td>"+obj.name+"</td>" +
                            "<td>"+obj.price+"</td>" +
                            "<td>"+obj.stock+"</td>" +
                            "<td ><a class='btn btn-sm btn-warning' data-toggle='modal' " +
                            "data-target='#change"+obj.id+"' id='changeBtn"+obj.id+"'>修改信息</a>&nbsp;"+
                            "<a class='btn btn-sm btn-danger' id='pullOffBtn"+obj.id+"'>下架</a>" +
                            "<a class='btn btn-sm btn-success' id='pullOnBtn"+obj.id+"'>上架</a></td></tr>");
                        $("tbody").append($tr);

                        if (obj.status==1){
                            $("#pullOnBtn"+obj.id).remove();
                        }else {
                            $("#pullOffBtn"+obj.id).remove();
                        }

                        $("#pullOnBtn"+obj.id).click(function () {
                            pullOnProduct(obj.id);
                        });
                        $("#pullOffBtn"+obj.id).click(function () {
                            pullOffProduct(obj.id);
                        });

                        $("#changeBtn"+obj.id).click(function () {
                            $(".modal").attr("id","change"+obj.id);
                            $(".modal-title").text("修改商品信息,编号"+obj.id);
                            changInfo(obj.id);
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

    showProductList();

    function showProductList() {
        $.ajax({
            url: "http://"+urla+"/manage/product/list.json?pageSize=8",
            //url: "./json/categories/parent/0/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                console.log(result);
                $.each(result,function (i,item) {
                    $.each(item.list,function (x,obj) {
                        var $tr=$("<tr>" +
                            "<td>"+obj.id+"</td>" +
                            "<td>"+obj.categoryId+"</td>"+
                            "<td>"+obj.name+"</td>" +
                            "<td>"+obj.price+"</td>" +
                            "<td>"+obj.stock+"</td>" +
                            "<td ><a class='btn btn-sm btn-warning' data-toggle='modal' " +
                            "data-target='#change"+obj.id+"' id='changeBtn"+obj.id+"'>修改信息</a>&nbsp;"+
                            "<a class='btn btn-sm btn-danger' id='pullOffBtn"+obj.id+"'>下架</a>" +
                            "<a class='btn btn-sm btn-success' id='pullOnBtn"+obj.id+"'>上架</a></td></tr>");
                        $("tbody").append($tr);

                        if (obj.status==1){
                            $("#pullOnBtn"+obj.id).remove();
                        }else {
                            $("#pullOffBtn"+obj.id).remove();
                        }

                        $("#pullOnBtn"+obj.id).click(function () {
                            pullOnProduct(obj.id);
                        });
                        $("#pullOffBtn"+obj.id).click(function () {
                            pullOffProduct(obj.id);
                        });

                        $("#changeBtn"+obj.id).click(function () {
                            $(".modal").attr("id","change"+obj.id);
                            $(".modal-title").text("修改商品信息,编号"+obj.id);
                            changInfo(obj.id);
                        });

                    })
                })
            },
            error:function (data) {
                console.log("X");
                console.log(data);
            }
        });
    }

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');

    function changInfo(id) {
        $.ajax({
            url: "http://" + urla + "/manage/product/detail.json?productId="+id,
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                console.log(result);
                $("#inputName").val(result.data.name);
                $("#inputSubtitle").val(result.data.subtitle);
                $("#inputPrice").val(result.data.price);
                $("#inputStock").val(result.data.stock);
                $("#inputDetail").val(result.data.detail);

                $("#upload").on("click", function () {
                    $.ajax({
                        type: "POST",
                        cache: true,
                        url: "http://" + urla + "/manage/product/save_or_update.json",
                        data: "name=" + $("#inputName").val()
                        + "&id=" + id
                        + "&subtitle=" + $("#inputSubtitle").val()
                        + "&price=" + $("#inputPrice").val()
                        + "&stock=" + $("#inputStock").val()
                        + "&categoryId=" + $("#selChlidId").val()
                        + "&detail=" + $("#inputDetail").val()
                        ,
                        error: function (request) {
                            console.log();
                        },
                        success: function (data) {
                            console.log(data);
                            $("#myModal").modal('hide');
                            successModal.modal('show');
                            successModal.css("padding-right","0");
                            successModal.on('hide.bs.modal', function () {
                                location.reload();
                            })
                        }
                    });
                });
            },
            error: function (data) {
                console.log("X");
                console.log(data);
            }
        })
    }

    function pullOffProduct(id) {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/manage/product/save_or_update.json",
            data: "id=" + id
            + "&status=2",
            error: function (request) {
                console.log();
            },
            success: function (data) {
                console.log(data);
                successModal.modal('show');
                successModal.css("padding-right","0");
                successModal.on('hide.bs.modal', function () {
                    location.reload();
                })
            }
        });
    }

    function pullOnProduct(id) {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/manage/product/save_or_update.json",
            data: "id=" + id
            + "&status=1",
            error: function (request) {
                console.log();
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
                successModal.modal('show');
                successModal.css("padding-right","0");
                successModal.on('hide.bs.modal', function () {
                    location.reload();
                })
            }
        });
    }

    $.ajax({
        url: "http://" + urla + "/manage/category/parallel_children.json",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
            console.log(result);
            var test = result.data;
            addChildCategory(test[0].id);
            $.each(result, function (i, item) {
                $.each(item, function (x, obj) {
                    var $op = $("<option class='opParent' value='" + obj.id + "'>" + obj.name + "</option>");
                    $("#selParentId").append($op);
                })
            });
            $('#selParentId').change(function () {
                addChildCategory($(this).val());
                console.log($(this).val());

            });

            /*  $('#selChlidId').change(function () {
             console.log($(this).val());
             });
             */
        },
        error: function (data) {
            console.log("X");
            console.log(data);
        }
    });

    function addChildCategory(id) {
        $.ajax({
            url: "http://" + urla + "/manage/category/parallel_children.json?categoryId=" + id,
            //url: "./json/categories/parent/0/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function (result) {
                $("#selChlidId").empty();
                $.each(result, function (i, item) {
                    $.each(item, function (x, obj) {
                        var $op2 = $("<option class='opChild' value='" + obj.id + "'>" + obj.name + "</option>");
                        $("#selChlidId").append($op2);
                    })
                });
            },
            error: function (data) {
                console.log("X");
                console.log(data);
            }
        });
    }


});