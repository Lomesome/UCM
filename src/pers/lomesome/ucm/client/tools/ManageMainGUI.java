package pers.lomesome.ucm.client.tools;

/**
 * 管理程序主界面
 */

import pers.lomesome.ucm.client.view.MainInterface;

public class ManageMainGUI {
    private static MainInterface mainGui;

    public static MainInterface getMainGui() {
        return mainGui;
    }

    public static void setMainGui(MainInterface mainGui) {
        ManageMainGUI.mainGui = mainGui;
    }
}
