package pers.lomesome.ucm.client.tools;

import pers.lomesome.ucm.client.view.MakeHead;

public class ManageChangeHead {

    private static MakeHead makeHead = null;

    public static MakeHead getMakeHead() {
        return makeHead;
    }

    public static void setMakeHead(MakeHead makeHead) {
        ManageChangeHead.makeHead = makeHead;
    }

    public static void delStage(){
        makeHead = null;
    }

}
