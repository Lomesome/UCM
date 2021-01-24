package pers.lomesome.ucm.client.tools;

/**
 * 功能：管理添加好友界面
 */


import pers.lomesome.ucm.client.view.AddFriend;

public class ManageAddFriend {

    private static AddFriend addfriend = null;

    public static AddFriend getAddfriend() {
        return addfriend;
    }

    public static void setAddfriend(AddFriend addfriend) {
        ManageAddFriend.addfriend = addfriend;
    }

    public static void delStage(){
        addfriend = null;
    }
}
