package pers.lomesome.ucm.client.tools;

/**
 * 功能：向服务器发送获取好友的包
 */

import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ManageMyFriendsMsg {
    public static void getMyFriends(String userid){
        Message message = new Message();
        message.setSender(userid);
        message.setMesType(MessageType.MESSAGE_GET_MYFRIEND);
        // 客户端A发送给服务器
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(userid).getS().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void myInformationChangeToFriends(String friendid){
        Message message = new Message();
        message.setSender(OwnInformation.getMyinformation().getUserid());
        message.setGetter(friendid);
        List list = new ArrayList();
        list.add(OwnInformation.getMyinformation());
        message.setLists(list);
        message.setMesType(MessageType.MESSAGE_MY_IMFORMATION_TO_FRIENDS);
        // 客户端A发送给服务器
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        list = null;
    }
}
