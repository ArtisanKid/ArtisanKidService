package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.BaseMagician;
import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Level;
import com.artisankid.elementwar.common.ewmodel.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiXiangYu on 2017/4/16.
 */
public class RoomDao {
    /**
     * 查询全部等级对象
     * @return
     */
    public List<Room> selectAll() {
        String sql = "SELECT * FROM Room LEFT JOIN Room_Magician";

        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        List<Map<String, Object>> results = manager.select(sql);
        manager.close();

        List<Room> objects = new ArrayList<>();
        for (Map<String, Object> result : results) {
            Room object = new Room();
            object.setRoomID(result.get("roomID").toString());
            objects.add(object);
        }
        return objects;
    }

    /**
     * 根据房间ID查询反应条件对象
     * @param roomID
     * @return
     */
    public Room selectByRoomID(String roomID) {
        String sql = "SELECT * FROM Room WHERE roomID = '" + roomID + "';";
        return selectRoomBySQL(sql);
    }

    /**
     * 根据魔法师ID查询反应条件对象
     * @param openID
     * @return
     */
    public Room selectByOpenID(String openID) {
        String sql = "SELECT roomID FROM Room_Magician WHERE openID = '" + openID + "';";
        return selectRoomBySQL(sql);
    }

    public Room selectRoomBySQL(String sql) {
        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        Map<String, Object> result = manager.selectOne(sql);
        manager.close();

        if(result == null) {
            return null;
        }

        Room object = new Room();
        object.setRoomID(result.get("roomID").toString());

        UserDao dao = new UserDao();
        List<BaseMagician> magicians = dao.selectByRoomID(object.getRoomID());
        object.setMagicians(magicians);
        return object;
    }

    /**
     * 插入Room对象
     * @param object
     * @return
     */
    public boolean insert(Room object) {
        String insertRoomSQL = "INSERT INTO Room (roomID) VALUES ('"
                + object.getRoomID() + "');";
        List<String> insertRoomMagicianSQLs = new ArrayList<>();
        for(BaseMagician magician : object.getMagicians()) {
            String insertRoomMagicianSQL = "INSERT INTO Room_Magician (roomID, openID) VALUES ('"
                    + object.getRoomID() + "', '"
                    + magician.getOpenID() + "');";
            insertRoomMagicianSQLs.add(insertRoomMagicianSQL);
        }

        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        boolean result = manager.insert(insertRoomSQL);
        for(String insertRoomMagicianSQL: insertRoomMagicianSQLs) {
            result = manager.insert(insertRoomMagicianSQL);
        }
        manager.close();
        return result;
    }

    /**
     * 删除Room对象
     * @param roomID
     * @return
     */
    public boolean delete(String roomID) {
        String deleteRoomSQL = "DELETE FROM Room WHERE roomID = '" + roomID + "';";
        String deleteRoomMagicianSQL = "DELETE FROM Room_Magician WHERE roomID = '" + roomID + "';";
        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        boolean deleteRoomResult = manager.delete(deleteRoomSQL);
        boolean deleteRoomMagicianResult = manager.delete(deleteRoomMagicianSQL);
        manager.close();
        return deleteRoomResult && deleteRoomMagicianResult;
    }
}
