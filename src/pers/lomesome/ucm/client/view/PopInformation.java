package pers.lomesome.ucm.client.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pers.lomesome.ucm.client.tools.*;
import pers.lomesome.ucm.common.Message;
import pers.lomesome.ucm.common.MessageType;
import pers.lomesome.ucm.common.PeopleInformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;

public class PopInformation {
    GridPane friendAttr = new GridPane();
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage = new Stage();
    private ImageView friendHead;

    public  PopInformation(PeopleInformation friend) {
        final ComboBox<String> sexCBox = new ComboBox<>();
        sexCBox.getItems().addAll("男", "女");
        if (friend.getSex() != null)
            sexCBox.getSelectionModel().select(friend.getSex());

        final ComboBox<String> ageCBox = new ComboBox<>();
        for (int i = 0; i < 120; i++) {
            ageCBox.getItems().add(String.valueOf(i));
        }
        ageCBox.getSelectionModel().select(friend.getAge());

        final ComboBox<String> zodiacCBox = new ComboBox<>();
        zodiacCBox.getItems().addAll("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪");

        final ComboBox<String> constellationCBox = new ComboBox<>();
        constellationCBox.getItems().addAll("摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座");

        final ComboBox<String> bloodCBox = new ComboBox<>();
        bloodCBox.getItems().addAll("未知", "A型", "B型", "O型", "AB型", "其它");
        DatePicker datePicker;
        if (friend.getBirthday() == null) {
            datePicker = new DatePicker();
        } else {
            datePicker = new DatePicker(LocalDate.of(Integer.parseInt(friend.getBirthday().split("-")[0]), Integer.parseInt(friend.getBirthday().split("-")[1]), Integer.parseInt(friend.getBirthday().split("-")[2])));
        }
        BorderPane rootFriendInfo = new BorderPane();
        VBox head_name_msg = new VBox(20);
        head_name_msg.setAlignment(Pos.CENTER);
        head_name_msg.setPadding(new Insets(30, 0, 30, 0));
        if(friend.getHead() == null)
            friendHead = new ImageView(this.getClass().getResource("/source/head/" + (Math.abs((friend.getUserid().hashCode() % 100)) + 1) + ".jpg").toString());
        else {
            InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(friend.getHead().getBytes()));
            friendHead = new ImageView(new Image(in));
        }

        ImageView hoverImage = new ImageView(this.getClass().getResource("/source/image/camera.png").toString());
        hoverImage.setStyle("-fx-background-color: transparent");
        hoverImage.setFitHeight(80);
        hoverImage.setFitWidth(80);
        Rectangle rect = new Rectangle(hoverImage.prefWidth(-1), hoverImage.prefHeight(-1));
        rect.setArcWidth(80);
        rect.setArcHeight(80);
        hoverImage.setVisible(false);
        hoverImage.setClip(rect);
        hoverImage.setPreserveRatio(true);
        StackPane headStackPane = new StackPane();
        headStackPane.getChildren().addAll(friendHead, hoverImage);
        hoverImage.setOnMouseExited(event -> hoverImage.setVisible(false));
        friendHead.setOnMouseEntered(event -> hoverImage.setVisible(true));
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
        TextField nameFiled = new TextField(friend.getNickname());
        nameFiled.setMaxWidth(100);

        ImageView chatImage = new ImageView(this.getClass().getResource("/source/image/chat.png").toString());
        imagePane.getChildren().add(chatImage);
        imagePane.setMaxWidth(chatImage.getFitWidth());
        imagePane.setOnMouseEntered(event -> {
            imagePane.getChildren().remove(0);
            ImageView newchatImage = new ImageView(this.getClass().getResource("/source/image/chathover.png").toString());
            newchatImage.setFitWidth(40);
            newchatImage.setFitHeight(40);
            imagePane.getChildren().add(newchatImage);
        });
        imagePane.setOnMouseExited(event -> {
            imagePane.getChildren().remove(0);
            ImageView newchatImage = new ImageView(this.getClass().getResource("/source/image/chat.png").toString());
            newchatImage.setFitWidth(40);
            newchatImage.setFitHeight(40);
            imagePane.getChildren().add(newchatImage);
        });
        chatImage.setFitWidth(40);
        chatImage.setFitHeight(40);
        hoverImage.setOnMouseClicked(event -> {
            if(ManageChangeHead.getMakeHead() == null){
                MakeHead makeHead = new MakeHead();
                makeHead.mainView();
                ManageChangeHead.setMakeHead(makeHead);
            }else {
                ManageChangeHead.getMakeHead().mainView();
            }
        });

