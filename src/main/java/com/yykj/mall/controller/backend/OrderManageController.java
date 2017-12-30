package com.yykj.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.service.IOrderService;
import com.yykj.mall.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private IOrderService orderService;

    @RequestMapping(value = "list.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session,
                                              @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return orderService.manageList(pageNum,pageSize);

    }

    @RequestMapping(value = "detail.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<OrderDTO> getDetail(HttpSession session, Long orderNo){

        return orderService.manageDetail(orderNo);

    }

    @RequestMapping(value = "search.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> search(HttpSession session,
                                           Long orderNo,
                                           @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return orderService.manageSearch(orderNo,pageNum,pageSize);
    }

    @RequestMapping(value = "send_goods.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpSession session, Long orderNo){
        return orderService.manageSendGoods(orderNo);
    }
}
