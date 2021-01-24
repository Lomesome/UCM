package pers.lomesome.ucm.client.view;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;
import java.net.*;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pers.lomesome.ucm.client.model.ClientUser;
import pers.lomesome.ucm.client.tools.*;
import pers.lomesome.ucm.client.view.MyUtils.MyTextFieldSkin;
import pers.lomesome.ucm.client.view.MyUtils.TopTile;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.User;

public class Landing {
    private double xOffset = 0;
    private double yOffset = 0;
    private int actioni;
    private String re = null;
    private boolean cancelflag = false;
    private boolean actionflag = false;
    double insize;
    double runWidth;
    double runHeight;
    double landbuttonsize;
    public void setUp(){
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        int width = (int)screenRectangle.getWidth();
        int height = (int)screenRectangle.getHeight();
        double absx = width / 1440.0;
        double absy = height / 900.0;
        runWidth = 250 * absx;
        runHeight = runWidth * 4 / 3;
        landbuttonsize = 40 * ((absx + absy) / 2.0);
        if (landbuttonsize > 50) {
            landbuttonsize = 50;
        }
        insize = 20 * ((absx + absy) / 2.0);
        if (insize > 30) {
            insize = 30;
        }
    }

    public Landing(Stage primaryStage) throws Exception {
        setUp();
        AnchorPane anchorPane = new AnchorPane();
        TopTile uptitle = new TopTile(primaryStage);
        Pane toptitle = uptitle.toptitle(primaryStage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        toptitle.setMaxWidth(100);
        toptitle.setStyle("-fx-background-color: transparent;" + "-fx-padding: 10 0 0 10;");
        ImageView imageView = new ImageView(this.getClass().getResource("/source/image/Logo.png").toString());
        imageView.setFitHeight(runWidth * 0.4);
        imageView.setFitWidth(runWidth * 0.4);
        ImageView reimageView = new ImageView(this.getClass().getResource("/source/image/background.png").toString());
        reimageView.setFitHeight(runWidth * 0.05);
        reimageView.setFitWidth(runWidth * 0.05);
        ImageView chooseImageView = new ImageView(this.getClass().getResource("/source/image/choose3.png").toString());
        chooseImageView.setFitHeight(runWidth * 0.05);
        chooseImageView.setFitWidth(runWidth * 0.05);
        Button touxiang = new Button();
        Circle a = new Circle(1);
        touxiang.setGraphic(imageView);
        touxiang.setShape(a);
        touxiang.getStyleClass().add("head");
        GridPane action = new GridPane();
        TextField inputUsername = new TextField();
        PasswordField inputPassword = new PasswordField();
        TextFieldSkin textFieldSkin = new MyTextFieldSkin(inputPassword, '•');
        inputPassword.setSkin(textFieldSkin);
        inputUsername.setMinHeight(runHeight * 0.14);
        inputPassword.setMinHeight(runHeight * 0.14);
        inputUsername.setMaxWidth(runWidth * 0.70);
        inputPassword.setMaxWidth(runWidth * 0.70);
        inputUsername.setAlignment(Pos.CENTER_LEFT);
        inputPassword.setAlignment(Pos.CENTER_LEFT);
        Service<Integer> actionService = new Service<Integer>() {
            int i = 1;

            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        while (actionflag) {
                            Thread.sleep(70);
                            ImageView imv = new ImageView(this.getClass().getResource("/source/image/action/" + i + ".png").toString());
                            imv.setPreserveRatio(true);
                            imv.setFitHeight(runWidth * 0.5);
                            Platform.runLater(() -> {
                                action.getChildren().clear();
                                action.add(imv, 0, 0);
                            });
                            if (i == 15) {
                                i = 1;
                            }
                            i++;
                        }
                        return null;
                    }
                };
            }
        };

        Label inUsername = new Label("");
        Label inPassword = new Label("");
        inUsername.setFont(Font.font(16.5));
        inPassword.setFont(Font.font(16));
        inputPassword.setStyle("-fx-background-color:transparent;" + "-fx-border-width: 0 0 0.5 0;" + "-fx-border-style: solid inside;" + "-fx-border-color: gray;" + "-fx-padding:0 28 0 0;" + "-fx-font-size:" + insize + "px;");
        inUsername.setTextFill(Color.LIGHTGRAY);
        inPassword.setTextFill(Color.LIGHTGRAY);
        inputUsername.setStyle("-fx-background-color:transparent;" + "-fx-border-width: 0 0 0.5 0;" + "-fx-border-style: solid inside;" + "-fx-border-color: gray;" + "-fx-padding:0 0 0 0;" + "-fx-font-size:" + insize + "px;");
        inputUsername.setOnMouseClicked(event -> {
            if (inUsername.getText().equals("输入帐号"))
                inUsername.setText("");
            if (inPassword.getText().equals("") && inputPassword.getText().equals(""))
                inPassword.setText("输入密码");
        });

        ToggleButton tb1 = new ToggleButton();
        Circle b = new Circle(2);
        tb1.setGraphic(reimageView);
        tb1.setShape(b);
        tb1.getStyleClass().add("tb");
        inputPassword.setOnMouseClicked(event -> {
            if (inPassword.getText().equals("输入密码"))
                inPassword.setText("");
            if (inUsername.getText().equals("") && inputUsername.getText().equals(""))
                inUsername.setText("输入帐号");
        });

        anchorPane.setOnMouseClicked(event -> {
            if (inUsername.getText().equals("") && inputUsername.getText().equals(""))
                inUsername.setText("输入帐号");
            if (inPassword.getText().equals("") && inputPassword.getText().equals(""))
                inPassword.setText("输入密码");
        });
        touxiang.setOnMouseClicked(event -> {
            if (inUsername.getText().equals("") && inputUsername.getText().equals(""))
                inUsername.setText("输入帐号");
            if (inPassword.getText().equals("") && inputPassword.getText().equals(""))
                inPassword.setText("输入密码");
        });
        try {
            BufferedReader br = new BufferedReader(new FileReader(".password.txt"));
            inputUsername.setText(br.readLine());
            inputPassword.setText(br.readLine());
            if (br.readLine().equals("true")) {
                tb1.setSelected(true);
                tb1.setGraphic(chooseImageView);
            } else {
                tb1.setSelected(false);
                inUsername.setText("输入帐号");
                inPassword.setText("输入密码");
                br.close();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        Label remeber = new Label("记住密码");
//        remeber.setFont(Font.font(size));
        remeber.setTextFill(Color.GRAY);
        remeber.setOnMouseClicked(event -> tb1.setSelected(!tb1.isSelected()));

        remeber.hoverProperty().addListener(observable -> {
            if (remeber.isHover()) {
                remeber.setTextFill(Color.BLACK);
            } else {
                remeber.setTextFill(Color.GRAY);
            }
        });

        tb1.selectedProperty().addListener((v, e, t) -> {
            if (!t) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(".password.txt"));
                    bw.write("");
                    bw.newLine();
                    bw.write("");
                    bw.newLine();
                    bw.write("flase");
                    bw.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        tb1.selectedProperty().addListener(observable -> {
            if (tb1.isSelected()) {
                tb1.setGraphic(chooseImageView);
            } else {
                tb1.setGraphic(reimageView);
            }
        });

        Label landingButton = new Label("⧁");
        landingButton.setFont(Font.font(landbuttonsize));
        landingButton.setTextFill(Color.GRAY);
        landingButton.hoverProperty().addListener(observable -> {
            if (landingButton.isHover()) {
                if (inputPassword.getText().length() != 0) {
                    landingButton.setTextFill(Color.LIGHTBLUE);
                }
            } else {
                if (inputPassword.getText().length() != 0) {
                    landingButton.setTextFill(Color.BLACK);
                } else {
                    landingButton.setTextFill(Color.GRAY);
                }
            }
        });

        inputPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (inputPassword.getText().length() == 0) {
                landingButton.setStyle("-fx-text-fill: gray;" + "-fx-background-color:transparent;");
            } else {
                landingButton.setStyle("-fx-text-fill: black;" + "-fx-background-color:transparent;");
            }
        });
        Label register = new Label("注册帐号");
        Button cancel = new Button("取消");
        Service<Integer> landingService = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        ClientUser clientUser = new ClientUser();
                        User user = new User();
                        user.setUserId(inputUsername.getText());
                        user.setPasswd(inputPassword.getText());
                        try {
                            if (cancelflag) {
                                re = clientUser.checkUser(user);
                                Thread.sleep(1000);
                            }
                        } catch (Exception e) {
                            cancelflag = false;
                        }
                        if(cancelflag){
                            if (re.equals(MessageType.MESSAGE_SUCCEED)) {
                                Thread.sleep(50);
                                Platform.runLater(() -> {
                                    try {
                                        MainInterface chat = new MainInterface();
                                        chat.mainView();
                                        ManageMainGUI.setMainGui(chat);
                                        ManageAddPeopleMsg.getAddPeopleMsg(inputUsername.getText());
                                        ManageNoReadMsg.getNoReadMsg(inputUsername.getText());
                                        ManageMyFriendsMsg.getMyFriends(inputUsername.getText());
                                        ManageHistoryMsg.getHistoryMsg(inputUsername.getText());
                                        primaryStage.close();
                                    } catch (Exception e) {
                                    }
                                });
                                if (tb1.isSelected()) {
                                    BufferedWriter bw;
                                    try {
                                        bw = new BufferedWriter(new FileWriter(".password.txt"));
                                        bw.write(inputUsername.getText());
                                        bw.newLine();
                                        bw.write(inputPassword.getText());
                                        bw.newLine();
                                        bw.write("true");
                                        bw.close();
                                    } catch (IOException e1) {
                                    }
                                }
                            } else {
                                double nowx;
                                nowx = primaryStage.getX();
                                for (actioni = 0; actioni < 8; actioni++) {
                                    Platform.runLater(() -> {
                                        if (actioni % 2 == 0) {
                                            primaryStage.setX(nowx + 10);
                                        } else {
                                            primaryStage.setX(nowx - 10);
                                        }
                                    });
                                    Thread.sleep(50);
                                }
                                primaryStage.setX(nowx);
                                String s2 = this.getClass().getResource("/source/music/error.wav").toString();
                                URL url = null;
                                try {
                                    url = new URL(s2);
                                } catch (MalformedURLException e) {
                                }
                                AudioClip sound = Applet.newAudioClip(url);
                                sound.play();
                                Platform.runLater(() -> {
                                    inPassword.setText("输入密码");
                                    inputPassword.setText("");
                                    Alert alert = new Alert(AlertType.ERROR);
                                    alert.initStyle(StageStyle.UNDECORATED);
                                    alert.setHeaderText("提示");
                                    if (re.equals(MessageType.MESSAGE_NO_CONNECTED))
                                        alert.setContentText("无网络连接");
                                    else if (re.equals(MessageType.MESSAGE_WRONG_PASSWORD))
                                        alert.setContentText("帐号密码错误");
                                    else if (re.equals(MessageType.MESSAGE_NO_ACCOUNT))
                                        alert.setContentText("该帐号不存在");
                                    cancelflag = false;
                                    actionflag = false;
                                    action.setVisible(false);
                                    register.setVisible(true);
                                    landingButton.setVisible(true);
                                    inPassword.setVisible(true);
                                    inputPassword.setVisible(true);
                                    inUsername.setVisible(true);
                                    inputUsername.setVisible(true);
                                    tb1.setVisible(true);
                                    remeber.setVisible(true);
                                    cancel.setVisible(false);
                                    inUsername.setVisible(true);
                                    inputUsername.setVisible(true);
                                    cancel.setVisible(false);
                                    alert.setX(primaryStage.getX() - 50);
                                    alert.setY(primaryStage.getY() + 30);
                                    alert.showAndWait();
                                });
                            }
                        }
                        return null;
                    }
                };
            }
        };

        inputPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && inputPassword.getText().length() != 0 && inputUsername.getText().length() != 0) {
                actionflag = true;
                action.setVisible(true);
                actionService.restart();
                register.setVisible(false);
                landingButton.setVisible(false);
                inPassword.setVisible(false);
                inputPassword.setVisible(false);
                inUsername.setVisible(false);
                inputUsername.setVisible(false);
                tb1.setVisible(false);
                remeber.setVisible(false);
                cancel.setVisible(true);
                cancelflag = true;
                landingService.restart();
            }
        });

        cancel.getStyleClass().add("cancel");
        cancel.setVisible(false);
        cancel.setPrefSize(runWidth * 0.333, runHeight * 0.1);