        Label set = new Label("编辑");
        Button closebutton = new Button();
        closebutton.getStyleClass().add("close");
        HBox hBox = new HBox(closebutton,set);
        closebutton.setOnMouseClicked(event -> {
            ManagePopInformation.delStage();
            stage.close();
        });

        Circle a = new Circle(2);
        ImageView closeImage = new ImageView(this.getClass().getResource("/source/image/close.png").toString());
        closeImage.setFitHeight(15);
        closeImage.setFitWidth(15);
        closebutton.setGraphic(closeImage);
        closebutton.setShape(a);

        set.setOnMouseEntered(event -> set.setTextFill(Color.SKYBLUE));
        set.setOnMouseExited(event -> set.setTextFill(Color.BLACK));
        set.setUserData("0");
        HBox.setMargin(closebutton, new Insets(0,0,0,35));
        HBox.setMargin(set, new Insets(0, 0, 0, 280));

        Text signature = new Text();
        if(friend.getSignature() == null){
            signature.setText("输入个性签名");
        }else {
            signature.setText(friend.getSignature());
        }
        head_name_msg.getChildren().addAll(hBox, headStackPane, friendname, signature);
        head_name_msg.setStyle("-fx-background-color: lightgray;-fx-background-insets: 12 12 0 12;-fx-background-radius: 10 10 0 0");

        StackPane mid = new StackPane();

//        friendAttr.setStyle("-fx-background-color: red");
        friendAttr.setMaxWidth(300);
        friendAttr.setAlignment(Pos.TOP_CENTER);

        VBox sex = new VBox(10, new Label("性别"));
        sex.setAlignment(Pos.BOTTOM_CENTER);
        Text sextext = new Text();

