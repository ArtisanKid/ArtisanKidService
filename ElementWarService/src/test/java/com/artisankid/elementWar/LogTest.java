package com.artisankid.elementWar;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shaohua.wangshaohu on 2017/4/5.
 */

public class LogTest {
    private Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void testLog() {
        logger.info("testLog");
    }
}
