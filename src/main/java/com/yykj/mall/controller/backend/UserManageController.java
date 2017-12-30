package com.yykj.mall.controller.backend;

import com.yykj.mall.common.Const;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.entity.User;
import com.yykj.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Lee on 2017/8/15.
 */
@Controller
@RequestMapping(value = "/manage/users/")
public class UserManageController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()){
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            }
        }
        return ServerResponse.createByErrorMessage("登录失败！");
    }
}
