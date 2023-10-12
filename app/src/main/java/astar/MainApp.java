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
        var horizontalDots = (int) ((screenWidth) / (Constants.NodeDistance));
        var verticalDots = (int) ((screenHeight) / (Constants.NodeDistance));
        primaryStage = new Views(horizontalDots - 1, verticalDots - 6, Constants.Radius);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
