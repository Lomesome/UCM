package pers.lomesome.ucm.client.tools;

/**
 * 功能：向服务器发送获取历史消息的包
 */

import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ManageHistoryMsg {
    private static List<Message> historyMsg = new ArrayList<>();

    public static void addhistoryMsg(Message message){
        historyMsg.add(message);
    }

    public static List<Message> getHistoryMsg() {
        return historyMsg;
    }

    public static void getHistoryMsg(String userid){
        Message message = new Message();
        message.setSender(userid);
        message.setMesType(MessageType.MESSAGE_GET_HISTORY);
        // 客户端A发送给服务器
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(userid).getS().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public static List<Message> getHistoryMsgWithO(String otherid) {
        List<Message> newList = new ArrayList<>();
        for(Message message:historyMsg){
            if(otherid.equals(message.getSender()) || otherid.equals(message.getGetter())){
                newList.add(message);
            }
        }
        return newList;
    }
}
