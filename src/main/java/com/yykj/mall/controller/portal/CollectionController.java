package com.yykj.mall.controller.portal;

import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.entity.User;
import com.yykj.mall.service.ICollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Lee
 * @date 2017/12/2
 */
@Controller
@RequestMapping("/collection/")
public class CollectionController {

    @Autowired
    private ICollectionService collectionService;

    @RequestMapping(value = "list.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCollectionList(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return collectionService.listCollectedProduct(user.getId());
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping(value = "add.json", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> collectProduct(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return collectionService.collect(user.getId(), productId);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping(value = "cancel.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Boolean> cancelCollection(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return collectionService.cancelCollect(user.getId(), productId);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping(value = "collection_status.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Boolean> getCollectionStatus(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return collectionService.getCollectionStatus(user.getId(), productId);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }
}
