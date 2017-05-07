package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.BaseMagician;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.utils.MagicianManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
public class MagicianDao {

    /**
     * 根据连接状态查询用户
     *
     * @param state
     * @param offset
     * @param pageSize
     * @return
     */
    public List<BaseMagician> selectByState(BaseMagician.ConnectState state, int offset, int pageSize) {
        String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE connect_state = '" + state.getValue()
                + "' LIMIT " + offset + "," + pageSize + ";";
        return this.selectMagiciansBySQL

                (sql);
    }

    /**
     * 根据连接状态查询用户
     *
     * @param openID
     * @param relation
     * @param offset
     * @param pageSize
     * @return
     */
    public List<BaseMagician> selectByRelation(String openID, BaseMagician.UserRelation relation, int offset, int pageSize) {
        String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE openID IN (SELECT openID FROM Magician_Magician WHERE openID = '" + openID + "' AND relation = '" + relation.getValue() + "' ORDER BY create_time LIMIT " + offset + "," + pageSize + ") ORDER BY create_time;";
        return this.selectMagiciansBySQL(sql);
    }

    /**
     * 根据排名查询用户
     *
     * @param offset
     * @param pageSize
     * @return
     */
    public List<BaseMagician> selectByRank(int offset, int pageSize) {
        String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID ORDER BY rank DESC LIMIT " + offset + "," + pageSize + ";";
        return this.selectMagiciansBySQL(sql);
    }

    /**
     * 根据房间查询用户
     *
     * @param roomID
     * @return
     */
    public List<BaseMagician> selectByRoomID(String roomID) {
        String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE openID IN (SELECT openID FROM Room_Magician WHERE roomID = '" + roomID + "');";
        return this.selectMagiciansBySQL(sql);
    }

    private List<BaseMagician> selectMagiciansBySQL(String sql) {
        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        List<Map<String, Object>> result = manager.select(sql);
        manager.close();

        List<BaseMagician> objects = new ArrayList<>();
        for (Map<String, Object> map : result) {
            BaseMagician object = new BaseMagician();
            object.setOpenID((String) map.get("openID"));
            object.setNickname((String) map.get("nickname"));
            object.setSmallPortrait((String) map.get("small_portrait"));
            object.setStrength((Integer) map.get("strength"));
            object.setHonor((String) map.get("honor"));
            objects.add(object);
        }
        return objects;
    }

    /*
     * 根据userID查询用户
     *
     * @param openID
     * @return
     */
    public Magician selectByUserID(String userID) {
        String sql = "SELECT * FROM Magician LEFT JOIN User ON Magician.userID = User.userID WHERE Magician.userID = '" + userID + "';";
        return this.selectMagicianBySQL(sql);
    }

    /*
     * 根据openID和登录平台查询用户
     *
     * @param openID
     * @return
     */
    public Magician selectByOpenID(String openID) {
        String sql = "SELECT * FROM Magician LEFT JOIN User ON Magician.userID = User.userID WHERE openID = '" + openID + "';";
        return this.selectMagicianBySQL(sql);
    }

    /**
     * 根据昵称查询用户
     *
     * @param nickname
     * @return
     */
    public Magician selectByNickname(String nickname) {
        String sql = "SELECT * FROM Magician LEFT JOIN User ON Magician.userID = User.userID WHERE nickname = '" + nickname + "';";
        return this.selectMagicianBySQL(sql);
    }

    private Magician selectMagicianBySQL(String sql) {
        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        Map<String, Object> result = manager.selectOne(sql);
        manager.close();

        if(result == null) {
            return null;
        }

        Magician object = new Magician();
        object.setOpenID((String) result.get("openID"));
        object.setNickname((String) result.get("nickname"));
        object.setSmallPortrait((String) result.get("small_portrait"));
        object.setStrength((Integer) result.get("strength"));
        object.setHonor((String) result.get("honor"));

        object.setPortrait((String) result.get("portrait"));
        object.setLargePortrait((String) result.get("large_portrait"));
        object.setMobile((String) result.get("mobile"));

        Date birthday = (Date)result.get("birthday");
        object.setBirthday(birthday.getTime());
        object.setMotto((String) result.get("motto"));

        int win_count = (Integer)result.get("win_count");
        int lose_count = (Integer)result.get("lose_count");
        int total_play_count = win_count + lose_count;
        if(total_play_count > 0) {
            object.setWinPercent(win_count / total_play_count);
        }

        long man_synthesis_count = (Long)result.get("man_synthesis_count");
        long auto_synthesis_count = (Long)result.get("auto_synthesis_count");
        long total_synthesis_count = auto_synthesis_count + auto_synthesis_count;
        if(total_synthesis_count > 0) {
            object.setManSynthesisPercent(man_synthesis_count / total_synthesis_count);
            object.setAutoSynthesisPercent(auto_synthesis_count / total_synthesis_count);
        }
        object.setRank((Long) result.get("rank"));

        return object;
    }

    /**
     * 插入新的魔法师
     * @param userID
     * @return
     */
    public boolean insert(String userID) {
        String openID = MagicianManager.CreateOpenID(userID);
        String sql = "INSERT INTO Magician (userID, openID) VALUES ('" + userID + "', '" + openID + "');";

        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        boolean result = manager.insert(sql);
        manager.close();
        return result;
    }

    /**
     * 更新用户信息
     *
     * @param magician
     * @return
     */
    public boolean update(Magician magician) {
        String updateUserSQL = "UPDATE User SET "
                + "nickname = '" + magician.getNickname() + "', "
                + "portrait = '" + magician.getPortrait() + "', "
                + "small_portrait = '" + magician.getSmallPortrait() + "', "
                + "large_portrait = '" + magician.getLargePortrait() + "', "
                + "mobile = '" + magician.getMobile() + "' "
                + "WHERE userID = (SELECT userID FROM Magician WHERE openID = '" + magician.getOpenID() + "');";

        String updateMagicianSQL = "UPDATE Magician SET "
                + "strength = '" + magician.getStrength() + "', "
                + "honor = '" + magician.getHonor() + "' "
                + "WHERE openID = '" + magician.getOpenID() + "';";

        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        boolean updateUserResult = manager.update(updateUserSQL);
        boolean updateMagicianResult = manager.update(updateMagicianSQL);
        manager.close();
        return updateUserResult && updateMagicianResult;
    }

    /**
     * 插入用户关系
     *
     * @param openID
     * @param targetOpenID
     * @param relation
     * @return
     */
    public boolean insertRelation(String openID, String targetOpenID, BaseMagician.UserRelation relation) {
        String sql = "INSERT INTO Magician_Relation (openID, ref_openID, type) VALUES ('"
                + openID + "', '"
                + targetOpenID + "', '"
                + relation.getValue() + "');";

        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        boolean result = manager.insert(sql);
        manager.close();
        return result;
    }

    /**
     * 更新用户关系
     *
     * @param openID
     * @param targetOpenID
     * @param relation
     * @return
     */
    public boolean updateRelation(String openID, String targetOpenID, BaseMagician.UserRelation relation) {
        String sql = "UPDATE Magician_Relation SET "
                + "type = '" + relation.getValue() + "' "
                + "WHERE openID = '" + openID + "' AND ref_openID = '" + targetOpenID + "';";

        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        boolean result = manager.insert(sql);
        manager.close();
        return result;
    }
}
