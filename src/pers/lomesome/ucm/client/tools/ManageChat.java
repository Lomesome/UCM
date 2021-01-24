package pers.lomesome.ucm.client.tools;

/**
 * 功能：管理与好友的聊天界面
 */

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import java.util.HashMap;

public class ManageChat {

    private static HashMap<String, VBox> friendsChat = new HashMap<>();

    public static VBox getFriendChat(String id) {
        return friendsChat.get(id);
    }

    public static void addFriendChat(String id, VBox friendChat){
        friendsChat.put(id, friendChat);
    }

    public static void delFriendChat(String id){
        friendsChat.remove(id);
    }

    public static ScrollPane getOutPane(String id){
        return (ScrollPane) friendsChat.get(id).getChildren().get(1);
    }
}