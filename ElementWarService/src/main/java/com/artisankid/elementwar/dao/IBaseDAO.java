package com.artisankid.elementwar.dao;

import com.artisankid.elementwar.dao.dataobject.BaseDO;
import com.artisankid.elementwar.dao.dataobject.BaseQuery;

import java.util.List;

/**
 * Created by shaohua.wangshaohu on 2017/4/5.
 */
public interface IBaseDAO<T extends BaseDO, REQUEST extends BaseQuery> {

    int insert(T var1);

    int update(T var1);

    T select(Long var1);

    List<T> queryPage(REQUEST var1);

    int queryPageCount(REQUEST var1);

    List<T> selectList(REQUEST var1);
}
