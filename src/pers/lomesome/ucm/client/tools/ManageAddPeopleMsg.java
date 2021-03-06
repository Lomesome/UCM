package pers.lomesome.ucm.client.tools;

/**
 * 功能：向服务器发送添加好友的包
 */

import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ManageAddPeopleMsg {

    public static void getAddPeopleMsg(String userid){
        Message message = new Message();
        message.setSender(userid);
        message.setMesType(MessageType.MESSAGE_GET_HISTORY_ADDPROPLE);
        // 客户端A发送给服务器
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(userid).getS().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
