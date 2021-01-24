package pers.lomesome.ucm.client.tools;

/**
 * 管理好友所在分组的列表
 */

import pers.lomesome.ucm.common.PeopleInformation;
import java.util.HashMap;

public class ManageGroupList {

    private static HashMap<String, PeopleInformation> grouplist = new HashMap<>();

    public static void setGrouplist(HashMap<String, PeopleInformation> grouplist) {
        ManageGroupList.grouplist = grouplist;
    }

    public static HashMap<String, PeopleInformation> getGrouplist() {
        return grouplist;
    }
}
