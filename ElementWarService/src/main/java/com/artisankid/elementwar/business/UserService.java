package com.artisankid.elementwar.business;

import com.artisankid.elementwar.dao.IUserDAO;
import com.artisankid.elementwar.dao.dataobject.UserDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by shaohua.wangshaohu on 2017/4/10.
 */
@Service
public class UserService {

    @Resource(name = "userDAO")
    private IUserDAO userDAO;

    public UserDO getUserById(Long id) {
        UserDO userDO = userDAO.select(id);
        return userDO;
    }
}
