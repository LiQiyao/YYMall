$(document).ready(function () {

    //页面调整
    var widowheight = $(window).height() - 50;
    $("body").css("height", widowheight);

    var Chart1 = echarts.init(document.getElementById('product_graph'));
    var option1 = {
         title : {
             text: '全部商品',
             x:'center'
         },
         tooltip : {
             trigger: 'item',
             formatter: "{a} <br/>{b} : {c} ({d}%)"
         },

         series : [
             {
                 name: '分类',
                 type: 'pie',
                 radius : '55%',
                 center: ['50%', '60%'],
                 data:[
                     {value:335, name:'居家'},
                     {value:310, name:'餐厨'},
                     {value:234, name:'配件'},
                     {value:135, name:'服装'},
                     {value:348, name:'电器'}
                 ],
                 itemStyle: {
                     emphasis: {
                         shadowBlur: 10,
                         shadowOffsetX: 0,
                         shadowColor: 'rgba(0, 0, 0, 0.5)'
                     }
                 }
             }
         ]
     };
    Chart1.setOption(option1);


    var Chart2 = echarts.init(document.getElementById('order_graph'));
    var option2 = {
        color: ['#3398DB'],
        title : {
            text: '一周访问量',
            x:'center'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data : ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'直接访问',
                type:'bar',
                barWidth: '60%',
                data:[10, 52, 200, 334, 390, 330, 220]
            }
        ]
    };

    Chart2.setOption(option2);

});