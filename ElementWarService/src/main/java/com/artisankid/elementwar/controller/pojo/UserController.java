package com.artisankid.elementwar.controller.pojo;

import com.artisankid.elementwar.business.UserService;
import com.artisankid.elementwar.common.dbdao.dataobject.UserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/getUserById.json/{id}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String getUserById(@PathVariable("id") Long id){
        logger.error("getUser start");
        UserDO userDO = userService.getUserById(id);
        logger.error("getUser end");
        return userDO.toString();
    }
}
