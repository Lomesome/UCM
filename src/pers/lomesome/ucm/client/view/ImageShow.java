package pers.lomesome.ucm.client.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageShow {
    private boolean flag = true;
    private ImageView imageView;
    double x;
    Stage primaryStage = new Stage();
    Image image;
    public ImageShow(Image image) {
        this.image = image;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        int width = (int) dim.getWidth();
        int height = (int) dim.getHeight();
        ImageView img = new ImageView(image);
        img.setPreserveRatio(true);
        StackPane imageHolder = new StackPane(img);
        BorderPane border = new BorderPane();
        StackPane stack = new StackPane();
        ScrollPane scroll = new ScrollPane();
        border.setCenter(scroll);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setContent(imageHolder);
        scroll.setStyle("-fx-background-color: transparent");
        if (image.getWidth() > width * 0.5) {
            double absx = width * 0.5 / image.getWidth();
            img.setFitWidth(image.getWidth() * absx);
            img.setPreserveRatio(true);
            primaryStage.setHeight(image.getWidth() * absx);
            if (image.getHeight() * absx > height * 0.8) {
                primaryStage.setHeight(height * 0.8);
            } else {
                primaryStage.setHeight(image.getHeight() * absx);
            }
        } else if (image.getHeight() > height * 0.8) {
            primaryStage.setHeight(height * 0.8);
        }

        final ChangeListener<Number> listener = new ChangeListener<Number>() {
            final Timer timer = new Timer();
            TimerTask task = null;
            final long delayTime = 5;

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) {
                if (task != null) {
                    task.cancel();
                }
                task = new TimerTask() {
                    @Override
                    public void run() {
                        imageHolder.setMinHeight(primaryStage.getHeight() - 10);
                        flag = true;
                        if (img.getFitHeight() + 10 != primaryStage.getHeight()) {
                            img.setFitHeight(primaryStage.getHeight() - 20);
                        }
                        if (img.getFitWidth() + 10 != primaryStage.getWidth()) {
                            img.setFitWidth(primaryStage.getWidth());
                        }
                        scroll.setLayoutX((primaryStage.getHeight() - img.getFitHeight()) / 2);
                    }
                };
                timer.schedule(task, delayTime);
            }
        };

        border.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                if (event.getZoomFactor() < 1 && img.getFitHeight() > 30) {
                    if (img.getFitHeight() < 100) {
                        img.setFitHeight(img.getFitHeight() - 20);
                    } else {
                        img.setFitHeight(img.getFitHeight() - 40);
                    }
                }
                if (event.getZoomFactor() > 1 && img.getFitHeight() < image.getHeight() - 30) {
                    if (img.getFitHeight() < 100) {
                        img.setFitHeight(img.getFitHeight() + 20);
                    } else {
                        img.setFitHeight(img.getFitHeight() + 40);
                    }
                }
                event.consume();
            }
        });

        border.setOnZoomStarted(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                event.consume();
            }
        });

        border.setOnZoomFinished(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                event.consume();
            }
        });

        border.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        if (flag) {
                            img.setFitHeight(image.getHeight());
                            img.setFitWidth(image.getWidth());
                            flag = false;
                        } else {
                            img.setFitHeight(primaryStage.getHeight());
                            img.setFitWidth(primaryStage.getWidth());
                            flag = true;
                        }
                    }
                }
            }
        });

        primaryStage.widthProperty().addListener(listener);
        primaryStage.heightProperty().addListener(listener);
        imageHolder.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scroll.getViewportBounds().getWidth(), scroll.viewportBoundsProperty()));
        stack.getChildren().add(imageHolder);
        stack.setAlignment(Pos.CENTER);
        Scene scene = new Scene(border);
        scene.setFill(null);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}