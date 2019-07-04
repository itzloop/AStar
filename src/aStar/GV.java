package aStar;

import aStar.graph.Vector2D;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class GV {
    public static final double screenWidth  = Screen.getPrimary().getBounds().getWidth();
    public static final double screenHeight = Screen.getPrimary().getBounds().getHeight();
    public static final double radius = 5;
    public static final double maxRatio= 50000;
    public static final double obstacleRatio = maxRatio/6;
    public static final double nodeDistance= 20;
    public static final int W = 90;
    public static final int H = 50;
    public static Vector2D mousePosition = new Vector2D(0,0);
    public static Vector2D nodeCenterPosition = new Vector2D(0,0);
    public static final double edgeStrokeSize = 6;
    public static final double nodeStrokeSize = 2;
    public static final String backgroundColor = "rgb(152, 204, 108);";
    public static final String toolbarColor = "rgb(110, 168, 164);";
    public static final Color nodeFillColor = Color.rgb(248, 235, 152);
    public static final Color nodeSelectedColor= Color.rgb(209, 57, 61);
    public static final Color nodeStrokeColor= Color.BLACK;
    public static final Color edgeColor = Color.BLACK;



}
