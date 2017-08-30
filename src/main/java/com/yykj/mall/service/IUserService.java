package com.yykj.mall.service;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.User;

/**
 * Created by Lee on 2017/8/12.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String s, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String newPassword, String giveToken);

    ServerResponse<String> resetPassword(User user, String newPassword, String oldPassword);

    ServerResponse<User> updateInformation(User newUserInfo);

    ServerResponse<User> getInformation(int userId);
}
