package pers.lomesome.ucm.client.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pers.lomesome.ucm.client.tools.OSinfo;
import pers.lomesome.ucm.client.view.Landing;
import java.io.File;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        if(OSinfo.isWindows()){
            File dir = new File(System.getProperty("user.home") + "/AppData/Local/UCM");
            if (!dir.exists()) {// 判断目录是否存在
                dir.mkdir();
            }
        }
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Landing(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
