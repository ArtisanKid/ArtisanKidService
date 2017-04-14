package com.artisankid.elementwar.controller.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shaohua.wangshaohu on 2017/4/5.
 */
@Controller
@RequestMapping(value = "/testController")
public class TestController {


    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value="/test.json",method = RequestMethod.GET)
    @ResponseBody
    public String testMethod(){
        logger.error("com.artisankid.elementwar.controller.TestController.testMethod");
        return "com.artisankid.elementwar.controller.TestController.testMethod";
    }
}