        if(friend.getSex() == null){
            sextext.setText("?");
        } else if(friend.getSex().equals("男")){
            sextext.setText("♂");
        }else if(friend.getSex().equals("女")){
            sextext.setText("♀");
        }
        sextext.setFont(Font.font("Tahoma", 32));
        sextext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop[]{new Stop(0, Color.AQUA), new Stop(0.5f, Color.RED)}));
        sextext.setStrokeWidth(1);
        sextext.setStroke(Color.TRANSPARENT);
        sex.getChildren().add(0, sextext);

        Label ageLabel = new Label("年龄");
        VBox age = new VBox(10, ageLabel);
        age.setAlignment(Pos.BOTTOM_CENTER);
        Text agetext = new Text();
        agetext.setFont(Font.font("Tahoma", 32));
        if(friend.getAge() == null){
            agetext.setText("?");
        }else {
            agetext.setText(String.valueOf(friend.getAge()));
        }
        agetext.setFont(Font.font("Tahoma", 28));
        agetext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop[]{new Stop(0, Color.ROYALBLUE), new Stop(0.5f, Color.LIGHTBLUE)}));
        agetext.setStrokeWidth(1);
        agetext.setStroke(Color.TRANSPARENT);
        age.getChildren().add(0, agetext);

        VBox zodiac = new VBox(10, new Label("生肖"));
        zodiac.setAlignment(Pos.BOTTOM_CENTER);
        Text zodiactext = new Text();
        zodiactext.setFont(Font.font("Tahoma", 28));
        if(friend.getBirthday() == null){
            zodiactext.setText("?");
            zodiactext.setFont(Font.font("Tahoma", 32));
        }else {
            zodiactext.setText(Year.getYear(Integer.valueOf(friend.getBirthday().split("-")[0])));
        }
        zodiactext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop[]{new Stop(0, Color.AQUA), new Stop(0.5f, Color.DEEPPINK)}));
        zodiactext.setStrokeWidth(1);
        zodiactext.setStroke(Color.TRANSPARENT);
        zodiac.getChildren().add(0, zodiactext);

        VBox constellation = new VBox(10, new Label("星座"));
        constellation.setAlignment(Pos.BOTTOM_CENTER);
        Text constellationtext = new Text();
        constellationtext.setFont(Font.font("Tahoma", 25));
        if(friend.getBirthday() == null){
            constellationtext.setText("?");
            constellationtext.setFont(Font.font("Tahoma", 32));
        }else {
            constellationtext.setText(Year.getConstellation(Integer.valueOf(friend.getBirthday().split("-")[1]),Integer.valueOf(friend.getBirthday().split("-")[2])).replace("座",""));
        }
        constellationtext.setFont(Font.font("Tahoma", 25));
        constellationtext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop[]{new Stop(0, Color.AQUA), new Stop(0.5f, Color.DARKGREEN)}));
        constellationtext.setStrokeWidth(1);
        constellationtext.setStroke(Color.TRANSPARENT);
        constellation.getChildren().add(0, constellationtext);

        VBox birthday = new VBox(10, new Label("生日"));
        birthday.setAlignment(Pos.BOTTOM_CENTER);
        Text birthdaytext = new Text();
        birthdaytext.setFont(Font.font("Tahoma", 25));
        if(friend.getBirthday() == null){
            birthdaytext.setText("?");
            birthdaytext.setFont(Font.font("Tahoma", 32));
        }else {
            birthdaytext.setText(friend.getBirthday().split("-",2)[1].replace("-","/"));
        }
        birthdaytext.setFont(Font.font("Tahoma", 25));
        birthdaytext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop[]{new Stop(0, Color.INDIANRED), new Stop(0.5f, Color.ORANGE)}));
        birthdaytext.setStrokeWidth(1);
        birthdaytext.setStroke(Color.TRANSPARENT);
        birthday.getChildren().add(0, birthdaytext);

        VBox blood = new VBox(10, new Label("血型"));
        blood.setAlignment(Pos.BOTTOM_CENTER);
        Text bloodtext = new Text("?");
        bloodtext.setFont(Font.font("Tahoma", 30));
        bloodtext.setFill(new LinearGradient(0, 0, 1, 2, true, CycleMethod.REPEAT, new Stop[]{new Stop(0, Color.YELLOWGREEN), new Stop(0.5f, Color.ORANGE)}));
        bloodtext.setStrokeWidth(1);
        bloodtext.setStroke(Color.TRANSPARENT);
        blood.getChildren().add(0, bloodtext);

        ArrayList list = new ArrayList();
        ObservableList observableList = FXCollections.observableList(list);
        observableList.addListener((ListChangeListener)(o)->{
            String nn = String.valueOf(ChronoUnit.YEARS.between((Temporal) o.getList().get(o.getList().size()-1), LocalDate.now()));
            ageCBox.getSelectionModel().select(nn);
            agetext.setText(nn);
            birthdaytext.setText(String.valueOf(o.getList().get(o.getList().size()-1)).split("-",2)[1].replace("-","/"));
            String sx = Year.getYear(Integer.valueOf(String.valueOf(o.getList().get(o.getList().size()-1)).split("-",2)[0]));
            zodiactext.setText(sx);
            zodiacCBox.getSelectionModel().select(sx);
            String xz = Year.getConstellation(Integer.valueOf(String.valueOf(o.getList().get(o.getList().size()-1)).split("-")[1]),Integer.valueOf(String.valueOf(o.getList().get(o.getList().size()-1)).split("-")[2])).replace("座","");
            constellationCBox.getSelectionModel().select(xz);
            constellationtext.setText(xz.replace("座",""));
        });
        set.setOnMouseClicked(event -> {
            if (set.getUserData() == "0") {
                set.setText("完成");
                set.setUserData("1");
                head_name_msg.getChildren().set(3, new TextField(friend.getSignature()));
                VBox.setMargin(head_name_msg.getChildren().get(3), new Insets(0,50,0,50));
                sex.getChildren().set(1, sexCBox);
                age.getChildren().set(1, ageCBox);
                zodiac.getChildren().set(1, zodiacCBox);
                constellation.getChildren().set(1, constellationCBox);
                birthday.getChildren().set(1, datePicker);
                blood.getChildren().set(1, bloodCBox);
                head_name_msg.getChildren().set(2, nameFiled);
            } else {
                set.setText("编辑");
                set.setUserData("0");
                TextField t = (TextField) head_name_msg.getChildren().get(3);
                head_name_msg.getChildren().set(3, new Text(t.getText()));
                sex.getChildren().set(1, new Label("性别"));
                age.getChildren().set(1, new Label("年龄"));
                zodiac.getChildren().set(1, new Label("生肖"));
                constellation.getChildren().set(1, new Label("星座"));
                birthday.getChildren().set(1, new Label("生日"));
                blood.getChildren().set(1, new Label("血型"));
                head_name_msg.getChildren().set(2,new Label(nameFiled.getText()));
                String birth = "";
                try{
                    birth = String.valueOf(observableList.get(observableList.size() - 1));
                }catch (Exception e){}

                if(!sexCBox.getSelectionModel().getSelectedItem().equals(OwnInformation.getMyinformation().getSex()) || !ageCBox.getSelectionModel().getSelectedItem().equals(OwnInformation.getMyinformation().getAge()) || !nameFiled.getText().equals(OwnInformation.getMyinformation().getNickname()) || !t.getText().equals(OwnInformation.getMyinformation().getSignature()) || !birth.equals(OwnInformation.getMyinformation().getBirthday())) {
                    List list1 = new ArrayList();
                    OwnInformation.getMyinformation().setSex(sexCBox.getSelectionModel().getSelectedItem());
                    OwnInformation.getMyinformation().setAge(ageCBox.getSelectionModel().getSelectedItem());
                    OwnInformation.getMyinformation().setNickname(nameFiled.getText());
                    OwnInformation.getMyinformation().setSignature(t.getText());
                    try {
                        OwnInformation.getMyinformation().setBirthday(String.valueOf(observableList.get(observableList.size() - 1)));
                    } catch (Exception e) {

                    }
                    list1.add(OwnInformation.getMyinformation());
                    Message message = new Message();
                    message.setSender(OwnInformation.getMyinformation().getUserid());
                    message.setLists(list1);
                    message.setMesType(MessageType.MESSAGE_CHANGE_MY_IMFORMATION);
                    message.setSendTime(new Date().toString());
                    // 客户端A发送给服务器
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientServerThread(OwnInformation.getMyinformation().getUserid()).getS().getOutputStream());
                        oos.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    ManageFriendList.changeMyInformationToFriends();
                }
            }

        });

        sexCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("男")) {
                sextext.setText("♂");
            } else if (newValue.equals("女")) {
                sextext.setText("♀");
            }
        });

        ageCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            agetext.setText((String) newValue);
        });

        zodiacCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            zodiactext.setText((String) newValue);
        });

        constellationCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            newValue = (String) newValue;
            constellationtext.setText(((String) newValue).replace("座", ""));
        });


        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                 @Override
                 public DateCell call(DatePicker param) {
                     return new DateCell() {
                         @Override
                         public void updateItem(LocalDate item, boolean empty) {
                             super.updateItem(item, empty);
                             this.setOnMouseClicked(event -> {
                                 observableList.add(item);
                             });
                         }
                     };
                 }
             }
        );

        bloodCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            newValue = (String) newValue;
            if(newValue.equals("未知") || newValue.equals("其它")){
                bloodtext.setText("?");
            }else {
                bloodtext.setText(((String) newValue).replace("型", ""));
            }
        });

        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(20, 0, 0, 0));
        HBox ucmBox = new HBox(10);
        HBox nicknameBox = new HBox(10);
        HBox notenameBox = new HBox(10);

        Label unmLabel = new Label("UCM");
        Label nicknameLabel = new Label("昵称");
        Label notenameLabel = new Label("备注");
        unmLabel.setMinWidth(30);
        nicknameLabel.setMinWidth(30);
        notenameLabel.setMinWidth(30);

        Text ucm = new Text(friend.getUserid());
        Text nickname = new Text(friend.getNickname());
        Text notename = new Text(friend.getNote());

        unmLabel.setTextFill(Paint.valueOf("#6E6E6E"));
        nicknameLabel.setTextFill(Paint.valueOf("#6E6E6E"));
        notenameLabel.setTextFill(Paint.valueOf("#6E6E6E"));

        ucmBox.getChildren().addAll(unmLabel, ucm);
        nicknameBox.getChildren().addAll(nicknameLabel, nickname);
        notenameBox.getChildren().addAll(notenameLabel, notename);
        notenameBox.setVisible(false);
        bottom.getChildren().addAll(ucmBox, nicknameBox, notenameBox);
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
        DropShadow dropshadow = new DropShadow();// 阴影向外
        dropshadow.setRadius(10);// 颜色蔓延的距离
        dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.setOffsetY(3);// 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.setSpread(0.005);// 颜色变淡的程度
        dropshadow.setColor(Color.BLACK);// 设置颜色
        rootFriendInfo.setEffect(dropshadow);// 绑定指定窗口控件
        rootFriendInfo.setStyle("-fx-background-radius: 10px;" + "-fx-border-radius: 10px;" + "-fx-background-color: white;" + "-fx-border-width: 0;" + "-fx-background-insets:12;");
        //        mid.getChildren().add(friendAttr);
        rootFriendInfo.setTop(head_name_msg);
        rootFriendInfo.setCenter(mid);

        rootFriendInfo.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        rootFriendInfo.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(rootFriendInfo,400,650);
        scene.getStylesheets().add("/source/windowstyle.css");
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public ImageView getFriendHead() {
        return friendHead;
    }
}
