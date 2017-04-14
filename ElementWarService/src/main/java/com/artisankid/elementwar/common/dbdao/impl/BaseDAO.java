package com.artisankid.elementwar.common.dbdao.impl;


import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by shaohua.wangshaohu on 2017/4/10.
 */
public class BaseDAO<T> {

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    protected int executeInsert(String statementName, Object parameterObject) {
        return this.sqlSessionTemplate.insert(statementName, parameterObject);
    }

    protected T executeQueryForObject(String statementName, Object parameterObject) {
        return this.sqlSessionTemplate.selectOne(statementName, parameterObject);
    }

    protected int executeQuerySize(String statementName, Object parameterObject) {
        Integer tempResult = this.sqlSessionTemplate.selectOne(statementName, parameterObject);
        if (tempResult == null) {
            return 0;
        }
        return tempResult.intValue();
    }

    protected int executeUpdate(String statementName, Object parameterObject) {
        return sqlSessionTemplate.update(statementName, parameterObject);
    }

    protected List<T> executeQueryForList(String statementName, Object parameterObject) {
        return this.sqlSessionTemplate.selectList(statementName, parameterObject);
    }
}
