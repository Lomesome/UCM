package pers.lomesome.ucm.client.tools;

/**
 * 功能：客户端连接服务器线程
 */

import java.net.*;
import java.io.*;
import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.PeopleInformation;

public class ClientConServerThread extends Thread {
    private Socket s;

    // 构造方法
    public ClientConServerThread(Socket s) {
        this.s = s;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    public void run() {
        while (true) {
            // 不停的读取从服务器发来的消息
            try {
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message m = (Message) ois.readObject();
                if (m.getMesType().equals(MessageType.MESSAGE_COMM) || m.getMesType().equals(MessageType.MESSAGE_COMM_IMAGE) ) {
                    ManageMainGUI.getMainGui().showMsg(m,true);
                }else if (m.getMesType().equals(MessageType.MESSAGE_RET_MYFRIEND)) {
                    // 返回在线好友的包
                    // getter是相对于服务器的接收者，也就是自己的QQ
                    if (m.getLists() != null) {
                        ManageMainGUI.getMainGui().setAllfriends(m.getLists());
                        ManageFriendList.addFriends(m.getLists());
                    }
                }else if (m.getMesType().equals(MessageType.MESSAGE_EXIT)) {


                }else if (m.getMesType().equals(MessageType.MESSAGE_RET_NOREAD) || m.getMesType().equals(MessageType.MESSAGE_RET_HISTORY)) {
                    if (m.getLists() != null) {
                        for (Object o : m.getLists()) {
                            Message message = (Message) o;
                            if (message.getMesType().equals(MessageType.MESSAGE_COMM) || message.getMesType().equals(MessageType.MESSAGE_COMM_IMAGE)) {
                                ManageMainGUI.getMainGui().showMsg(message, true);
                            }
                        }
                    }
                }else if (m.getMesType().equals(MessageType.MESSAGE_RET_HISTORY_ADDPROPLE)) {
                    if (m.getLists() != null) {
                        for (Object o : m.getLists()) {
                            Message message = (Message) o;
                            if(!OwnInformation.getMyinformation().equalsObjct((PeopleInformation) message.getLists().get(0))) {
                                if (message.getSender().equals(OwnInformation.getMyinformation().getUserid())) {
                                    ManageMainGUI.getMainGui().setMakeBox(message);
                                } else {
                                    ManageMainGUI.getMainGui().setMakeBox2(message);
                                }
                            }
                        }
                    }
                }else if(m.getMesType().equals(MessageType.MESSAGE_RET_FINDPEOPLE)){
                    ManageAddFriend.getAddfriend().setObjectmsgbox(m.getLists());
                }else if(m.getMesType().equals(MessageType.MESSAGE_RET_ADDPEOPLE)){
                    if(m.getContent().equals("adding")){
                        ManageMainGUI.getMainGui().addFriendRequest(m.getLists());
                    } else if(m.getContent().equals("added")){
                        ManageMainGUI.getMainGui().setMakeBox2(m);
                        ManageMainGUI.getMainGui().setAllfriends(m.getLists());
                        ManageFriendList.addFriends(m.getLists());
                    } else if(m.getContent().equals("refuse")){
                        ManageMainGUI.getMainGui().setMakeBox2(m);
                    }
                }else if(m.getMesType().equals(MessageType.MESSAGE_MY_IMFORMATION_TO_FRIENDS)){
                    ManageFriendList.getFriendList().put(m.getSender(),m.getLists().get(0));
                    ManageMainGUI.getMainGui().setNewFriendsName((PeopleInformation) m.getLists().get(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
