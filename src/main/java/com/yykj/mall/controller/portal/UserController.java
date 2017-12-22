package com.yykj.mall.controller.portal;

import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.entity.User;
import com.yykj.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Lee on 2017/8/12.
 */
@Controller
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return userService.register(user);
    }

    @RequestMapping(value = "register_check_valid.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String s, String type){
        return userService.checkValid(s, type);
    }


    @RequestMapping(value = "/forget_pwd/get_question.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userService.selectQuestion(username);
    }

    //返回一个token,用于重置密码时的校验
    @RequestMapping(value = "/forget_pwd/check_answer.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return userService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "/forget_pwd/reset_password.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String token){
        return userService.forgetResetPassword(username, newPassword, token);
    }

    @RequestMapping(value = "reset_password.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(HttpSession session, String oldPassword, String newPassword){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录！");
        }
        return userService.resetPassword(user, newPassword, oldPassword);
    }

    @RequestMapping(value = "/info/update.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User newUserInfo){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录！");
        }
        newUserInfo.setId(currentUser.getId());
        newUserInfo.setUsername(currentUser.getUsername());//防止横向越权，固定id和username
        ServerResponse<User> response = userService.updateInformation(newUserInfo);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "info.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，跳转到登录界面！");
        }
        return userService.getInformation(currentUser.getId());
    }
}
