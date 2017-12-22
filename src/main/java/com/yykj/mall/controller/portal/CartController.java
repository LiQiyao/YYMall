package com.yykj.mall.controller.portal;

import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.entity.User;
import com.yykj.mall.service.ICartService;
import com.yykj.mall.dto.CartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Lee on 2017/8/20.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService cartService;

    @RequestMapping(value = "add.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartDTO> add(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.add(user.getId(), productId, count);
    }

    @RequestMapping(value = "list.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartDTO> list(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.list(user.getId());
    }

    @RequestMapping(value = "update.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartDTO> update(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.update(user.getId(),productId,count);
    }

    @RequestMapping(value = "products/delete.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartDTO> delete_products(HttpSession session, String productIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.deleteProducts(user.getId(), productIds);
    }

    @RequestMapping(value = "check_all.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartDTO> checkAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.checkOrUnCheck(user.getId(), null, Const.Cart.CHECKED);
    }

    @RequestMapping(value = "un_check_all.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartDTO> unCheckAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.checkOrUnCheck(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @RequestMapping(value = "check.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartDTO> check(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.checkOrUnCheck(user.getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping(value = "un_check.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartDTO> unCheck(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.checkOrUnCheck(user.getId(), productId, Const.Cart.UN_CHECKED);
    }
}
