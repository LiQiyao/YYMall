$(document).ready(function () {
    $(function () {
        $('#collapsed2').collapse('show')
    });

    //页面调整
     var widowheight = $(window).height() - 50;
     $("body").css("height", widowheight);

   /* var $category_item1=$("<div class='btn btn-info btn-lg category-item'>示例1</div>");
    var $category_item2=$("<div class='btn btn-info btn-lg category-item'>示例2</div>");

    var $btn_add=$("<button type='button' class='btn btn-success btn-lg' data-toggle='modal' data-target='#add_child'>" +
        "<span class='glyphicon glyphicon-plus'></span> 新增 </button>");
    var $panel_body=$("<div class='panel-body'></div>");
    var $panel_heading=$("<div class='panel-heading'> <span class='category-title'>浏览器</span>");
    var $panel=$("<div class='panel panel-default'>");
    $panel_body.append($category_item1,$category_item2,$btn_add);
    $panel.append($panel_heading,$panel_body);
    $(".category-box").append($panel);*/

    $("#addParentBtn").click(function () {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/manage/category/add.json",
            data:"categoryName="+$("#inputParentCategory").val(),
            error: function(request) {
                console.log(request);
            },
            success: function(data) {
                console.log(data);
                console.log("ok");
                location.reload();
            }
        });
    });

    $("#addChildCategoryBtn").click(function () {
        $.ajax({
            type: "POST",
            cache: true,
            url:"http://"+urla+"/manage/category/add.json",
            data:"parentId="+$(".modal").attr("id").substr(9,15)+"&categoryName="+$("#inputChildCategory").val(),
            error: function(request) {
                console.log(request);
            },
            success: function(data) {
                console.log(data);
                console.log("ok");
                location.reload();
            }
        });
    });

    $.ajax({
        url: "http://"+urla+"/manage/category/parallel_children.json",
        //url: "./json/categories/parent/0/list.json",
        type: "GET",
        dataType: 'JSON',
        success: function(result){
            console.log(result);
            $.each(result,function(i,item){
                $.each(item,function(x,obj){
                    var $panel=$("<div class='panel panel-default'></div>");
                    var $p_heading=$("<div class='panel-heading'>" +
                        "<span class='category-title'>"+obj.name+"</span></div>");
                    var $p_body=$("<div class='panel-body' id='pb"+obj.id+"'></div>");
                    addChildCategory(obj.id);
                    var $btn=$("<button type='button' class='btn btn-success btn-lg' data-toggle='modal' " +
                        "data-target='#add_child"+obj.id+"' id='btn"+obj.id+"'>" +
                        "<span class='glyphicon glyphicon-plus'></span> 新增 </button>");
                    $p_body.append($btn);
                    $panel.append($p_heading,$p_body);
                    $(".category-box").append($panel);

                    $("#btn"+obj.id).click(function () {
                        $(".modal").attr("id","add_child"+obj.id);
                    });
                })
            });
        },
        error:function (data) {
            console.log("X");
            console.log(data);
        }
    });

    function addChildCategory(id) {
        $.ajax({
            url: "http://"+urla+"/manage/category/parallel_children.json?categoryId="+id,
            //url: "./json/categories/parent/0/list.json",
            type: "GET",
            dataType: 'JSON',
            success: function(result){
                console.log(result);
                $.each(result,function(i,item){
                    $.each(item,function(x,obj){
                        var $div=$("<div class='btn btn-info btn-lg category-item'>"+obj.name+"</div>");
                        $("#pb"+id).append($div);
                    })
                });
            },
            error:function (data) {
                console.log("X");
                console.log(data);
            }
        });
    }


});