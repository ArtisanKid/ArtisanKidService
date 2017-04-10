package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.dao.dataobject.BaseDO;
import com.artisankid.elementwar.common.dao.dataobject.BaseQuery;

import java.util.List;

/**
 * Created by shaohua.wangshaohu on 2017/4/5.
 */
public interface IBaseDAO<T extends BaseDO, REQUEST extends BaseQuery> {

    Long insert(T var1);

    Integer update(T var1);

    T select(Long var1);

    List<T> queryPage(REQUEST var1);

    Integer queryPageCount(REQUEST var1);

    List<T> selectList(REQUEST var1);
}
