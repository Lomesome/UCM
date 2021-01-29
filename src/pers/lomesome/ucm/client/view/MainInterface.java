package pers.lomesome.ucm.client.view;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pers.lomesome.ucm.client.tools.*;
import pers.lomesome.ucm.client.view.MyUtils.DrawUtil;
import pers.lomesome.ucm.client.view.MyUtils.TopTile;
import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.PeopleInformation;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MainInterface {
    private Map<PeopleInformation, ObservableList> Map = new HashMap<>();
    private Map<PeopleInformation, Pane> addfriendPane = new HashMap<>();
    private Map<String, TitledPane> titledPaneMap = new HashMap<>();
    private double xOffset = 0;
    private double yOffset = 0;
    private int emojiflag = 0;
    private List<PeopleInformation> alluser = new ArrayList<>();
    private ObservableList<PeopleInformation> obList = FXCollections.observableArrayList(alluser);
    private List<PeopleInformation> addfriendrequestlist = new ArrayList<>();
    public ListView<PeopleInformation> listView = new ListView<>(obList); // 依据指定数据创建列表视图
    private BorderPane rootPane = new BorderPane();
    private ImageView messageImage = new ImageView(this.getClass().getResource("/resources/image/messaging_choose.png").toString());
    private ImageView friendsImage = new ImageView(this.getClass().getResource("/resources/image/user_nochoose.png").toString());
    private ImageView applicationImage = new ImageView(this.getClass().getResource("/resources/image/application_nochoose.png").toString());
    private Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
    private final int width = (int) screenRectangle.getWidth();
    private final int height = (int) screenRectangle.getHeight();
    private double runWidth;
    private double runHeight;
    private String url;
    private Emoji emoji;
    private SplitPane chatViewSplitPane;
    private SplitPane friendlistViewSplitPane;
    private StackPane funPane;
    private SplitPane splitPane;
    private HBox friendViewPane;
    private VBox friendRequestBox;
    private ImageView myHeadImageView;
    private TitledPane friendsTitledPane;
    private HBox topBox;
    private Stage primaryStage = new Stage();
    private boolean music = true;
    private Date lasttime = null;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Scene scene;

    public MainInterface() { }

    private void setUp() {
        runWidth = width * 0.7;
        runHeight = runWidth * 0.6;
        PeopleInformation peopleInformation = new PeopleInformation();
        peopleInformation.setUserid("@@##admin");
        obList.add(peopleInformation);
    }

    public void mainView() {
        setUp();
        chatViewSplitPane = getChatView();
        friendlistViewSplitPane = getFriendsView();
        topBox = getTopBox();
        Text noneText = new Text("暂未开发，尽情期待······");
        noneText.setFont(Font.font("Tahoma", 55));
        noneText.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop(0, Color.SKYBLUE), new Stop(0.5f, Color.PURPLE)));
        noneText.setStrokeWidth(1);
        noneText.setStroke(Color.TRANSPARENT);
        funPane = new StackPane(noneText);
        BorderPane.setMargin(funPane, new Insets(0,0,12,12));

        primaryStage.xProperty().addListener((arg,o,n)->{
            if((Double)n + primaryStage.getWidth() < 50){
                primaryStage.setX(50 - primaryStage.getWidth());
            }
        });

        primaryStage.yProperty().addListener((arg,o,n)->{
            if((Double)n < 0){
                primaryStage.setY(0);
            }else if((Double)n > height - 100){
                primaryStage.setY(height - 100);
            }
        });

        DropShadow dropshadow = new DropShadow();// 阴影向外
        dropshadow.setRadius(10);// 颜色蔓延的距离
        dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.setOffsetY(3);// 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.setSpread(0.005);// 颜色变淡的程度
        dropshadow.setColor(Color.BLACK);// 设置颜色
        rootPane.setEffect(dropshadow);// 绑定指定窗口控件
        rootPane.setTop(topBox);
        rootPane.setCenter(chatViewSplitPane);
        splitPane.setStyle("-fx-border-width: 0;-fx-background-insets: 12;");
        rootPane.setStyle("-fx-background-insets: 12;-fx-background-radius: 10px");
        chatViewSplitPane.setStyle("-fx-background-insets: 12;");
        scene = new Scene(rootPane, runWidth, runHeight);
        scene.getStylesheets().add("/resources/windowstyle.css");
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        DrawUtil.addDrawFunc(primaryStage, chatViewSplitPane); // 添加窗体拉伸效果
        DrawUtil.addDrawFunc(primaryStage, friendlistViewSplitPane); // 添加窗体拉伸效果
        DrawUtil.addDrawFunc(primaryStage, funPane); // 添加窗体拉伸效果
        primaryStage.getTitle();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox getTopBox(){
        HBox top = new HBox();
        TopTile topTile = new TopTile();
        Pane topTitleButton = topTile.toptitle(primaryStage);
        topTitleButton.setId("pane-style");
        topTitleButton.setStyle("-fx-background-color: TRANSPARENT;");
        topTile.btnClose.setOnMouseClicked(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "是否退出？", new ButtonType("取消", ButtonBar.ButtonData.NO), new ButtonType("确定", ButtonBar.ButtonData.YES));
                    alert.initOwner(primaryStage);
                    alert.setHeaderText("提示");
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                        System.exit(0);
                    }
                }
        );

        ContextMenu addMusicMenu = new ContextMenu();
        addMusicMenu.setUserData("hide");

        addMusicMenu.focusedProperty().addListener((a, o, n) -> {
            if (!n)
                addMusicMenu.setUserData("hide");
        });

        MenuItem addMusic = new MenuItem("静音");
        addMusic.setUserData("jy");
        addMusic.setOnAction(event -> {
            if(addMusic.getUserData().equals("jy")) {
                addMusic.setText("恢复音效");
                music = false;
                addMusic.setUserData("hf");
            }else {
                addMusic.setText("静音");
                music = true;
                addMusic.setUserData("jy");
            }
        });

        addMusicMenu.getItems().addAll(addMusic);
        StackPane chooseBoxStackPane = new StackPane();
        chooseBoxStackPane.setPrefWidth(runWidth);
        HBox chooseBox = new HBox(35);
        chooseBox.setAlignment(Pos.CENTER);
        HBox messageImagePane = new HBox();
        HBox friendsImagePane = new HBox();
        HBox applicationImagePane = new HBox();
        messageImagePane.setAlignment(Pos.CENTER);
        friendsImagePane.setAlignment(Pos.CENTER);
        applicationImagePane.setAlignment(Pos.CENTER);
        messageImagePane.getChildren().add(messageImage);
        friendsImagePane.getChildren().add(friendsImage);
        applicationImagePane.getChildren().add(applicationImage);
        messageImage.setFitHeight(40);
        messageImage.setFitWidth(40);
        friendsImage.setFitHeight(40);
        friendsImage.setFitWidth(40);
        applicationImage.setFitHeight(40);
        applicationImage.setFitWidth(40);

        messageImagePane.setOnMouseClicked(event -> {
            messageImage.setImage(new Image(this.getClass().getResource("/resources/image/messaging_choose.png").toString()));
            friendsImage.setImage(new Image(this.getClass().getResource("/resources/image/user_nochoose.png").toString()));
            applicationImage.setImage(new Image(this.getClass().getResource("/resources/image/application_nochoose.png").toString()));
            rootPane.setCenter(chatViewSplitPane);
        });

        friendsImagePane.setOnMouseClicked(event -> {
            messageImage.setImage(new Image(this.getClass().getResource("/resources/image/messaging_nochoose.png").toString()));
            friendsImage.setImage(new Image(this.getClass().getResource("/resources/image/user_choose.png").toString()));
            applicationImage.setImage(new Image(this.getClass().getResource("/resources/image/application_nochoose.png").toString()));
            rootPane.setCenter(friendlistViewSplitPane);
        });

        applicationImagePane.setOnMouseClicked(event -> {
            messageImage.setImage(new Image(this.getClass().getResource("/resources/image/messaging_nochoose.png").toString()));
            friendsImage.setImage(new Image(this.getClass().getResource("/resources/image/user_nochoose.png").toString()));
            applicationImage.setImage(new Image(this.getClass().getResource("/resources/image/application_choose.png").toString()));
            rootPane.setCenter(funPane);
        });

        chooseBox.getChildren().addAll(messageImagePane, friendsImagePane, applicationImagePane);
        chooseBoxStackPane.getChildren().add(chooseBox);

        top.setPrefHeight(55.0);
        HBox.setMargin(topTitleButton, new Insets(0, 0, 0, 30));
        HBox.setMargin(chooseBoxStackPane, new Insets(0, 0, 0, -30));


        if (OwnInformation.getMyinformation().getHead() == null) {
            myHeadImageView = new ImageView(this.getClass().getResource("/resources/head/" + (Math.abs((OwnInformation.getMyinformation().getUserid().hashCode() % 100)) + 1) + ".jpg").toString());
        } else if(OwnInformation.getMyinformation().getHead().startsWith("defaulthead:")){
            myHeadImageView = new ImageView(this.getClass().getResource("/resources/head/" + OwnInformation.getMyinformation().getHead().replace("defaulthead:","")).toString());
        }else {
            myHeadImageView = new ImageView(new Image(Base64.getDecoder().wrap(new ByteArrayInputStream(OwnInformation.getMyinformation().getHead().getBytes()))));
        }
        myHeadImageView.setFitHeight(30);
        myHeadImageView.setFitWidth(30);
        Rectangle rectangle = new Rectangle(myHeadImageView.prefWidth(-1), myHeadImageView.prefHeight(-1));
        rectangle.setArcWidth(50);
        rectangle.setArcHeight(50);
        myHeadImageView.setClip(rectangle);
        myHeadImageView.setPreserveRatio(true);

        myHeadImageView.setOnMouseClicked(event -> {
            if (ManagePopInformation.getPopInformation() == null) {
                PopInformation popInformation = new PopInformation(OwnInformation.getMyinformation());
                ManagePopInformation.setPopInformation(popInformation);
            } else {
                ManagePopInformation.getPopInformation().getStage().setAlwaysOnTop(true);
                ManagePopInformation.getPopInformation().getStage().setAlwaysOnTop(false);
            }
        });
        HBox.setMargin(myHeadImageView, new Insets(0, 30, 0, 0));
        top.getChildren().addAll(topTitleButton, chooseBoxStackPane, myHeadImageView);
        top.setStyle("-fx-background-color: #E6E6E6;-fx-background-radius: 10px 10px 0 0;-fx-background-insets: 2 12 0 12");
        top.setAlignment(Pos.CENTER_LEFT);
        top.setOnMouseClicked(event -> {
            if(event.getButton() ==  MouseButton.SECONDARY){
                if (addMusicMenu.getUserData() == "hide") {
                    addMusicMenu.setUserData("show");
                    addMusicMenu.show(top, Side.BOTTOM,event.getScreenX() - primaryStage.getX(),event.getScreenY() - primaryStage.getY() - 55);

                } else {
                    addMusicMenu.setUserData("hide");
                    addMusicMenu.hide();
                }
            }
        });

        top.setOnMousePressed(event -> {
            addMusicMenu.setUserData("hide");
            addMusicMenu.hide();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        top.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        top.setOnMouseReleased(event -> {
            if(primaryStage.getY() <= 0){
                primaryStage.setY(0);
            }else if(primaryStage.getY() >= height - 100){
                primaryStage.setY(height - 100);
            }
            if(primaryStage.getX() + primaryStage.getWidth() < 100){
                primaryStage.setX(100 - primaryStage.getWidth());
            }else if(primaryStage.getX() >= width - 50){
                primaryStage.setX(width - 50);
            }
        });
        return top;
    }

    private SplitPane getChatView() {
        StackPane stackPane = new StackPane();
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.2f, 0.6f);
        HBox listViewStackPane = new HBox();
        HBox.setMargin(listView, new Insets(0, 0, 12, 12));

        listView.setPrefWidth(runWidth);
        listView.getSelectionModel().select(0);
        listView.setStyle("-fx-padding: 0;-fx-background-color: transparent;");

        listView.getSelectionModel().selectedItemProperty().addListener((arg0, old_people, new_people) -> {
            if (new_people == null) {
                StackPane nstackPane = new StackPane();
                ImageView backImage = new ImageView(this.getClass().getResource("/resources/image/back.png").toString());
                backImage.setFitHeight(200);
                backImage.setFitWidth(200);
                nstackPane.getChildren().add(backImage);
                splitPane.getItems().set(1, stackPane);
            } else {
                if (new_people.getUserid().startsWith("@@##")) {

                    ScrollPane scrollPane = new ScrollPane();
                    scrollPane.setPrefWidth(0.8 * runWidth);
                    HBox hBox = new HBox(scrollPane);
                    HBox.setMargin(scrollPane, new Insets(0, 12, 12, 0));
                    scrollPane.setStyle("-fx-background-color: transparent");
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    friendRequestBox = new VBox(20);
                    scrollPane.setContent(friendRequestBox);
                    friendRequestBox.setPadding(new Insets(40, 0, 0, runWidth * 0.2));
                    friendRequestBox.setAlignment(Pos.TOP_CENTER);
                    for (PeopleInformation peopleInformation : addfriendrequestlist) {
                        if (addfriendPane.get(peopleInformation) == null) {
                            Pane pane = makeBox(peopleInformation);
                            friendRequestBox.getChildren().add(pane);
                            addfriendPane.put(peopleInformation, pane);
                        } else {
                            friendRequestBox.getChildren().add(0, addfriendPane.get(peopleInformation));
                        }
                    }
                    splitPane.getItems().set(1, hBox);
                } else {
                    try {
                        VBox chat = ManageChat.getFriendChat(new_people.getUserid());
                        if (chat.getUserData() == "nopop") {
                            splitPane.getItems().set(1, getChatWithFriend(new_people));
                        }
                    } catch (Exception e) {
                        splitPane.getItems().set(1, getChatWithFriend(new_people));
                    }
                }
            }
        });

        listView.setOnMouseClicked(event -> {
            try {
                emoji.getStage().close();
                emojiflag = 0;
            } catch (Exception ignored) {
            }
        });

        listView.setCellFactory(new Callback<ListView<PeopleInformation>, ListCell<PeopleInformation>>() {
                                    public ListCell<PeopleInformation> call(ListView<PeopleInformation> headerListView) {

                                        return new ListCell<PeopleInformation>() {

                                            @Override
                                            protected void updateItem(PeopleInformation item, boolean empty) {
                                                super.updateItem(item, empty);

                                                if (item != null) {
                                                    this.setOnMouseClicked(event -> {
                                                        if (!item.getUserid().startsWith("@@##")) {
                                                            if (event.getClickCount() == 1) {
                                                                try {
                                                                    Map.get(item).set(0, 0);
                                                                }catch (Exception ignored){}
                                                                VBox chat = ManageChat.getFriendChat(item.getUserid());
                                                                if (chat.getUserData() == "pop") {
                                                                    listView.getSelectionModel().select(null);
                                                                }
                                                            }
                                                            if (event.getClickCount() == 2) {
                                                                try {
                                                                    Map.get(item).set(0, 0);
                                                                }catch (Exception ignored){}
                                                                splitPane.getItems().set(1, stackPane);
                                                                VBox chat = ManageChat.getFriendChat(item.getUserid());
                                                                if (chat.getUserData() == "nopop") {
                                                                    new PopChat(chat, item.getNickname());
                                                                }
                                                            }
                                                        }
                                                    });

                                                    BorderPane root = new BorderPane();
                                                    root.setPrefHeight(60);
                                                    root.setStyle("-fx-background-color:transparent;");
                                                    Label name;
                                                    Circle a = new Circle(2);
                                                    ImageView buttonImageView = new ImageView(this.getClass().getResource("/resources/image/close.png").toString());
                                                    buttonImageView.setFitHeight(15);
                                                    buttonImageView.setFitWidth(15);
                                                    HBox head = new HBox(5);
                                                    ImageView imageView;
                                                    if (!item.getUserid().startsWith("@@##")) {
                                                        name = new Label(item.getNickname());
                                                        if (item.getHead() == null) {
                                                            imageView = new ImageView(this.getClass().getResource("/resources/head/" + (Math.abs((item.getUserid().hashCode() % 100)) + 1) + ".jpg").toString());
                                                        } else if(item.getHead().startsWith("defaulthead:")){
                                                            imageView = new ImageView(this.getClass().getResource("/resources/head/" + item.getHead().replace("defaulthead:","")).toString());
                                                        }else {
                                                            InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(item.getHead().getBytes()));
                                                            imageView = new ImageView(new Image(in));
                                                        }
                                                    } else {
                                                        name = new Label("好友验证消息");
                                                        imageView = new ImageView(this.getClass().getResource("/resources/image/Logo.png").toString());
                                                    }

                                                    name.setStyle("-fx-text-fill: black");
                                                    name.setPadding(new Insets(0, 0, 0, 10));
                                                    imageView.setFitHeight(runWidth * 0.04);
                                                    imageView.setFitWidth(runWidth * 0.04);
                                                    Rectangle rectangle = new Rectangle(imageView.prefWidth(-1), imageView.prefHeight(-1));
                                                    rectangle.setArcWidth(50);
                                                    rectangle.setArcHeight(50);
                                                    imageView.setClip(rectangle);
                                                    imageView.setPreserveRatio(true);
                                                    Button close = new Button();
                                                    close.setOnMouseClicked(event -> {
                                                        obList.remove(item);
                                                        listView.getSelectionModel().select(null);
                                                        ManageChat.delFriendChat(item.getUserid());
                                                    });
                                                    HBox.setMargin(close, new Insets(0, 0, 0, -5));
                                                    close.getStyleClass().add("hclose");
                                                    close.setGraphic(buttonImageView);
                                                    close.setShape(a);
                                                    close.setVisible(false);
                                                    head.setAlignment(Pos.CENTER);
                                                    head.getChildren().addAll(close, imageView);
                                                    root.setLeft(head);
                                                    BorderPane.setAlignment(imageView, Pos.CENTER);
                                                    BorderPane.setAlignment(name, Pos.CENTER_LEFT);
                                                    root.setCenter(name);
                                                    root.setOnMouseEntered(event -> close.setVisible(true));
                                                    root.setOnMouseExited(event -> close.setVisible(false));

                                                    if (Map.get(item) != null) {
                                                        if ((Integer) Map.get(item).get(0) != 0) {
                                                            Text tip = new Text();
                                                            if ((Integer) Map.get(item).get(0) < 100) {
                                                                tip.setText(String.valueOf(Map.get(item).get(0)));
                                                            } else {
                                                                tip.setText("99");
                                                            }
                                                            StackPane msg = new StackPane();
                                                            tip.setFill(javafx.scene.paint.Color.WHITE);
                                                            Circle back = new Circle(runHeight * 0.017);
                                                            back.setFill(javafx.scene.paint.Color.RED);
                                                            msg.getChildren().addAll(back, tip);
                                                            root.setRight(msg);
                                                            Map.get(item).addListener((ListChangeListener) (o) -> {
                                                                if ((Integer) o.getList().get(0) == 0) {
                                                                    msg.setVisible(false);
                                                                } else if ((Integer) o.getList().get(0) < 100) {
                                                                    msg.setVisible(true);
                                                                    tip.setText(String.valueOf(o.getList().get(0)));
                                                                } else {
                                                                    msg.setVisible(true);
                                                                    tip.setText("99");
                                                                }
                                                            });
                                                        }
                                                    }
                                                    setGraphic(root);
                                                } else {
                                                    setGraphic(null);
                                                }
                                            }
                                        };
                                    }
                                }
        );

        listViewStackPane.setStyle("-fx-background-color: white;-fx-background-insets: 0 0 12 12");
        listViewStackPane.setMaxWidth(runWidth * 0.25); // 设置列表视图的推荐宽高
        listViewStackPane.setMinWidth(runWidth * 0.2); // 设置列表视图的推荐宽高
        splitPane.setStyle("-fx-background-color:white;");
        listViewStackPane.getChildren().add(listView);
        ImageView backImage = new ImageView(this.getClass().getResource("/resources/image/back.png").toString());
        backImage.setFitHeight(200);
        backImage.setFitWidth(200);
        stackPane.getChildren().add(backImage);
        splitPane.getItems().addAll(listViewStackPane, stackPane);

        stackPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        stackPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        return splitPane;
    }

    private Pane makeBox(PeopleInformation peopleInformation) {
        BorderPane root = new BorderPane();
        root.setMaxWidth(450);
        root.getStyleClass().add("addfriend-rootpane");
        root.setPrefHeight(100);
        root.setStyle("-fx-background-color:transparent;-fx-padding: 0 10 0 10;-fx-background-radius: 5;-fx-border-radius: 5;-fx-border-width: 1;-fx-border-color: lightgray");

        Label name = new Label(peopleInformation.getNickname());
        name.setStyle("-fx-text-fill: black");
        name.setPadding(new Insets(0, 0, 0, 10));
        Label id = new Label("(" + peopleInformation.getUserid().replace("@@##", "") + ")");
        id.setTextFill(Color.ROYALBLUE);
        HBox head = new HBox(5);
        ImageView imageView;
        if (peopleInformation.getHead() == null) {
            imageView = new ImageView(this.getClass().getResource("/resources/head/" + (Math.abs((peopleInformation.getUserid().replace("@@##", "").hashCode() % 100)) + 1) + ".jpg").toString());
        } else if(peopleInformation.getHead().startsWith("defaulthead:")){
            imageView = new ImageView(this.getClass().getResource("/resources/head/" + peopleInformation.getHead().replace("defaulthead:","")).toString());
        }else {
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
        HBox hBox = new HBox(name, id);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPrefWidth(400);
        HBox msg = new HBox(hBox);

        if (ManageFriendList.getFriendList().get(peopleInformation.getUserid()) != null) {
            Label label = new Label("已同意");
            label.setMinWidth(60);
            msg.getChildren().add(label);
        } else {
            Button sure = new Button("同意");
            Button refuse = new Button("拒绝");
            sure.setMinWidth(60);
            refuse.setMinWidth(60);
            sure.setOnMouseClicked(event -> {
                List<PeopleInformation> list = new ArrayList<>();
                list.add(peopleInformation);
                peopleInformation.setUserid(peopleInformation.getUserid().replace("@@##", ""));
                setAllfriends(list);
                ManageFriendList.addFriends(list);
                Message message = new Message();
                message.setSender(OwnInformation.getMyinformation().getUserid());
                message.setContent("added");
                list = new ArrayList<>();
                list.add(OwnInformation.getMyinformation());
                message.setLists(list);
                message.setGetter(peopleInformation.getUserid().replace("@@##", ""));
                message.setMesType(MessageType.MESSAGE_GET_ADDPEOPLE);
//                 客户端A发送给服务器
                msg.getChildren().remove(refuse);
                msg.getChildren().remove(sure);
                Label label = new Label("已同意");
                label.setMinWidth(60);
                msg.getChildren().add(label);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                list = null;
            });

            refuse.setOnMouseClicked(event -> {
                Message message = new Message();
                message.setSender(OwnInformation.getMyinformation().getUserid());
                message.setContent("refuse");
                List<PeopleInformation> list = new ArrayList<>();
                list.add(OwnInformation.getMyinformation());
                message.setLists(list);
                message.setGetter(peopleInformation.getUserid().replace("@@##", ""));
                message.setMesType(MessageType.MESSAGE_GET_ADDPEOPLE);
//                 客户端A发送给服务器
                msg.getChildren().remove(refuse);
                msg.getChildren().remove(sure);
                Label label = new Label("已拒绝");
                label.setMinWidth(60);
                msg.getChildren().add(label);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                list = null;
            });
            msg.getChildren().addAll(sure, refuse);
        }

        msg.setAlignment(Pos.CENTER_LEFT);
        root.setCenter(msg);
        BorderPane.setAlignment(imageView, Pos.CENTER);
        BorderPane.setAlignment(name, Pos.CENTER_LEFT);
        return root;
    }

    private VBox getChatWithFriend(PeopleInformation friend) {
        if (ManageChat.getFriendChat(friend.getUserid()) != null) {
            return ManageChat.getFriendChat(friend.getUserid());
        }
        VBox chatBox = new VBox();
        Label chatLabel = new Label();
        ScrollPane outchatpane = new ScrollPane();
        VBox.setMargin(outchatpane, new Insets(0, 12, 0, 0));
        VBox outchat = new VBox();
        TextArea inchat = new TextArea();
        chatBox.setStyle("-fx-background-color: white;-fx-background-insets: 0 12 12 0");
        chatLabel.setText(friend.getNickname());
        chatLabel.setPadding(new Insets(10, 0, 10, 20));
        chatLabel.setFont(Font.font(18));
        outchat.setPadding(new Insets(10, 0, 0, 0));
        outchatpane.setContent(outchat);
        outchatpane.setOnMouseClicked(event -> {
            try {
                emoji.getStage().close();
                emojiflag = 0;
            } catch (Exception ignored) {
            }
        });
        outchatpane.setPrefHeight(450);
        outchatpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // never show a vertical ScrollBar
        outchatpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        outchatpane.setFitToWidth(true); // set content width to viewport width
        outchatpane.setPannable(true);
        outchat.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> outchatpane.setVvalue(1));
        outchatpane.setStyle("-fx-background-color: transparent;");

        inchat.setWrapText(true);
        inchat.setFont(Font.font(16));
        inchat.setPrefSize(runWidth, runHeight * 0.23);
        inchat.setStyle("-fx-background-color: transparent;");
        inchat.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                Message message = new Message();
                message.setSender(OwnInformation.getMyinformation().getUserid());
                message.setGetter(friend.getUserid());
                message.setContent(inchat.getText());
                message.setMesType(MessageType.MESSAGE_COMM);
                message.setSendTime(df.format(new Date()));
                showMsg(message, true);
//                 客户端A发送给服务器
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                inchat.setText("");
            }
        });
        inchat.getStyleClass().add("in");
        inchat.textProperty().addListener((observable, oldValue, newValue) -> {
            if (inchat.getText().equals("\n")) {
                inchat.setText("");
            }
        });
        inchat.setOnMouseClicked(event -> {
            try {
                emoji.getStage().close();
                emojiflag = 0;
            } catch (Exception ignored) {
            }
        });

        Service<Integer> imgsendservice = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        String base64img;
                        base64img = PicUtils.imageToBase64(url,200);
                        Message message = new Message();
                        message.setSender(OwnInformation.getMyinformation().getUserid());
                        message.setGetter(friend.getUserid());
                        message.setContent(base64img);
                        message.setMesType(MessageType.MESSAGE_COMM_IMAGE);
                        message.setSendTime(df.format(new Date()));
                        showMsg(message, true);
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

        VBox in = new VBox();
        in.setOnDragOver(event -> {
            if (event.getGestureSource() != in && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        in.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                url = db.getFiles().toString().split("\\[")[1].split("\\]")[0];
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "是否发送照片？", new ButtonType("取消", ButtonBar.ButtonData.NO), new ButtonType("确定", ButtonBar.ButtonData.YES));
                alert.initOwner(primaryStage);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText("提示");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    imgsendservice.restart();
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        HBox setBar = new HBox(10);
        Button sendimg = new Button();
        sendimg.setOnMouseClicked(event -> {
            Bounds bounds = sendimg.getBoundsInLocal();
            Bounds screenBounds = sendimg.localToScreen(bounds);
            if (emojiflag == 0) {
                emoji = new Emoji(screenBounds.getMinX() - (900.0 / 6.0 * (int)screenRectangle.getHeight() / 900.0 + 100) / 2, screenBounds.getMinY() - (900.0 / 6.0 * (int)screenRectangle.getHeight() / 900.0 + 105), friend);
                emoji.getStage().focusedProperty().addListener((arg, o, n) -> {
                    if (!n) {
                        emoji.getStage().close();
                        emojiflag = 0;
                    }
                });
                emojiflag = 1;
            } else {
                emoji.getStage().close();
            }
        });

        MyRecord mr = new MyRecord();
        Service<Integer> stopvoice = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        Thread.sleep(2000);
                        mr.stop();
                        mr.play();
                        return null;
                    }
                };
            }
        };

        final boolean[] voiceflag = {false};
        Button sendvoice = new Button("");
        sendvoice.getStyleClass().add("voice");
        Label voicelabel = new Label("按住空格键(Space)说话，Esc退出");
        voicelabel.setStyle("-fx-border-color: #cbc7c7;-fx-border-width: 2;-fx-border-radius: 30;-fx-padding: 12;-fx-text-fill: #837f7f");
        StackPane stackPane = new StackPane(voicelabel);
        stackPane.setPrefSize(runWidth, runHeight * 0.23);
        ImageView speak = new ImageView(this.getClass().getResource("/resources/image/voices_show.png").toString());
        sendvoice.setOnMouseClicked(event -> {
            voiceflag[0] = true;
            in.getChildren().set(1, stackPane);
            VBox.setMargin(stackPane, new Insets(0, 12, 12, 0));
        });

        final boolean[] once = {true};
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if(once[0]) {
                if (key.getCode() == KeyCode.SPACE) {
                    if (voiceflag[0]) {
                        mr.capture();
                        once[0] = false;
                        stackPane.getChildren().set(0, speak);
                    }
                }
                if (key.getCode() == KeyCode.ESCAPE) {
                    in.getChildren().set(1, inchat);
                    voiceflag[0] = false;
                }
            }
        });

        Service<Integer> voicesendservice = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<Integer>() {
                    @Override
                    protected Integer call() {
                        String base64voice;
                        base64voice = mr.toBase64Voice();
                        Message message = new Message();
                        message.setSender(OwnInformation.getMyinformation().getUserid());
                        message.setGetter(friend.getUserid());
                        message.setContent(base64voice);
                        message.setMesType(MessageType.MESSAGE_COMM_VOICE);
                        message.setSendTime(df.format(new Date()));
                        showMsg(message, true);
//                         客户端A发送给服务器
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

        scene.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            once[0] = true;
            if(key.getCode() == KeyCode.SPACE){
                if(voiceflag[0]) {
                    mr.stop();
                    stackPane.getChildren().set(0, voicelabel);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "是否发送语音？", new ButtonType("取消", ButtonBar.ButtonData.NO), new ButtonType("确定", ButtonBar.ButtonData.YES));
                    alert.initOwner(primaryStage);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setHeaderText("提示");
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                        voicesendservice.restart();
                    }
                }
            }
        });

        ImageView voiceImageView = new ImageView(this.getClass().getResource("/resources/image/background2.png").toString()); //更改按钮图像
        sendvoice.setGraphic(voiceImageView);

        HBox.setMargin(sendimg, new Insets(3, 0, 3, 10));
        HBox.setMargin(sendvoice, new Insets(3, 0, 3, 10));
        sendimg.getStyleClass().add("img");
        Circle a1 = new Circle(2);
        ImageView imageView1 = new ImageView(this.getClass().getResource("/resources/image/background2.png").toString()); //更改按钮图像
        sendimg.setGraphic(imageView1);
        sendimg.setShape(a1);
        Circle a2 = new Circle(2);
        sendvoice.setShape(a2);

        setBar.getChildren().addAll(sendimg, sendvoice);
        in.getChildren().addAll(setBar, inchat);
        VBox.setMargin(inchat, new Insets(0, 12, 12, 0));
        in.setStyle(" -fx-background-color: white;-fx-border-width: 0.2 0 0 0;-fx-border-style: solid inside;");
        VBox.setMargin(in, new Insets(0, 12, 12, 0));
        chatBox.getChildren().addAll(chatLabel, outchatpane, in);
        ManageChat.addFriendChat(friend.getUserid(), chatBox);
        chatBox.setUserData("nopop");
        for (Message message : ManageHistoryMsg.getHistoryMsgWithO(friend.getUserid())) {
            showMsg(message, false);
        }
        return chatBox;
    }

    private SplitPane getFriendsView() {
        splitPane = new SplitPane();
        splitPane.setDividerPositions(0.2f, 0.6f);

        friendViewPane = new HBox();
        friendViewPane.setStyle("-fx-background-color: white;");
        friendViewPane.setMaxWidth(runWidth * 0.25); // 设置列表视图的推荐宽高
        friendViewPane.setMinWidth(runWidth * 0.2); // 设置列表视图的推荐宽高

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);

        Button addfriends = new Button("加好友");

        ContextMenu addfriendsMenu = new ContextMenu();
        addfriendsMenu.setUserData("hide");
        addfriends.setOnMouseClicked(event -> {
            if (addfriendsMenu.getUserData() == "hide") {
                addfriendsMenu.setUserData("show");
                addfriendsMenu.show(addfriends, Side.BOTTOM, 0, 0);
            } else {
                addfriendsMenu.setUserData("hide");
                addfriendsMenu.hide();
            }
        });

        addfriendsMenu.focusedProperty().addListener((a, o, n) -> {
            if (!n)
                addfriendsMenu.setUserData("hide");
        });

        MenuItem addpeople = new MenuItem("添加联系人");
        addfriendsMenu.getItems().addAll(addpeople);

        addpeople.setOnAction(event -> {
            if (ManageAddFriend.getAddfriend() == null) {
                AddFriend addFriend = new AddFriend();
                addFriend.mainView();
                ManageAddFriend.setAddfriend(addFriend);
            } else {
                ManageAddFriend.getAddfriend().getStage().setAlwaysOnTop(true);
                ManageAddFriend.getAddfriend().getStage().setAlwaysOnTop(false);
            }
        });

        addfriends.getStyleClass().add("addfriends");
        addfriends.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Rectangle rect1 = new Rectangle(20, 20);
        rect1.setFill(Color.TRANSPARENT);
        Rectangle rect2 = new Rectangle(20, 20);
        rect2.setFill(Color.TRANSPARENT);
        HBox hBox = new HBox(10, rect1, addfriends, rect2);

        hBox.setPadding(new Insets(20, 0, 20, 0));
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(addfriends, Priority.ALWAYS);

        addfriends.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(runHeight);
        vBox.getChildren().addAll(hBox, scrollPane);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // never show a vertical ScrollBar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox inScroll = new VBox();
        scrollPane.setContent(inScroll);

        Label groupLabel = new Label("群聊");
        HBox groupLabelBox = new HBox(groupLabel);
        groupLabel.setStyle("-fx-text-fill: gray;-fx-padding: 10 0 10 10");

        Label friendlabel = new Label("好友");
        HBox friendLabelBox = new HBox(friendlabel);
        friendlabel.setStyle("-fx-text-fill: gray;-fx-padding: 10 0 10 10");

        titledPaneMap.put("群组", new CreateTiTlepane().kindFriend("群组", ManageGroupList.getGrouplist().values()));
        friendsTitledPane = new CreateTiTlepane().kindFriend("好友", ManageFriendList.getFriendList().values());
        titledPaneMap.put("好友", friendsTitledPane);

        inScroll.getChildren().addAll(groupLabelBox, titledPaneMap.get("群组"), friendLabelBox, titledPaneMap.get("好友"));

        vBox.setMinWidth(runWidth * 0.2);
        vBox.setMaxWidth(runWidth * 0.25); // 设置列表视图的推荐宽高
        splitPane.setStyle("-fx-background-color:white;");
        friendViewPane.setStyle("-fx-background-insets: 0 0 12 12");
        HBox.setMargin(vBox, new Insets(0, 0, 12, 12));
        friendViewPane.getChildren().add(vBox);
        StackPane stackPane = new StackPane();
        ImageView backImage = new ImageView(this.getClass().getResource("/resources/image/back.png").toString());
        backImage.setFitHeight(200);
        backImage.setFitWidth(200);
        stackPane.getChildren().add(backImage);
        splitPane.getItems().addAll(friendViewPane, stackPane);
        stackPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        stackPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        return splitPane;
    }

    class CreateTiTlepane {
        VBox vBox = new VBox();

        public CreateTiTlepane() { }

        public TitledPane kindFriend(String name, Iterable<PeopleInformation> list) {

            for (PeopleInformation peopleInformation : list) {
                vBox.getChildren().add(friendInformation(peopleInformation, vBox));
            }
            vBox.setStyle("-fx-background-color: white;-fx-padding: 0");

            TitledPane titledPane = new TitledPane(name, vBox);

            titledPane.setMinWidth(0.15 * runWidth);
            titledPane.setPrefWidth(0.25 * runWidth);
            titledPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            titledPane.setAnimated(false);
            titledPane.setExpanded(false);
            return titledPane;
        }

        public VBox getvBox() {
            return vBox;
        }
    }

    private AnchorPane friendInformation(PeopleInformation friend, VBox vBox) {
        ImageView friendHead;
        if (friend.getHead() == null) {
            friendHead = new ImageView(this.getClass().getResource("/resources/head/" + (Math.abs((friend.getUserid().hashCode() % 100)) + 1) + ".jpg").toString());
        } else if(friend.getHead().startsWith("defaulthead:")){
            friendHead = new ImageView(this.getClass().getResource("/resources/head/" + friend.getHead().replace("defaulthead:","")).toString());
        }else {
            InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(friend.getHead().getBytes()));
            friendHead = new ImageView(new Image(in));
        }
        friendHead.setFitHeight(40);
        friendHead.setFitWidth(40);
        Rectangle rectangle = new Rectangle(friendHead.prefWidth(-1), friendHead.prefHeight(-1));
        rectangle.setArcWidth(40);
        rectangle.setArcHeight(40);
        friendHead.setClip(rectangle);
        friendHead.setPreserveRatio(true);
        AnchorPane sonBox = new AnchorPane();
        sonBox.setPrefWidth(450);
        String showname;
        if (friend.getNote() == null) {
            showname = friend.getNickname();
        } else {
            showname = friend.getNote();
        }
        Label titleLabel = new Label(showname);
        titleLabel.setUserData(friend.getUserid());
        titleLabel.setTextFill(Color.BLACK);
        Label explainLabel = new Label(friend.getSignature());
        explainLabel.setTextFill(new Color(0, 0, 0, 0.8));
        explainLabel.setWrapText(true);
        explainLabel.setMaxWidth(250);
        sonBox.getChildren().addAll(friendHead, titleLabel, explainLabel);
        AnchorPane.setTopAnchor(friendHead, 10.0);
        AnchorPane.setBottomAnchor(friendHead, 10.0);
        AnchorPane.setLeftAnchor(friendHead, 12.0);
        AnchorPane.setTopAnchor(titleLabel, 12.0);
        AnchorPane.setLeftAnchor(titleLabel, 80.0);
        AnchorPane.setTopAnchor(explainLabel, 30.0);
        AnchorPane.setLeftAnchor(explainLabel, 80.0);
        sonBox.setStyle("-fx-background-color: white");
        sonBox.setOnMouseClicked(event -> {
            for (Node node : vBox.getChildren()) {
                node.setStyle("-fx-background-color: white");
            }
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                splitPane.getItems().set(1, getFriendMsg(friend.getUserid()));
                sonBox.setStyle("-fx-background-color: WhiteSmoke");
            }
        });
        return sonBox;
    }

    private Pane getFriendMsg(String friendid) {
        PeopleInformation friend = ManageFriendList.getFriend(friendid);
        BorderPane rootFriendInfo = new BorderPane();

        VBox head_name_msg = new VBox(20);
        BorderPane.setMargin(head_name_msg, new Insets(0, 12, 0, 0));
        head_name_msg.setAlignment(Pos.CENTER);
        head_name_msg.setPadding(new Insets(30, 0, 30, 0));
        ImageView friendHead;
        if (friend.getHead() == null) {
            friendHead = new ImageView(this.getClass().getResource("/resources/head/" + (Math.abs((friend.getUserid().hashCode() % 100)) + 1) + ".jpg").toString());
        } else if(friend.getHead().startsWith("defaulthead:")){
            friendHead = new ImageView(this.getClass().getResource("/resources/head/" + friend.getHead().replace("defaulthead:","")).toString());
        }else {
            InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(friend.getHead().getBytes()));
            friendHead = new ImageView(new Image(in));
        }
        friendHead.setFitHeight(80);
        friendHead.setFitWidth(80);
        Rectangle rectangle = new Rectangle(friendHead.prefWidth(-1), friendHead.prefHeight(-1));
        rectangle.setArcWidth(80);
        rectangle.setArcHeight(80);
        friendHead.setClip(rectangle);
        friendHead.setPreserveRatio(true);
        Label friendname = new Label(friend.getNickname());
        friendname.setFont(Font.font(20));
        StackPane imagePane = new StackPane();
        imagePane.setOnMouseClicked(event -> {
            obList.remove(friend);
            obList.add(0, friend);
            rootPane.setCenter(chatViewSplitPane);
            listView.getSelectionModel().select(friend);
            messageImage.setImage(new Image(this.getClass().getResource("/resources/image/messaging_choose.png").toString()));
            friendsImage.setImage(new Image(this.getClass().getResource("/resources/image/user_nochoose.png").toString()));
            applicationImage.setImage(new Image(this.getClass().getResource("/resources/image/application_nochoose.png").toString()));
        });

        ImageView chatImage = new ImageView(this.getClass().getResource("/resources/image/chat.png").toString());
        imagePane.getChildren().add(chatImage);
        imagePane.setMaxWidth(chatImage.getFitWidth());
        imagePane.setOnMouseEntered(event -> {
            ImageView newchatImage = new ImageView(this.getClass().getResource("/resources/image/chathover.png").toString());
            newchatImage.setFitWidth(40);
            newchatImage.setFitHeight(40);
            imagePane.getChildren().set(0, newchatImage);
        });
        imagePane.setOnMouseExited(event -> {
            ImageView newchatImage = new ImageView(this.getClass().getResource("/resources/image/chat.png").toString());
            newchatImage.setFitWidth(40);
            newchatImage.setFitHeight(40);
            imagePane.getChildren().set(0, newchatImage);
        });
        chatImage.setFitWidth(40);
        chatImage.setFitHeight(40);
        head_name_msg.getChildren().addAll(friendHead, friendname, imagePane);

        head_name_msg.setStyle("-fx-background-image: url('resources/image/back"+ (Math.abs((friend.getUserid().hashCode() % 1)) + 1) +".png');-fx-background-size: 100%;-fx-background-repeat: no-repeat;");

        StackPane mid = new StackPane();
        GridPane friendAttr = new GridPane();

        friendAttr.setMaxWidth(300);
        friendAttr.setAlignment(Pos.TOP_CENTER);
        VBox sex = new VBox(10, new Label("性别"));
        sex.setAlignment(Pos.BOTTOM_CENTER);
        Text sextext = new Text();

        if (friend.getSex() == null) {
            sextext.setText("?");
        } else if (friend.getSex().equals("男")) {
            sextext.setText("♂");
        } else if (friend.getSex().equals("女")) {
            sextext.setText("♀");
        }
        sextext.setFont(Font.font("Tahoma", 32));
        sextext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop(0, Color.AQUA), new Stop(0.5f, Color.RED)));
        sextext.setStrokeWidth(1);
        sextext.setStroke(Color.TRANSPARENT);
        sex.getChildren().add(0, sextext);

        VBox age = new VBox(10, new Label("年龄"));
        age.setAlignment(Pos.BOTTOM_CENTER);
        Text agetext = new Text();
        agetext.setFont(Font.font("Tahoma", 32));
        if (friend.getAge() == null) {
            agetext.setText("?");
        } else {
            agetext.setText(String.valueOf(friend.getAge()));
        }
        agetext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop(0, Color.ROYALBLUE), new Stop(0.5f, Color.LIGHTBLUE)));
        agetext.setStrokeWidth(1);
        agetext.setStroke(Color.TRANSPARENT);
        age.getChildren().add(0, agetext);

        VBox zodiac = new VBox(10, new Label("生肖"));
        zodiac.setAlignment(Pos.BOTTOM_CENTER);
        Text zodiactext = new Text();
        zodiactext.setFont(Font.font("Tahoma", 28));
        if (friend.getBirthday() == null) {
            zodiactext.setText("?");
            zodiactext.setFont(Font.font("Tahoma", 32));
        } else {
            zodiactext.setText(Year.getYear(Integer.parseInt(friend.getBirthday().split("-")[0])));
        }
        zodiactext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop(0, Color.AQUA), new Stop(0.5f, Color.DEEPPINK)));
        zodiactext.setStrokeWidth(1);
        zodiactext.setStroke(Color.TRANSPARENT);
        zodiac.getChildren().add(0, zodiactext);

        VBox constellation = new VBox(10, new Label("星座"));
        constellation.setAlignment(Pos.BOTTOM_CENTER);
        Text constellationtext = new Text();
        constellationtext.setFont(Font.font("Tahoma", 25));
        if (friend.getBirthday() == null) {
            constellationtext.setText("?");
            constellationtext.setFont(Font.font("Tahoma", 32));
        } else {
            constellationtext.setText(Year.getConstellation(Integer.parseInt(friend.getBirthday().split("-")[1]), Integer.parseInt(friend.getBirthday().split("-")[2])).replace("座", ""));
        }

        constellationtext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop(0, Color.AQUA), new Stop(0.5f, Color.DARKGREEN)));
        constellationtext.setStrokeWidth(1);
        constellationtext.setStroke(Color.TRANSPARENT);
        constellation.getChildren().add(0, constellationtext);

        VBox birthday = new VBox(10, new Label("生日"));
        birthday.setAlignment(Pos.BOTTOM_CENTER);
        Text birthdaytext = new Text();
        birthdaytext.setFont(Font.font("Tahoma", 25));
        if (friend.getBirthday() == null) {
            birthdaytext.setText("?");
            birthdaytext.setFont(Font.font("Tahoma", 32));
        } else {
            birthdaytext.setText(friend.getBirthday().split("-", 2)[1].replace("-", "/"));
        }

        birthdaytext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop(0, Color.INDIANRED), new Stop(0.5f, Color.ORANGE)));
        birthdaytext.setStrokeWidth(1);
        birthdaytext.setStroke(Color.TRANSPARENT);
        birthday.getChildren().add(0, birthdaytext);

        VBox blood = new VBox(10, new Label("血型"));
        blood.setAlignment(Pos.BOTTOM_CENTER);
        Text bloodtext = new Text("?");
        bloodtext.setFont(Font.font("Tahoma", 32));
        bloodtext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop(0, Color.YELLOWGREEN), new Stop(0.5f, Color.ORANGE)));
        bloodtext.setStrokeWidth(1);
        bloodtext.setStroke(Color.TRANSPARENT);
        blood.getChildren().add(0, bloodtext);

        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(20, 0, 0, 0));
        HBox ucmBox = new HBox(10);
        HBox nicknameBox = new HBox(10);
        HBox noyenameBox = new HBox(10);

        Label unmLabel = new Label("UCM");
        Label nicknameLabel = new Label("昵称");
        Label notenameLabel = new Label("备注");
        unmLabel.setMinWidth(30);
        nicknameLabel.setMinWidth(30);
        notenameLabel.setMinWidth(30);

        Text ucm = new Text(friend.getUserid());
        Text nickname = new Text(friend.getNickname());
        Text notename = new Text(friend.getNote());

        unmLabel.setTextFill(javafx.scene.paint.Paint.valueOf("#6E6E6E"));
        nicknameLabel.setTextFill(Paint.valueOf("#6E6E6E"));
        notenameLabel.setTextFill(Paint.valueOf("#6E6E6E"));

        ucmBox.getChildren().addAll(unmLabel, ucm);
        nicknameBox.getChildren().addAll(nicknameLabel, nickname);
        noyenameBox.getChildren().addAll(notenameLabel, notename);

        bottom.getChildren().addAll(ucmBox, nicknameBox, noyenameBox);
        constellation.setStyle("-fx-border-style: solid inside;-fx-border-width: 0 0 1 0;-fx-border-color: lightgray");
        birthday.setStyle("-fx-border-style: solid inside;-fx-border-width: 0 0 1 0;-fx-border-color: lightgray");
        blood.setStyle("-fx-border-style: solid inside;-fx-border-width: 0 0 1 0;-fx-border-color: lightgray");
        Insets insets = new Insets(20, 20, 20, 20);
        sex.setPadding(insets);
        age.setPadding(insets);
        zodiac.setPadding(insets);
        constellation.setPadding(insets);
        birthday.setPadding(insets);
        blood.setPadding(insets);
        mid.getChildren().add(friendAttr);
        friendAttr.add(sex, 0, 0);
        friendAttr.add(age, 1, 0);
        friendAttr.add(zodiac, 2, 0);
        friendAttr.add(constellation, 0, 1);
        friendAttr.add(birthday, 1, 1);
        friendAttr.add(blood, 2, 1);
        friendAttr.add(bottom, 0, 2, 3, 1);
        rootFriendInfo.setTop(head_name_msg);
        rootFriendInfo.setCenter(mid);
        return rootFriendInfo;
    }

    public void showMsg(Message message, Boolean save) {
        if (save) {
            ManageHistoryMsg.addhistoryMsg(message);
        }
        try {
            if(obList.size()==0){
                return;
            }
            if (obList.size() == 1) {
                if (obList.get(0).getUserid().startsWith("@@##")) {
                    return;
                }
            }
            if (ManageChat.getFriendChat(message.getSender()) != null || OwnInformation.getMyinformation().getUserid().equals(message.getSender())) {
                VBox outpane;
                Date date = df.parse(message.getSendTime());
                boolean timeflag = false;
                StackPane timeStack = null;
                if(lasttime == null || date.getTime() - lasttime.getTime() > 1000 * 180){
                    timeflag = true;
                    timeStack = new StackPane(new Label(message.getSendTime()));
                }
                lasttime = date;
                ImageView headimageView;
                if (message.getSender().equals(OwnInformation.getMyinformation().getUserid())) {
                    if (OwnInformation.getMyinformation().getHead() == null) {
                        headimageView = new ImageView(this.getClass().getResource("/resources/head/" + (Math.abs((message.getSender().hashCode() % 100)) + 1) + ".jpg").toString());
                    } else if(OwnInformation.getMyinformation().getHead().startsWith("defaulthead:")){
                        headimageView = new ImageView(this.getClass().getResource("/resources/head/" + OwnInformation.getMyinformation().getHead().replace("defaulthead:","")).toString());
                    }else {
                        InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(OwnInformation.getMyinformation().getHead().getBytes()));
                        headimageView = new ImageView(new Image(in));
                    }
                } else if (ManageFriendList.getFriend(message.getSender()).getHead() == null) {
                    headimageView = new ImageView(this.getClass().getResource("/resources/head/" + (Math.abs((message.getSender().hashCode() % 100)) + 1) + ".jpg").toString());
                } else if(ManageFriendList.getFriend(message.getSender()).getHead().startsWith("defaulthead:")){
                    headimageView = new ImageView(this.getClass().getResource("/resources/head/" + ManageFriendList.getFriend(message.getSender()).getHead().replace("defaulthead:","")).toString());
                }else {
                    InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(ManageFriendList.getFriend(message.getSender()).getHead().getBytes()));
                    headimageView = new ImageView(new Image(in));
                }

                headimageView.setFitHeight(runHeight * 0.058);
                headimageView.setFitWidth(runHeight * 0.058);
                Rectangle rect = new Rectangle(headimageView.prefWidth(-1), headimageView.prefHeight(-1));
                rect.setArcWidth(runHeight * 0.028);
                rect.setArcHeight(runHeight * 0.028);
                headimageView.setClip(rect);
                headimageView.setPreserveRatio(true);

                double[] points;
                if (message.getSender().equals(OwnInformation.getMyinformation().getUserid())) {
                    outpane = (VBox) ManageChat.getOutPane(message.getGetter()).getContent();
                    points = new double[]{
                            0.0, 0.0,
                            0.0, 10.0,
                            10.0, 5.0
                    };
                } else {
                    outpane = (VBox) ManageChat.getOutPane(message.getSender()).getContent();
                    points = new double[]{
                            0.0, 5.0,
                            10.0, 0.0,
                            10.0, 10.0
                    };
                }
                ImageView imageView;
                Polygon triangle = new Polygon(points);

                HBox messageBox = new HBox();
                messageBox.setPrefWidth(runWidth * 0.5);
                messageBox.setPadding(new Insets(10, 5, 10, 5));
                if (MessageType.MESSAGE_COMM.equals(message.getMesType())) {
                    Label messageBubble = new Label(message.getContent());
                    messageBubble.setWrapText(true);
                    messageBubble.setMaxWidth(runWidth * 0.5);

                    messageBubble.setPadding(new Insets(6));
                    messageBubble.setFont(new Font(14));
                    HBox.setMargin(messageBubble, new Insets(8, 0, 0, 0));
                    if (message.getSender().equals(OwnInformation.getMyinformation().getUserid())) {
                        triangle.setFill(Color.rgb(179,231,244));
                        messageBubble.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 4px;");
                        HBox.setMargin(triangle, new Insets(15, 10, 0, 0));
                        messageBox.getChildren().addAll(messageBubble, triangle, headimageView);
                        messageBox.setAlignment(Pos.TOP_RIGHT);
                    } else {
                        triangle.setFill(Color.rgb(241,236,236));
                        messageBubble.setStyle("-fx-background-color: rgb(241,236,236); -fx-background-radius: 4px;");
                        HBox.setMargin(triangle, new Insets(15, 0, 0, 10));
                        messageBox.getChildren().addAll(headimageView, triangle, messageBubble);
                    }
                    if(timeflag) {
                        StackPane finalTimeStack = timeStack;
                        Platform.runLater(() -> outpane.getChildren().add(finalTimeStack));
                    }
                    Platform.runLater(()->outpane.getChildren().add(messageBox));
                } else if (MessageType.MESSAGE_COMM_IMAGE.equals(message.getMesType())) {
                    if (message.getContent().indexOf("@#@") != 0) {
                        InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(message.getContent().getBytes()));
                        Image image = new Image(in);
                        imageView = new ImageView(image);
                        if (image.getWidth() > runWidth * 0.25) {
                            imageView.setFitWidth(runWidth * 0.25);
                            imageView.setFitHeight(runWidth * 0.25 / image.getWidth() * image.getHeight());
                        }
                        imageView.setOnMouseClicked(mouseEvent -> {
                            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                                if (mouseEvent.getClickCount() == 2) {
                                    Platform.runLater(() -> new ImageShow(image));
                                }
                            }
                        });
                    } else {
                        Image image = new Image(this.getClass().getResource("/resources/emoji/" + message.getContent().split("@#@")[1]).toString());
                        imageView = new ImageView(image);
                        imageView.setOnMouseClicked(mouseEvent -> {
                            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                                if (mouseEvent.getClickCount() == 2) {
                                    Platform.runLater(() -> new ImageShow(image));
                                }
                            }
                        });
                    }

                    StackPane imageStack = new StackPane(imageView);
                    if(OwnInformation.getMyinformation().getUserid().equals(message.getSender())) {
                        imageStack.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 4px;-fx-padding: 8");
                        triangle.setFill(Color.rgb(179,231,244));
                    } else {
                        imageStack.setStyle("-fx-background-color: rgb(241,236,236); -fx-background-radius: 4px;-fx-padding: 8");
                        triangle.setFill(Color.rgb(241,236,236));
                    }
                        if (message.getSender().equals(OwnInformation.getMyinformation().getUserid())) {
                        HBox.setMargin(triangle, new Insets(15, 10, 0, 0));
                        messageBox.getChildren().addAll(imageStack, triangle, headimageView);
                        messageBox.setAlignment(Pos.TOP_RIGHT);
                    } else {
                        HBox.setMargin(triangle, new Insets(15, 0, 0, 10));
                        messageBox.getChildren().addAll(headimageView, triangle, imageStack);
                    }
                    if(timeflag) {
                        StackPane finalTimeStack = timeStack;
                        Platform.runLater(() -> outpane.getChildren().add(finalTimeStack));
                    }
                    Platform.runLater(()->outpane.getChildren().add(messageBox));
                }else if (MessageType.MESSAGE_COMM_VOICE.equals(message.getMesType())) {

                    imageView = new ImageView(this.getClass().getResource("/resources/image/voice_play.png").toString());
                    HBox hBox = new HBox(20, imageView);
                    hBox.setAlignment(Pos.CENTER);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    MyRecord mr = new MyRecord();
                    Service<Integer> voiceback = new Service<Integer>() {
                        @Override
                        protected Task<Integer> createTask() {
                            return new Task<Integer>() {
                                @Override
                                protected Integer call() throws InterruptedException {
                                    Thread.sleep(mr.getMicrosecondLengthForWav(message.getContent()) * 1000 + 500);
                                    imageView.setImage(new Image(this.getClass().getResource("/resources/image/voice_play.png").toString()));
                                    return null;
                                }
                            };
                        }
                    };
                    Label voicetime = new Label(mr.getMicrosecondLengthForWav(message.getContent()) + "''");
                    hBox.getChildren().add(voicetime);
                    hBox.setOnMouseClicked(mouseEvent -> {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1) {
                                imageView.setImage(new Image(this.getClass().getResource("/resources/image/voice_play_.png").toString()));
                                mr.play(message.getContent());
                                voiceback.restart();
                            }
                        }
                    });

                    StackPane imageStack = new StackPane(hBox);
                    if(OwnInformation.getMyinformation().getUserid().equals(message.getSender())) {
                        imageStack.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 4px;-fx-padding: 8");
                        triangle.setFill(Color.rgb(179,231,244));
                    } else {
                        imageStack.setStyle("-fx-background-color: rgb(241,236,236); -fx-background-radius: 4px;-fx-padding: 8");
                        triangle.setFill(Color.rgb(241,236,236));
                    }
                    if (message.getSender().equals(OwnInformation.getMyinformation().getUserid())) {
                        HBox.setMargin(triangle, new Insets(15, 10, 0, 0));
                        messageBox.getChildren().addAll(imageStack, triangle, headimageView);
                        messageBox.setAlignment(Pos.TOP_RIGHT);
                    } else {
                        HBox.setMargin(triangle, new Insets(15, 0, 0, 10));
                        messageBox.getChildren().addAll(headimageView, triangle, imageStack);
                    }
                    if(timeflag) {
                        StackPane finalTimeStack = timeStack;
                        Platform.runLater(() -> outpane.getChildren().add(finalTimeStack));
                    }
                    Platform.runLater(()->outpane.getChildren().add(messageBox));
                }
            }
        } catch (Exception ignored) { ignored.printStackTrace(); }
    }

    public void addFriendRequest(List<PeopleInformation> allfriends) {
        boolean istrue = true;
        for (PeopleInformation peopleInformation : allfriends) {
            addfriendrequestlist.add(peopleInformation);
            for (PeopleInformation peopleInformation1 : obList) {
                if (peopleInformation1.getUserid().startsWith("@@##")) {
                    Platform.runLater(() -> listView.getSelectionModel().select(null));
                    Platform.runLater(() -> listView.getSelectionModel().select(peopleInformation1));
                    istrue = false;
                }
            }
            if (istrue) {
                peopleInformation.setUserid("@@##" + peopleInformation.getUserid());
                obList.add(peopleInformation);
                Platform.runLater(() -> listView.getSelectionModel().select(peopleInformation));
            }
        }
    }

    public void setMakeBox(Message message) {
        PeopleInformation people = (PeopleInformation) message.getLists().get(0);
        boolean flag = true;
        for (PeopleInformation peopleInformation : addfriendrequestlist) {
            if (peopleInformation.equalsObjct((PeopleInformation) message.getLists().get(0))) {
                people = peopleInformation;
                flag = false;
            }
        }
        if (flag)
            ManageMainGUI.getMainGui().addFriendRequest(message.getLists());
        if (message.getSender().equals(OwnInformation.getMyinformation().getUserid())) {
            Label label = new Label();
            switch (message.getContent()) {
                case "adding":
                    label.setText("已申请");
                    break;
                case "added":
                    label.setText("已同意");
                    break;
                case "refuse":
                    label.setText("已拒绝");
                    break;
            }
            while (addfriendPane.get(people) == null) ;
            Pane pane = addfriendPane.get(people);
            HBox hBox = (HBox) pane.getChildren().get(1);
            try {
                hBox.getChildren().remove(1, 3);
            } catch (Exception e) {
                hBox.getChildren().remove(1, 2);
            }

            label.setMinWidth(60);
            hBox.getChildren().add(label);
        }
    }

    public void setMakeBox2(Message message) {
        if (message.getContent().equals("adding")) {
            addFriendRequest(message.getLists());
            return;
        }
        PeopleInformation people = null;
        for (PeopleInformation peopleInformation : addfriendrequestlist) {
            if (peopleInformation.getUserid().replace("@@##", "").equals(message.getSender())) {
                people = peopleInformation;
                break;
            }
        }

        Label label = new Label();
        if (message.getContent().equals("refuse")) {
            label.setText("已拒绝");
        } else if (message.getContent().equals("added")) {
            label.setText("已同意");
        }

        Pane pane = addfriendPane.get(people);
        HBox hBox = (HBox) pane.getChildren().get(1);

        Platform.runLater(() -> {
            hBox.getChildren().remove(1, 2);
            hBox.getChildren().add(label);
        });
        label.setMinWidth(60);
    }

    public void setMakeBox3(PeopleInformation p) {
        boolean f = true;
        p.setUserid("@@##" + p.getUserid());
        List list = new ArrayList();
        list.add(p);
        PeopleInformation people = p;
        for (PeopleInformation peopleInformation : addfriendrequestlist) {
            if (peopleInformation.equalsObjct(p)) {
                people = peopleInformation;
                f = false;
                break;
            }
        }

        if (f) {
            addFriendRequest(list);
        }

        list = null;

        listView.getSelectionModel().select(people);
        Label label = new Label("已申请");
        Pane pane = addfriendPane.get(people);
        HBox hBox = (HBox) pane.getChildren().get(1);

        Platform.runLater(() -> {
            try {
                hBox.getChildren().remove(1, 3);
            } catch (Exception e) {
                hBox.getChildren().remove(1, 2);
            }
            hBox.getChildren().add(label);
        });
        label.setMinWidth(60);
    }

    public void setAllfriends(List<PeopleInformation> allfriends) {
        VBox vBox = (VBox) titledPaneMap.get("好友").getContent();

        for (PeopleInformation peopleInformation : allfriends) {
            Platform.runLater(() -> vBox.getChildren().add(friendInformation(peopleInformation, vBox)));
        }
    }

    public ImageView getMyHeadImageView() {
        return myHeadImageView;
    }

    public void setNewFriendsName(PeopleInformation friend) {
        VBox vBox = (VBox) friendsTitledPane.getContent();
        for (Node node : vBox.getChildren()) {
            AnchorPane anchorPane = (AnchorPane) node;
            if (anchorPane.getChildren().get(1).getUserData().equals(friend.getUserid())) {
                ImageView imageView = (ImageView) anchorPane.getChildren().get(0);
                if(friend.getHead().startsWith("defaulthead:")){
                    imageView.setImage(new Image("/resources/head/" + friend.getHead().replace("defaulthead:","")));
                }else {
                    imageView.setImage(new Image(Base64.getDecoder().wrap(new ByteArrayInputStream(friend.getHead().getBytes()))));
                }
                Label name = (Label) anchorPane.getChildren().get(1);
                if (ManageFriendList.getFriend(friend.getUserid()).getNote() != null) {
                    name.setText(ManageFriendList.getFriend(friend.getUserid()).getNote());
                } else {
                    name.setText(friend.getNickname());
                }
                Label explian = (Label) anchorPane.getChildren().get(2);
                explian.setText(friend.getSignature());
            }
        }
    }

    public void doMsg(String friendid) {
        boolean flag = true;
        PeopleInformation friend = null;
        for (PeopleInformation people : obList) {
            if (people.getUserid().equals(friendid)) {
                friend = people;
                flag = false;
                break;
            }
        }
        if (flag) {
            friend = ManageFriendList.getFriend(friendid);
        }

        PeopleInformation finalFriend = friend;
        Platform.runLater(() -> {
            try {
                if (!friendid.equals(listView.getSelectionModel().getSelectedItems().get(0).getUserid())) {
                    obList.remove(finalFriend);
                    obList.add(0, finalFriend);
                }
            } catch (Exception e) {
                obList.remove(finalFriend);
                obList.add(0, finalFriend);
            }
        });

        if(listView.getSelectionModel().getSelectedItems().size() == 0 || !listView.getSelectionModel().getSelectedItems().get(0).getUserid().equals(friendid)) {
//            new BasicPlayer().sound("/resources/music/system.wav");
            if (Map.get(friend) == null) {
                ArrayList list = new ArrayList();
                ObservableList observableList = FXCollections.observableList(list);
                observableList.add(1);
                Map.put(friend, observableList);
            } else {
                Map.get(friend).set(0, (Integer) Map.get(friend).get(0) + 1);
            }
        }
        friend = null;
    }

    public boolean isMusic() {
        return music;
    }
}