//        cancel.setFont(Font.font(size));
        cancel.setOnMouseClicked(event -> {
            cancelflag = false;
            actionflag = false;
            action.setVisible(false);
            register.setVisible(true);
            landingButton.setVisible(true);
            inPassword.setVisible(true);
            inputPassword.setVisible(true);
            inUsername.setVisible(true);
            inputUsername.setVisible(true);
            tb1.setVisible(true);
            remeber.setVisible(true);
            cancel.setVisible(false);
        });

        landingButton.setOnMouseClicked(event -> {
            if (inputPassword.getText().length() != 0 && inputUsername.getText().length() != 0) {
                actionflag = true;
                action.setVisible(true);
                actionService.restart();
                register.setVisible(false);
                landingButton.setVisible(false);
                inPassword.setVisible(false);
                inputPassword.setVisible(false);
                inUsername.setVisible(false);
                inputUsername.setVisible(false);
                tb1.setVisible(false);
                remeber.setVisible(false);
                cancel.setVisible(true);
                cancelflag = true;
                landingService.restart();
            }
        });
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        anchorPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        anchorPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

//        register.setFont(Font.font(size));
        register.setTextFill(Color.GRAY);
        register.setOnMouseClicked(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("http://59.110.125.3"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        register.hoverProperty().addListener(observable -> {
            if (register.isHover()) {
                register.setTextFill(Color.BLACK);
            } else {
                register.setTextFill(Color.GRAY);
            }
        });

        DropShadow dropshadow = new DropShadow();// 阴影向外
        dropshadow.setRadius(10);// 颜色蔓延的距离
        dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.setOffsetY(3);// 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.setSpread(0.005);// 颜色变淡的程度
        dropshadow.setColor(Color.BLACK);// 设置颜色
        anchorPane.setEffect(dropshadow);// 绑定指定窗口控件
        anchorPane.setStyle("-fx-background-radius: 10px;" + "-fx-border-radius: 10px;" + "-fx-background-color: white;" + "-fx-border-width: 0;" + "-fx-background-insets:12;");
        anchorPane.getChildren().addAll(touxiang, action, inUsername, inPassword, inputUsername, inputPassword, register, remeber, landingButton, tb1, toptitle, cancel);
        AnchorPane.setBottomAnchor(inputUsername, runHeight * 0.34);
        AnchorPane.setLeftAnchor(inputUsername, runWidth * 0.2);
        AnchorPane.setRightAnchor(inputUsername, runWidth * 0.2);
        AnchorPane.setBottomAnchor(inputPassword, runHeight * 0.2);
        AnchorPane.setLeftAnchor(inputPassword, runWidth * 0.2);
        AnchorPane.setRightAnchor(inputPassword, runWidth * 0.2);
        AnchorPane.setBottomAnchor(inUsername, runHeight * 0.38);
        AnchorPane.setLeftAnchor(inUsername, runWidth * 0.2);
        AnchorPane.setBottomAnchor(inPassword, runHeight * 0.24);
        AnchorPane.setLeftAnchor(inPassword, runWidth * 0.2);
        AnchorPane.setTopAnchor(action, runHeight * 0.16);
        AnchorPane.setLeftAnchor(action, runWidth * 0.25);
        AnchorPane.setTopAnchor(touxiang, runHeight * 0.2);
        AnchorPane.setLeftAnchor(touxiang, runWidth * 0.3);
        AnchorPane.setRightAnchor(touxiang, runWidth * 0.3);
        AnchorPane.setLeftAnchor(register, runWidth * 0.2);
        AnchorPane.setBottomAnchor(register, runHeight * 0.1);
        AnchorPane.setRightAnchor(remeber, runWidth * 0.2);
        AnchorPane.setBottomAnchor(remeber, runHeight * 0.1);
        AnchorPane.setBottomAnchor(landingButton, runHeight * 0.2);
        AnchorPane.setRightAnchor(landingButton, runWidth * 0.2);
        AnchorPane.setBottomAnchor(tb1, runHeight * 0.105);
        AnchorPane.setRightAnchor(tb1, runWidth * 0.4);
        AnchorPane.setTopAnchor(toptitle, runHeight * 0.05);
        AnchorPane.setLeftAnchor(toptitle, runWidth * 0.05);
        AnchorPane.setRightAnchor(cancel, runWidth * 0.33);
        AnchorPane.setBottomAnchor(cancel, runHeight * 0.25);
        AnchorPane.setLeftAnchor(cancel, runWidth * 0.33);
        Scene scene = new Scene(anchorPane, runWidth, runHeight);
        scene.getStylesheets().add("/source/windowstyle.css");
        scene.setFill(null);
        MySystemTray.getInstance(primaryStage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getTitle();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}