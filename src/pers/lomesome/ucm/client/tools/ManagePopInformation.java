package pers.lomesome.ucm.client.tools;

import pers.lomesome.ucm.client.view.PopInformation;

public class ManagePopInformation {

    private static PopInformation popInformation = null;

    public static PopInformation getPopInformation() {
        return popInformation;
    }

    public static void setPopInformation(PopInformation popInformation) {
        ManagePopInformation.popInformation = popInformation;
    }

    public static void delStage(){
        popInformation = null;
    }

}
