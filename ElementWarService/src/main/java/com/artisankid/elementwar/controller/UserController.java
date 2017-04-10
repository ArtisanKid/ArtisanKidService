package com.artisankid.elementwar.controller;

import com.alibaba.fastjson.JSON;
import com.artisankid.elementwar.business.UserService;
import com.artisankid.elementwar.dao.dataobject.UserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shaohua.wangshaohu on 2017/4/10.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value="/getUserById",method = RequestMethod.GET)
    @ResponseBody
    public String getUserById(){
        logger.error("getUser start");
        UserDO userDO = userService.getUserById(1L);
        logger.error("getUser end");
        return JSON.toJSONString(userDO);
    }
}
