package com.yykj.mall.controller.portal;

import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.User;
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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.html", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String s, String type){
        return iUserService.checkValid(s, type);
    }

    @RequestMapping(value = "get_user_info.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息！");
    }

    @RequestMapping(value = "forget_get_question.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    //返回一个token,用于重置密码时的校验
    @RequestMapping(value = "forget_check_answer.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_reset_password.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String giveToken){
        return iUserService.forgetResetPassword(username, newPassword, giveToken);
    }

    @RequestMapping(value = "reset_password.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(HttpSession session, String oldPassword, String newPassword){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录！");
        }
        return iUserService.resetPassword(user, newPassword, oldPassword);
    }

    @RequestMapping(value = "update_information.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User newUserInfo){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录！");
        }
        newUserInfo.setId(currentUser.getId());
        newUserInfo.setUsername(currentUser.getUsername());//防止横向越权，固定id和username
        ServerResponse<User> response = iUserService.updateInformation(newUserInfo);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get_information.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，跳转到登录界面！");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
