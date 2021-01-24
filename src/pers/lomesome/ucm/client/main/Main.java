package pers.lomesome.ucm.client.main;

import javafx.application.Application;
import javafx.stage.Stage;
import pers.lomesome.ucm.client.view.Landing;

public class Main extends Application {

    @Override
    public void init() throws Exception {
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
