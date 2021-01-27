package pers.lomesome.ucm.client.view;

import java.io.*;
import java.util.Date;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pers.lomesome.ucm.client.tools.*;
import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.PeopleInformation;

public class Emoji {
    Stage primaryStage = new Stage();
    public Emoji(double x, double y, PeopleInformation friend) {
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double absy = (int)screenRectangle.getHeight() / 900.0;
        double runsize = 900.0 / 6.0 * absy;
        double imagesize = runsize / 5.0 ;
        GridPane grid = new GridPane();
        ScrollPane scroll = new ScrollPane();
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // never show a vertical ScrollBar
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ImageView imageView;
        int j = 0;
        int k = 0;
        for (int i = 1;i < 224; i++ ) {
            String num =String.format("%0" + 3 + "d", i);
            k = (i-1)%50;
            Image image = new Image(this.getClass().getResource("/source/emoji/"+num+".png").toString());
            imageView = new ImageView(image);

            Service<Integer> imgsendservice = new Service<Integer>() {
                @Override
                protected Task<Integer> createTask() {
                    return new Task<Integer>() {
                        @Override
                        protected Integer call() throws Exception {
                            Message message = new Message();
                            message.setSender(OwnInformation.getMyinformation().getUserid());
                            message.setGetter(friend.getUserid());
                            message.setContent("@#@"+num+".png");
                            message.setMesType(MessageType.MESSAGE_COMM_IMAGE);
                            message.setSendTime(new Date().toString());
                            ManageMainGUI.getMainGui().showMsg(message, true);
                            // 客户端A发送给服务器
                            try {
                                ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                                oos.writeObject(message);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            return null;
                        }
                    };
                }
            };

            imageView.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        imgsendservice.restart();
                    }
                }
            });

            imageView.setFitHeight(imagesize);
            imageView.setFitWidth(imagesize);
            imageView.setStyle(" -fx-border-style: solid inside;" + "-fx-border-width: 1.5;");
            grid.setMargin(imageView, new Insets(10,10,10,10));
            grid.add(imageView, k, j);
            k++;
            if (k==50) {
                j++;
            }
        }

        Scene scene = new Scene(scroll,runsize + 100, runsize + 100);
        primaryStage.setHeight(runsize + 100);
        primaryStage.setWidth(runsize + 100);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        grid.setAlignment(Pos.CENTER);
        scene.getStylesheets().add("/source/windowstyle.css");
        scroll.setContent(grid);
        primaryStage.setX(x);
        primaryStage.setY(y);
        primaryStage.getTitle();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.onCloseRequestProperty().addListener(observable -> {
            System.out.println("close");
        });
    }

    public Stage getStage() {
        return primaryStage;
    }

    public void clear(Node[] nodes){
        for(Node node:nodes){
            node = null;
        }
        nodes = null;
        System.gc();
    }
}
