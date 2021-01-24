package pers.lomesome.ucm.common;

public interface MessageType {
    String MESSAGE_SUCCEED = "1"; // 表明登录成功
    String MESSAGE_EXIT = "0"; // 表明退出登陆
    String MESSAGE_NO_CONNECTED = "404"; //网络未连接
    String MESSAGE_NO_ACCOUNT = "2"; //没有该帐号
    String MESSAGE_WRONG_PASSWORD = "3"; //帐号密码错误
    String MESSAGE_COMM = "4"; // 信息的包
    String MESSAGE_COMM_IMAGE = "5"; // 图片的包
    String MESSAGE_GET_MYFRIEND = "6";// 要求所有好友的包
    String MESSAGE_RET_MYFRIEND = "7";// 返回所有好友的包
    String MESSAGE_GET_NOREAD = "8"; //要求未读消息的包
    String MESSAGE_RET_NOREAD = "9"; //返回未读消息的包
    String MESSAGE_GET_HISTORY = "10"; //要求历史消息的包
    String MESSAGE_RET_HISTORY = "11"; //返回历史消息的包
    String MESSAGE_GET_FINDPEOPLE = "12"; //要求查找用户的包
    String MESSAGE_RET_FINDPEOPLE = "13"; //返回查找用户的包
    String MESSAGE_GET_ADDPEOPLE = "14"; //要求添加好友的包
    String MESSAGE_RET_ADDPEOPLE = "15"; //返回添加好友的包
    String MESSAGE_GET_HISTORY_ADDPROPLE = "16"; //要求好友请求历史的包
    String MESSAGE_RET_HISTORY_ADDPROPLE = "17"; //返回好友请求历史的包
    String MESSAGE_CHANGE_MY_IMFORMATION = "18"; //
}
