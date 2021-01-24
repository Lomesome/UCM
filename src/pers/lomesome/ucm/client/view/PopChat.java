package pers.lomesome.ucm.client.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pers.lomesome.ucm.client.tools.ManageMainGUI;
import pers.lomesome.ucm.client.view.MyUtils.DrawUtil;
import pers.lomesome.ucm.client.view.MyUtils.TopTile;

public class PopChat {

    private double xOffset = 0;
    private double yOffset = 0;
    public PopChat(VBox chat,String nickname){
        chat.setUserData("pop");
        BorderPane borderPane = new BorderPane();
        chat.getChildren().get(0).setVisible(false);
        Stage stage = new Stage();
        TopTile topTile = new TopTile();
        Pane topTitleButton = topTile.toptitle(stage);
        topTitleButton.setId("pane-style");
        topTitleButton.setStyle("-fx-background-color: transparent;");
        topTile.btnClose.setOnMouseClicked(event -> {
            chat.setUserData("nopop");
            chat.getChildren().get(0).setVisible(true);
            ManageMainGUI.getMainGui().listView.getSelectionModel().select(null);
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
        Label nicknameLabel = new Label(nickname);
        StackPane stackPane = new StackPane(nicknameLabel);
        stackPane.setPrefWidth(1000);

        topBox.setPrefHeight(55.0);
        HBox.setMargin(topTitleButton, new Insets(0,0,0,30));
        HBox.setMargin(stackPane, new Insets(0,0,0,-30));
        topBox.getChildren().addAll(topTitleButton, stackPane);
        topBox.setStyle("-fx-background-color: #E6E6E6;-fx-background-insets: 2 12 0 12;-fx-background-radius: 10 10 0 0");

        DropShadow dropshadow = new DropShadow();// 阴影向外
        dropshadow.setRadius(10);// 颜色蔓延的距离
        dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.setOffsetY(3);// 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.setSpread(0.005);// 颜色变淡的程度
        dropshadow.setColor(Color.BLACK);// 设置颜色
        borderPane.setEffect(dropshadow);// 绑定指定窗口控件

        borderPane.setTop(topBox);
        borderPane.setCenter(chat);
        BorderPane.setMargin(chat, new Insets(0,0,0,12));
        borderPane.setStyle("-fx-background-radius: 10px;-fx-border-radius: 10px;-fx-border-width: 0;-fx-background-insets:12;");
        DrawUtil.addDrawFunc(stage, chat); // 添加窗体拉伸效果
        Scene scene = new Scene(borderPane);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("/source/windowstyle.css");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}
