package com.yykj.mall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.User;
import com.yykj.mall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lee on 2017/8/25.
 */
@RequestMapping("/order/")
@Controller
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(value = "create.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId){
/*        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }*/
        return iOrderService.create(1, shippingId);
    }

    @RequestMapping(value = "cancel.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo){
/*        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }*/
        return iOrderService.cancel(1, orderNo);
    }


    @RequestMapping(value = "get_cart_checked_product.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCartCheckedProduct(HttpSession session){
/*        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }*/
        return iOrderService.getCartCheckedProduct(1);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session,Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getDetail(user.getId(),orderNo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getList(user.getId(),pageNum,pageSize);
    }







    @RequestMapping(value = "pay.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
/*        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }*/
        //存二维码图片的地址
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, 1, path);
    }
    @RequestMapping(value = "alipay_callback.do", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        logger.info(requestParams + "");
        for (String keyItem : requestParams.keySet()){
            String[] values = requestParams.get(keyItem);
            String valueStr = "";
            for (int i = 0; i < values.length; i++){
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(keyItem, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
        //验证回调的正确性，来源是否是支付宝，避免重复回调
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("请停止非法请求！！！");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调失败");
        }
        //todo 验证订单各种数据

        ServerResponse serverResponse = iOrderService.alipayCallback(params);
        if (serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }
    @RequestMapping(value = "query_order_pay_status.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
/*        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }*/
        //存二维码图片的地址
        return iOrderService.queryOrderPayStatus(1, orderNo);
    }



}
