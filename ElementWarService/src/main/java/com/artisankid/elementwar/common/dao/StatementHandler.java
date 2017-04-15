package com.artisankid.elementwar.common.dao;

import java.sql.PreparedStatement;

/**
 * Created by LiXiangYu on 2017/4/15.
 */
public interface StatementHandler {
    void supplyToStatement(PreparedStatement statement);
}
