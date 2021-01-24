package pers.lomesome.ucm.client.view.MyUtils;

/**
 * 功能：自定义标题的样式
 */

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TopTile {
    private double xOffset = 0;
    private double yOffset = 0;
    Stage mainStage;
    public Button btnClose = new Button();
    public TopTile() { }
    public TopTile(Stage mainStage) {
        this.mainStage = mainStage;
    }
    public Pane toptitle(Stage stage) {
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        int width = (int)screenRectangle.getWidth();
        int height = (int)screenRectangle.getHeight();
        double absx =  width / 1440.0;
        double absy = height / 900.0;
        if(width <= 1440 && height <= 900) {
            absx = 1;
            absy = 1;
        }
        double runWidth = 250 * absx;
        double runHeight = 320 * absy;
        GridPane gpTitle = new GridPane();
        gpTitle.setAlignment(Pos.CENTER_LEFT);
        gpTitle.setPadding(new Insets(3));
        Button btnMin = new Button();
        Button btnMax = new Button();
        Circle a = new Circle(2);
        ImageView imageView = new ImageView(this.getClass().getResource("/source/image/background.png").toString());
        imageView.setFitHeight(runWidth*0.05);
        imageView.setFitWidth(runWidth*0.05);
        ImageView closeImage = new ImageView(this.getClass().getResource("/source/image/close.png").toString());
        closeImage.setFitHeight(runWidth*0.05);
        closeImage.setFitWidth(runWidth*0.05);
        ImageView minImageView = new ImageView(this.getClass().getResource("/source/image/min.png").toString());
        minImageView.setFitHeight(runWidth*0.05);
        minImageView.setFitWidth(runWidth*0.05);
        btnClose.setGraphic(imageView);
        btnClose.setShape(a);
        btnMax.setGraphic(imageView);
        btnMax.setShape(a);
        btnMin.setGraphic(imageView);
        btnMin.setShape(a);
        btnMin.setOnMouseClicked(event -> stage.setIconified(true));

        btnClose.setOnMouseClicked(event -> {
                if (stage == mainStage) {
                    System.exit(0);
                }
                else {
                    stage.close();
                }
            }
        );

        gpTitle.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        gpTitle.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        gpTitle.hoverProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(gpTitle.isHover()){
                    btnClose.setGraphic(closeImage);
                    btnClose.setStyle("-fx-background-color: #FF3333; ");
                    btnMin.setGraphic(minImageView);
                    btnMin.setStyle("-fx-background-color: #FFCC22; ");
                }
                else{
                    btnClose.setGraphic(imageView);
                    btnClose.setStyle("-fx-background-color: #FF3333; ");
                    btnMin.setGraphic(imageView);
                    btnMin.setStyle("-fx-background-color: #FFEE99;");
                }
            }
        });

        btnClose.getStyleClass().add("close");
        btnMin.getStyleClass().add("min");
        btnMax.getStyleClass().add("max");
        gpTitle.getStyleClass().add("window");
        HBox title = new HBox(8);
        title.hoverProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(title.isHover()){
                    btnClose.setStyle("-fx-background-color: #FF3333;" + "-fx-background-image: url(\"/source/image/close.png\");");
                    btnMin.setStyle("-fx-background-color: #FFCC22;" + "-fx-background-image: url(\"/source/image/min.png\");");
                }
                else {
                    btnClose.setStyle("-fx-background-color: #FF8888;" + "-fx-font-size:0.0001px;" + "-fx-background-insets: 0;"
                            + "-fx-border-style: solid inside;" + "-fx-border-width: 0.05;");
                    btnMin.setStyle("-fx-background-color: #FFEE99;" + "-fx-font-size:0.0001px;" + "-fx-background-insets: 0;"
                            + "-fx-border-style: solid inside;" + "-fx-border-width: 0.05;");
                }
            }
        });
        title.getChildren().addAll(btnClose, btnMin, btnMax);
        gpTitle.add(title, 0, 0);
        return gpTitle;
    }
}
