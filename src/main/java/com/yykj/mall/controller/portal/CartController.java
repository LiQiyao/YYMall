package com.yykj.mall.controller.portal;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.vo.CartVo;
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

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
        return null;
    }
}
