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

    public int insert(UserDO var1) {

        return this.executeInsert("user.insertUser",var1);
    }

    public int update(UserDO var1) {

        return this.executeUpdate("user.updateUser",var1);
    }

    public UserDO select(Long var1) {

        return this.executeQueryForObject("",var1);
    }

    public List<UserDO> queryPage(UserQuery var1) {

        return this.executeQueryForList("",var1);
    }

    public int queryPageCount(UserQuery var1) {

        return this.executeQuerySize("",var1);
    }

    public List<UserDO> selectList(UserQuery var1) {

        return this.executeQueryForList("",var1);
    }
}
