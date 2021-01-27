package pers.lomesome.ucm.client.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pers.lomesome.ucm.client.tools.*;
import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class MakeHead {

    private double xOffset = 0;
    private double yOffset = 0;
    private File headfile = null;
    private Stage stage = new Stage();

    public void mainView(){
        StackPane headstack = new StackPane();
        headstack.setMinSize(200,200);
        headstack.setMaxSize(200,200);
        headstack.setStyle("-fx-background-color: transparent;-fx-border-width: 1;-fx-border-color: gray;-fx-padding: 40");
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(350,300);
        BorderPane.setAlignment(headstack, Pos.TOP_CENTER);

        BorderPane.setMargin(headstack, new Insets(32,32,32,20));
        borderPane.setMinSize(200,200);
        borderPane.setStyle("-fx-background-insets: 12;-fx-background-radius:10;");
        ImageView imageView;
        if(OwnInformation.getMyinformation().getHead() == null) {
            imageView = new ImageView(this.getClass().getResource("/source/head/" + (Math.abs((OwnInformation.getMyinformation().getUserid().hashCode() % 100)) + 1) + ".jpg").toString());
        } else {
            InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(OwnInformation.getMyinformation().getHead().getBytes()));
            imageView = new ImageView(new Image(in));
        }
        imageView.setFitWidth(190);
        imageView.setFitHeight(190);
        Slider slider = new Slider();
        slider.valueProperty().addListener((a, o, n)->{
            imageView.setFitWidth((Double) n * 5 + 190);
            imageView.setFitHeight((Double) n * 5 + 190);
        });
        slider.setMinWidth(150);
        slider.setStyle("-fx-opacity: 0.5");
        borderPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(imageView);
        hBox.setMinSize(1000,1000);
        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.setStyle("-fx-background-color: transparent");
        scrollPane.setPannable(true);
        scrollPane.setHvalue(scrollPane.getHmax() / 2);
        scrollPane.setVvalue(scrollPane.getVmax() / 2);
        scrollPane.setMinSize(200,200);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StackPane.setMargin(slider,new Insets(170,0,0,0));
        headstack.getChildren().addAll(scrollPane,slider);

        borderPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        borderPane.setOnMouseClicked(event -> {
            borderPane.requestFocus();
        });

        final ToggleGroup group = new ToggleGroup();

        ToggleButton defaultButton = new ToggleButton("默认");
        defaultButton.setToggleGroup(group);
        defaultButton.setPadding(new Insets(5,10,5,10));
        defaultButton.setStyle("-fx-background-color: transparent;-fx-background-radius: 5");

        defaultButton.selectedProperty().addListener((a,o,n)->{
            if(n){
                defaultButton.setStyle("-fx-background-color: ROYALBLUE;-fx-background-radius: 5;-fx-text-fill: white");
            }else {
                defaultButton.setStyle("-fx-background-color: transparent;-fx-background-radius: 5;-fx-text-fill: black");
            }
        });
        int j = 0;
        int k = 0;
        ImageView defaultHead;
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(200);
        gridPane.setMaxHeight(200);
        for (int i = 1; i < 100; i++ ) {
            k = (i-1)%4;
            Image image = new Image(this.getClass().getResource("/source/head/"+i+".jpg").toString());
            defaultHead = new ImageView(image);

            int finalI = i;
            defaultHead.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        headfile = new File(this.getClass().getResource("/source/head/"+ finalI +".jpg").toString());
                        scrollPane.setHvalue(scrollPane.getHmax() / 2);
                        scrollPane.setVvalue(scrollPane.getVmax() / 2);
                        scrollPane.setContent(hBox);
                        imageView.setImage(image);
                    }
                }
            });

            defaultHead.setFitHeight(45);
            defaultHead.setFitWidth(45);
            defaultHead.setStyle(" -fx-border-style: solid inside;" + "-fx-border-width: 1.5;");
            gridPane.setMargin(defaultHead, new Insets(2,2,2,2));
            gridPane.add(defaultHead, k, j);
            k++;
            if (k==4) {
                j++;
            }
        }

        Button closebutton = new Button();
        closebutton.getStyleClass().add("close-makehead");
        closebutton.setOnMouseClicked(event -> {
            ManagePopInformation.delStage();
            stage.close();
        });

        Circle circle = new Circle(2);
        ImageView closeImage = new ImageView(this.getClass().getResource("/source/image/close.png").toString());
        closeImage.setFitHeight(15);
        closeImage.setFitWidth(15);
        closebutton.setGraphic(closeImage);
        closebutton.setShape(circle);

        defaultButton.setOnMouseClicked(event -> {
            scrollPane.setContent(new HBox(gridPane));
            scrollPane.setFitToWidth(true);
        });

        ToggleButton otherButton = new ToggleButton("其它");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("jpg png", "*.jpg","*.png")
        );

        otherButton.setToggleGroup(group);
        otherButton.setPadding(new Insets(5,10,5,10));
        otherButton.setStyle("-fx-background-color: transparent;-fx-background-radius: 5");
        otherButton.selectedProperty().addListener((a,o,n)->{
            if(n){
                otherButton.setStyle("-fx-background-color: ROYALBLUE;-fx-background-radius: 5;-fx-text-fill: white");
            }else {
                otherButton.setStyle("-fx-background-color: transparent;-fx-background-radius: 5;-fx-text-fill: black");
            }
        });
        otherButton.setOnMouseClicked(event -> {
            headfile = fileChooser.showOpenDialog(stage);
            if (headfile != null) {
                scrollPane.setContent(hBox);
                scrollPane.setHvalue(scrollPane.getHmax() / 2);
                scrollPane.setVvalue(scrollPane.getVmax() / 2);
                imageView.setImage(new Image("file:" + headfile.getPath()));
            }
        });
        VBox chooseBox = new VBox(10,closebutton, defaultButton, otherButton);
        VBox.setMargin(closebutton, new Insets(0,0,15,0));
        chooseBox.setStyle("-fx-background-radius: 10 0 0 10;-fx-background-color: #d7d7d7");
        chooseBox.setPadding(new Insets(15,20,20,10));
        chooseBox.setAlignment(Pos.TOP_LEFT);
        BorderPane.setMargin(chooseBox,new Insets(12,0,12,12));
        borderPane.setLeft(chooseBox);

        Button finish = new Button("完成");
        Button cancel = new Button("取消");
        cancel.setOnMouseClicked(event -> {
            ManageChangeHead.delStage();
            stage.close();
        });

        finish.setOnMouseClicked(event -> {
            String base64img = null;
            if(headfile != null) {
                String fileUrl = headfile.getPath();
                ImageZip zipimage = new ImageZip();
                if (!fileUrl.startsWith("file:")) {
                    try {
                        base64img = zipimage.resizeImageToSmall(fileUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    BufferedImage bImage = SwingFXUtils.fromFXImage(new Image(fileUrl), null);
                    ByteArrayOutputStream s = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bImage, "jpg", s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] res = s.toByteArray();
                    Base64.Encoder encoder = Base64.getEncoder();   // 对字节数组Base64编码
                    base64img = encoder.encodeToString(res);
                }
                if (!base64img.equals(OwnInformation.getMyinformation().getHead())) {
                    List list1 = new ArrayList();
                    OwnInformation.getMyinformation().setHead(base64img);
                    list1.add(OwnInformation.getMyinformation());
                    Message message = new Message();
                    message.setSender(OwnInformation.getMyinformation().getUserid());
                    message.setLists(list1);
                    message.setMesType(MessageType.MESSAGE_CHANGE_MY_IMFORMATION);
                    message.setSendTime(new Date().toString());
//                 客户端A发送给服务器
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                        oos.writeObject(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!fileUrl.startsWith("file:"))
                        fileUrl = "file:" + fileUrl;
                    ManageMainGUI.getMainGui().getMyHeadImageView().setImage(new Image(fileUrl));
                    ManagePopInformation.getPopInformation().getFriendHead().setImage(new Image(fileUrl));
                    ManageFriendList.changeMyInformationToFriends();
                }
            }
            ManageChangeHead.delStage();
            stage.close();
        });
        finish.getStyleClass().add("head-button");
        cancel.getStyleClass().add("head-button");

        HBox buttonBox = new HBox(10,cancel, finish);
        buttonBox.setPadding(new Insets(10,32,0,0));
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        VBox head = new VBox(headstack,buttonBox);
        head.setAlignment(Pos.CENTER);
        borderPane.setCenter(head);
        DropShadow dropshadow = new DropShadow();// 阴影向外
        dropshadow.setRadius(10);// 颜色蔓延的距离
        dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.setOffsetY(3);// 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.setSpread(0.005);// 颜色变淡的程度
        dropshadow.setColor(Color.BLACK);// 设置颜色
        borderPane.setEffect(dropshadow);// 绑定指定窗口控件
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add("/source/windowstyle.css");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }
}