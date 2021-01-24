package pers.lomesome.ucm.client.tools;

/**
 * 管理好友列表
 */

import pers.lomesome.ucm.common.PeopleInformation;

import java.util.*;

public class ManageFriendList {
    private static HashMap<String, PeopleInformation> friendlist = new HashMap<>();

    // 将界面添加到集合中
    public static void addFriend(PeopleInformation peopleInformation) {
        friendlist.put(peopleInformation.getUserid(), peopleInformation);
    }

    public static void addFriends(List<PeopleInformation> peopleInformations){
        for(PeopleInformation peopleInformation:peopleInformations){
            friendlist.put(peopleInformation.getUserid(), peopleInformation);
        }
    }
    public static PeopleInformation getFriend(String userid){
        return friendlist.get(userid);
    }

    // 从集合中获取界面
    public static HashMap getFriendList() {
        return friendlist;
    }
}
