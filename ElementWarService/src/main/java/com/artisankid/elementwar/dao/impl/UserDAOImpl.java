package com.artisankid.elementwar.dao.impl;

import com.artisankid.elementwar.dao.IUserDAO;
import com.artisankid.elementwar.dao.dataobject.UserDO;
import com.artisankid.elementwar.dao.dataobject.UserQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shaohua.wangshaohu on 2017/4/10.
 */
@Repository("userDAO")
public class UserDAOImpl extends BaseDAO<UserDO> implements IUserDAO {
    /**
     * ≤Â»Î
     * @param var1
     * @return
     */
    public int insert(UserDO var1) {

        return this.executeInsert("users.insertUser",var1);
    }

    /**
     * update
     *
     * @param var1
     * @return
     */
    public int update(UserDO var1) {

        return this.executeUpdate("users.updateUser", var1);
    }

    /**
     * query one
     *
     * @param var1
     * @return
     */
    public UserDO select(Long var1) {

        return this.executeQueryForObject("users.selectUser",var1);
    }

    /**
     * query by page
     *
     * @param var1
     * @return
     */
    public List<UserDO> queryPage(UserQuery var1) {

        return this.executeQueryForList("",var1);
    }

    /**
     * query count
     *
     * @param var1
     * @return
     */
    public int queryPageCount(UserQuery var1) {

        return this.executeQuerySize("",var1);
    }

    /**
     * query List
     * @param var1
     * @return
     */
    public List<UserDO> selectList(UserQuery var1) {

        return this.executeQueryForList("",var1);
    }
}
