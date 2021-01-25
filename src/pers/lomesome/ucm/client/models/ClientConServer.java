package pers.lomesome.ucm.client.models;

/**
 * 功能：客户端连接服务器的后台
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import pers.lomesome.ucm.client.tools.OwnInformation;
import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.PeopleInformation;
import pers.lomesome.ucm.common.User;
import pers.lomesome.ucm.client.tools.ClientConServerThread;
import pers.lomesome.ucm.client.tools.ManageClientConServerThread;

public class ClientConServer {
    public Socket s;

    // 发送第一次请求
    public String SendLoginInfoTOServer(Object o) {
        String re = MessageType.MESSAGE_NO_CONNECTED;
        try {
            // 连接127.0.0.1的8080端口
            s = new Socket("59.110.125.3", 8080);
//            s = new Socket("127.0.0.1", 8080);
            // 向服务器发送账号信息
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(o);

            // 从服务器收到验证是否通过的Message对象
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            Message ms = (Message) ois.readObject();
            re = ms.getMesType();
            // 验证登录是否成功
            if (ms.getMesType().equals(MessageType.MESSAGE_SUCCEED)) {
                // 创建一个该qq和服务器端保持通讯连接的线程
                ClientConServerThread ccst = new ClientConServerThread(s);
                // 启动该线程
                ccst.start();
                ManageClientConServerThread.addClientConServerThread(((User) o).getUserId(), ccst);
                OwnInformation.setMyinformation((PeopleInformation) ms.getLists().get(0));
            } else {
                // 关闭Scoket
                s.close();
            }
        } catch (Exception e) {

        } finally {

        }
        return re;
    }
}