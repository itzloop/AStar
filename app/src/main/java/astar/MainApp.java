package astar;

import astar.api.Views;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        var horizontalDots = (int) ((screenWidth) / (Constants.nodeDistance));
        var verticalDots = (int) ((screenHeight) / (Constants.nodeDistance));
        primaryStage = new Views(horizontalDots - 1, verticalDots - 6, Constants.radius);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
