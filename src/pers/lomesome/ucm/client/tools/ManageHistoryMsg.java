package pers.lomesome.ucm.client.tools;

/**
 * 功能：向服务器发送获取历史消息的包
 */

import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManageHistoryMsg {
    private static List<Message> historyMsg = new ArrayList<>();
    private static String filename = "";

    public static void init(){
        if(OSinfo.isWindows()){
            filename = System.getProperty("user.home") + "/AppData/Local/UCM/.history.bin";
        }else {
            filename = "./.history.bin";
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));
            oos.writeObject(historyMsg);
            oos.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void addhistoryMsg(Message message){
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(filename)));
            historyMsg = (List<Message>) ois.readObject();
        } catch (Exception e) {
            historyMsg = new ArrayList<>();
        } finally {
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
            }
        }
        historyMsg.add(message);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));
            oos.writeObject(historyMsg);
            oos.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        historyMsg = null;
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
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(filename)));
            historyMsg = (List<Message>) ois.readObject();
        } catch (Exception e) {
        } finally {
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
            }
        }
        List<Message> newList = new ArrayList<>();
        for(Message message:historyMsg){
            if(otherid.equals(message.getSender()) || otherid.equals(message.getGetter())){
                newList.add(message);
            }
        }
        historyMsg = null;
        return newList;
    }
}
