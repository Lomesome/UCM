package pers.lomesome.ucm.client.tools;

/**
 * 功能：保存登录帐户的信息
 */

import pers.lomesome.ucm.common.PeopleInformation;

public class OwnInformation {
    private static PeopleInformation myinformation;

    public static PeopleInformation getMyinformation() {
        return myinformation;
    }

    public static void setMyinformation(PeopleInformation myinformation) {
        OwnInformation.myinformation = myinformation;
    }
}
