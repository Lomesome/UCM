package pers.lomesome.ucm.common;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private static final long serialVersionUID = 5160728458299623666L;

    private String mesType;	//消息种类

    private String sender;	//发送者

    private String getter;	//接收者

    private String content;	//内容

    private String sendTime; //时间

    private List lists;

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public List getLists() {
        return lists;
    }

    public void setLists(List lists) {
        this.lists = lists;
    }

    @Override
    public String toString(){
        return "type: " + mesType + " sender: " + sender + " getter: " + getter + " content: " + content;
    }
}
