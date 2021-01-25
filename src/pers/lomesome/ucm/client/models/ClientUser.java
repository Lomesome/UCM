package pers.lomesome.ucm.client.models;

import pers.lomesome.ucm.common.User;

/**
 * 功能：登录后台实现
 */

public class ClientUser {

    // 检验用户合法性
    public String checkUser(User u) {
        return new ClientConServer().SendLoginInfoTOServer(u);
    }
}
