package com.artisankid.elementwar.tcpconnection.gate.utils;

import com.artisankid.elementwar.common.dao.EpicDao;
import com.artisankid.elementwar.common.ewmodel.Epic;

/**
 * Created by LiXiangYu on 2017/6/27.
 */
public class EpicManager {
    private static EpicDao dao = new EpicDao();

    static public void WriteEpic(String userID, String roomID, String history) {
        String timestamp = new Long(System.currentTimeMillis()).toString();

        Epic epic = new Epic();
        if(roomID != null) {
            epic.setEpicID(roomID);
        } else {
            epic.setEpicID(userID + ":" + timestamp);
        }
        epic.setHistory(history);

        dao.insert(userID, epic);
    }

    static public void WriteEpic(String ref_epicID, String history) {
        String timestamp = new Long(System.currentTimeMillis()).toString();

        Epic epic = new Epic();
        epic.setEpicID(ref_epicID + ":" + timestamp);
        epic.setHistory(history);

        dao.insert(epic, ref_epicID);
    }
}
