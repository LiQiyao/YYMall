$(document).ready(function () {
    $(function () {
        $('#collapsed1').collapse('show')
    });

    var widowheight = $(window).height() - 50;
    $("body").css("height", widowheight);

    $.ajax({
        url: "http://" + urla + "/manage/category/parallel_children.json",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function (result) {
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
                /*console.log($(this).val());*/

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
                /*console.log($('#selChlidId').val());*/
            },
            error: function (data) {
                console.log("X");
                console.log(data);
            }
        });
    }

    $(".img-thumbnail").css("display", "none");
    var imgnum = 1;
    var uriStr = '';
    //上传图片
    $("#uploadImgBtn").click(function () {
        var formData = new FormData();
        formData.append('upload_file', $('#file')[0].files[0]);
        $.ajax({
            url: "http://" + urla + "/manage/product/upload.json",
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log(data);
                $("#img" + imgnum).css("display", "block");
                $("#img" + imgnum).attr("src", data.data.url);
                uriStr += data.data.uri + ',';
                imgnum++;
                if (imgnum>5){
                    uriStr=uriStr.substring(0,uriStr.length-1);
                    $("#uploadImgBtn").attr("disabled", "disabled");
                    console.log(uriStr);
                }
            },
            error: function (data) {
                console.log("X");
                console.log(data);
            }
        });
    });

    var successModal=$('#successModal');
    var unknownModal=$('#unknownModal');

    $("#upload").on("click", function () {
        $.ajax({
            type: "POST",
            cache: true,
            url: "http://" + urla + "/manage/product/save_or_update.json",
            data: "name=" + $("#inputName").val()
            + "&subtitle=" + $("#inputSubtitle").val()
            + "&price=" + $("#inputPrice").val()
            + "&stock=" + $("#inputStock").val()
            + "&categoryId=" + $("#selChlidId").val()
            + "&detail=" + $("#inputDetail").val()
            + "&subImages="+uriStr
            ,
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
                successModal.modal('show');
                successModal.css("padding-right","0");
                successModal.on('hide.bs.modal', function () {
                    location.reload();
                })
            }
        });
    });



});