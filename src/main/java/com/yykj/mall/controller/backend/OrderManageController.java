package com.yykj.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.service.IOrderService;
import com.yykj.mall.service.IUserService;
import com.yykj.mall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Lee on 2017/8/29.
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {


    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iOrderService.manageList(pageNum,pageSize);

    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo){

        return iOrderService.manageDetail(orderNo);

    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iOrderService.manageSearch(orderNo,pageNum,pageSize);
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo){
        return iOrderService.manageSendGoods(orderNo);
    }
}
