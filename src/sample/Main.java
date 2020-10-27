package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    //define offsets
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Simple Weather Application");
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // * Author: J.Clarke, J.Connors, E. Bruno.
        // * Title: JavaFX : developing rich Internet applications (Applied. 5.24 section: Concepts of mouse actions)
        // * Retrieval date: 27/3/20,
        // * URL: https://learning.oreilly.com/library/view/JavaFX%E2%84%A2:+Developing+Rich+Internet+Applications/9780137013524/ch05.html#ch05list24 -->

        //grab root
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        //move around the application
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        //set application icon
        // * Author: FlatIconMaker,
        // * Retrieval date: 12/4/20,
        // * URL: http://www.iconarchive.com/show/flat-style-icons-by-flaticonmaker/cloud-icon.html
        Image image = new Image("/resources/cloud-icon.png");
        primaryStage.getIcons().add(image);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
