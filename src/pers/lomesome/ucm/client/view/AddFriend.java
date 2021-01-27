package pers.lomesome.ucm.client.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pers.lomesome.ucm.client.tools.*;
import pers.lomesome.ucm.client.view.MyUtils.TopTile;
import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.PeopleInformation;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class AddFriend {
    private double xOffset = 0;
    private double yOffset = 0;
    private VBox objectmsgbox = new VBox();
    private VBox msgBox = new VBox(30);
    private Stage stage = new Stage();
    public void mainView(){

        BorderPane borderPane = new BorderPane();
        TopTile topTile = new TopTile();
        Pane topTitleButton = topTile.toptitle(stage);
        topTitleButton.setId("pane-style");
        topTitleButton.setStyle("-fx-background-color: transparent;");
        topTile.btnClose.setOnMouseClicked(event -> {
            ManageAddFriend.delStage();
            stage.close();
        });
        HBox topBox = new HBox();

        topBox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        topBox.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        Label tipLabel = new Label("添加好友");
        StackPane stackPane = new StackPane(tipLabel);
        stackPane.setStyle("-fx-background-color: transparent");
        stackPane.setPrefWidth(300);
        topBox.setPrefHeight(25.0);
        StackPane.setMargin(tipLabel,new Insets(0,0,0,-65));
        HBox.setMargin(topTitleButton, new Insets(0,0,0,20));
        topBox.getChildren().addAll(topTitleButton, stackPane);
        topBox.setStyle("-fx-background-color: #E6E6E6;-fx-background-insets: 2 12 0 12;-fx-background-radius: 10 10 0 0");

        borderPane.setTop(topBox);


        msgBox.setOnMouseClicked(event -> {
            msgBox.requestFocus();
        });

        TextField textField = new TextField();
        VBox.setMargin(textField,new Insets(30,40,10,40));
        textField.setPrefWidth(300);
        textField.setStyle("-fx-background-radius: 20;-fx-background-color: lightgray");
        textField.setPromptText("UCM号");

        textField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER && !"".equals(textField.getText()) ){
                Message message = new Message();
                message.setSender(OwnInformation.getMyinformation().getUserid());
                message.setContent(textField.getText());
                message.setMesType(MessageType.MESSAGE_GET_FINDPEOPLE);
//                 客户端A发送给服务器
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        msgBox.getChildren().addAll(textField,objectmsgbox);
        msgBox.setAlignment(Pos.TOP_CENTER);
        borderPane.setCenter(msgBox);
        VBox.setMargin(objectmsgbox, new Insets(0,20,0,20));
        msgBox.setStyle("-fx-background-insets: 0 12 12 12;-fx-background-radius: 0 0 10px 10px");
        borderPane.setStyle("-fx-background-insets: 12;-fx-background-radius: 10px");

        DropShadow dropshadow = new DropShadow();// 阴影向外
        dropshadow.setRadius(10);// 颜色蔓延的距离
        dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.setOffsetY(3);// 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.setSpread(0.005);// 颜色变淡的程度
        dropshadow.setColor(Color.BLACK);// 设置颜色
        borderPane.setEffect(dropshadow);// 绑定指定窗口控件
        Scene scene = new Scene(borderPane);

        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("/source/windowstyle.css");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    private Pane makeBox(PeopleInformation peopleInformation){
        BorderPane root = new BorderPane();
        root.getStyleClass().add("addfriend-rootpane");
        root.setPrefHeight(60);
        root.setStyle("-fx-background-color:transparent;-fx-background-insets:0 20 0 0;-fx-background-radius: 5");

        root.setOnMouseClicked(event -> {
            for(Object o :objectmsgbox.getChildren()){
                try {
                    BorderPane b = (BorderPane) o;
                    if(b != root)
                        b.setStyle("-fx-background-color:transparent;-fx-background-insets:0 20 0 0;fx-background-radius: 5");
                }catch (Exception e){ }
            }
            root.setStyle("-fx-background-color:#ece4e4;-fx-background-insets:0 20 0 0;-fx-background-radius: 5");
        });

        Label name = new Label(peopleInformation.getNickname());
        name.setStyle("-fx-text-fill: black");
        name.setPadding(new Insets(0,0,0,10));
        Label id = new Label("("+peopleInformation.getUserid()+")");
        id.setTextFill(Color.ROYALBLUE);

        HBox head = new HBox(5);
        ImageView imageView;
        if (peopleInformation.getHead() == null) {
            imageView = new ImageView(this.getClass().getResource("/source/head/" + (Math.abs((peopleInformation.getUserid().hashCode() % 100)) + 1) + ".jpg").toString());
        } else {
            InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(peopleInformation.getHead().getBytes()));
            imageView = new ImageView(new Image(in));
        }
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        Rectangle rectangle = new Rectangle(imageView.prefWidth(-1), imageView.prefHeight(-1));
        rectangle.setArcWidth(50);
        rectangle.setArcHeight(50);
        imageView.setClip(rectangle);
        imageView.setPreserveRatio(true);

        head.setAlignment(Pos.CENTER);
        head.getChildren().addAll(imageView);
        root.setLeft(head);

        StackPane pane = new StackPane();
        if(ManageFriendList.getFriend(peopleInformation.getUserid()) != null){
            pane.getChildren().add(new Label("已添加"));
            pane.setMinWidth(60);
        }else {
            pane.setMaxHeight(20);
            pane.setMinWidth(20);
            ImageView addpeopleButton = new ImageView(this.getClass().getResource("/source/image/addpeople.png").toString());
            pane.getChildren().add(addpeopleButton);
            addpeopleButton.setFitWidth(20);
            addpeopleButton.setFitHeight(20);
            pane.setOnMouseEntered(event ->  addpeopleButton.setImage(new Image(this.getClass().getResource("/source/image/addpeople_.png").toString())));
            pane.setOnMouseExited(event ->  addpeopleButton.setImage(new Image(this.getClass().getResource("/source/image/addpeople.png").toString())));
            pane.setOnMousePressed(event -> addpeopleButton.setImage(new Image(this.getClass().getResource("/source/image/addpeople__.png").toString())));
            pane.setOnMouseReleased(event -> addpeopleButton.setImage(new Image(this.getClass().getResource("/source/image/addpeople_.png").toString())));
        }

        pane.setOnMouseClicked(event -> {
            if(ManageFriendList.getFriend(peopleInformation.getUserid()) != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "已添加该好友", new ButtonType("确定", ButtonBar.ButtonData.YES));
                alert.initOwner(stage);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText("提示");
                alert.showAndWait();
            }else if(!peopleInformation.getUserid().equals(OwnInformation.getMyinformation().getUserid())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "是否添加好友？", new ButtonType("取消", ButtonBar.ButtonData.NO), new ButtonType("确定", ButtonBar.ButtonData.YES));
                alert.initOwner(stage);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText("提示");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    Message message = new Message();
                    message.setSender(OwnInformation.getMyinformation().getUserid());
                    message.setContent("adding");
                    List<PeopleInformation> list = new ArrayList<>();
                    list.add(OwnInformation.getMyinformation());
                    message.setLists(list);
                    message.setGetter(peopleInformation.getUserid());
                    message.setMesType(MessageType.MESSAGE_GET_ADDPEOPLE);
                    ManageMainGUI.getMainGui().setMakeBox3(peopleInformation);
//                 客户端A发送给服务器
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                        oos.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "不能添加自己", new ButtonType("确定", ButtonBar.ButtonData.YES));
                alert.initOwner(stage);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText("提示");
                alert.showAndWait();
            }
        });
        HBox hBox = new HBox(name,id);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPrefWidth(300);
        HBox msg = new HBox(hBox, pane);
        HBox.setMargin(pane,new Insets(0,20,0,0));
        msg.setAlignment(Pos.CENTER_LEFT);
        msg.setStyle("-fx-background-insets: 0 20 0 0");
        root.setCenter(msg);
        BorderPane.setAlignment(imageView, Pos.CENTER);
        BorderPane.setAlignment(name, Pos.CENTER_LEFT);
        return root;
    }

    public void setObjectmsgbox(List<PeopleInformation> peopleInformationList){
        if(!peopleInformationList.isEmpty() ){
            msgBox.heightProperty().addListener((arg0, old_width, new_width)->{
                stage.setHeight(msgBox.getHeight()+100);
            });
            Platform.runLater(()->{
                objectmsgbox.getChildren().clear();
                Label lxr = new Label("联系人");
                VBox.setMargin(lxr, new Insets(0,0,0,20));
                lxr.setStyle("-fx-padding: 0 0 15 0");
                objectmsgbox.getChildren().add(lxr);
                for(PeopleInformation peopleInformation : peopleInformationList){
                    objectmsgbox.getChildren().add(makeBox(peopleInformation));
                    VBox.setMargin(objectmsgbox.getChildren().get(1), new Insets(0,0,0,20));
                }

            });
        }else {
            ImageView imageView = new ImageView(this.getClass().getResource("/source/image/nofind.png").toString());
            imageView.setFitHeight(300);
            imageView.setFitWidth(300);
            Platform.runLater(()-> {
                objectmsgbox.getChildren().clear();
                msgBox.heightProperty().addListener((arg0, old_width, new_width) -> {
                    stage.setHeight(msgBox.getHeight());
                });
                objectmsgbox.getChildren().add(new StackPane(imageView));
            });
        }
    }

    public Stage getStage() {
        return stage;
    }
}
