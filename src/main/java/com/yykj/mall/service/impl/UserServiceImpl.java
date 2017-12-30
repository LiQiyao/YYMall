package com.yykj.mall.service.impl;

import com.yykj.mall.common.Const;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.common.TokenCache;
import com.yykj.mall.dao.UserMapper;
import com.yykj.mall.entity.User;
import com.yykj.mall.service.IUserService;
import com.yykj.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Lee on 2017/8/12.
 */
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        if (userMapper.checkUsername(username) == 0){
            return ServerResponse.createByErrorMessage("此用户不存在！");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null){
            return ServerResponse.createByErrorMessage("密码错误!");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int res = userMapper.insert(user);
        if (res == 0){
            return ServerResponse.createByErrorMessage("注册失败！");
        }
        return ServerResponse.createBySuccessMessage("注册成功！");
    }

    @Override
    public ServerResponse<String> checkValid(String s, String type) {
        if (StringUtils.isNotBlank(type)){
            if (Const.EMAIL.equals(type)){
                int res = userMapper.checkEmail(s);
                if (res > 0){
                    return ServerResponse.createByErrorMessage("邮箱已被注册！");
                }
            }
            if (Const.USERNAME.equals(type)){
                int res = userMapper.checkUsername(s);
                if (res > 0){
                    return ServerResponse.createByErrorMessage("用户名已被注册！");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("类型参数错误！");
        }
        return ServerResponse.createBySuccessMessage("校验成功！");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()){
            return ServerResponse.createByErrorMessage("该用户不存在！");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("问题为空！");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int res = userMapper.checkAnswer(username, question, answer);
        if (res > 0){
            String token = UUID.randomUUID().toString();
            TokenCache.addKeyAndValue(TokenCache.TOKEN_PREFIX + username, token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage("问题答案错误！");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String giveToken) {
        ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()){
            return ServerResponse.createByErrorMessage("该用户不存在！");
        }
        if (StringUtils.isBlank(giveToken)){
            return ServerResponse.createByErrorMessage("传递的token为空！");
        }
        String cachedToken = TokenCache.getValue(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(cachedToken)){
            return ServerResponse.createByErrorMessage("token无效或已过期！");
        }
        if (StringUtils.equals(giveToken, cachedToken)){
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int res = userMapper.updatePasswordByUsername(username, md5Password);
            if (res > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功！");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取！");
        }
        return ServerResponse.createByErrorMessage("修改密码失败！");
    }

    @Override
    public ServerResponse<String> resetPassword(User user, String newPassword, String oldPassword) {
        int res = userMapper.checkPassword(user.getId(), MD5Util.MD5EncodeUtf8(oldPassword));
        if (res == 0){
            return ServerResponse.createByErrorMessage("密码输入错误！");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        res = userMapper.updateByPrimaryKeySelective(user);
        if (res > 0){
            return ServerResponse.createBySuccessMessage("修改密码成功！");
        }
        return ServerResponse.createByErrorMessage("修改密码失败！");
    }

    @Override
    public ServerResponse<User> updateInformation(User newUserInfo) {
        int res = userMapper.checkEmailById(newUserInfo.getId(), newUserInfo.getEmail());
        if (res > 0){
            return ServerResponse.createByErrorMessage("该邮箱已被其他用户占用！");
        }
        res = userMapper.updateByPrimaryKeySelective(newUserInfo);
        if (res > 0){
            return ServerResponse.createBySuccess("更新个人信息成功！", newUserInfo);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败！");
    }

    @Override
    public ServerResponse<User> getInformation(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServerResponse.createByErrorMessage("找不到用户信息！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
