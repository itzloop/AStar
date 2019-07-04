package aStar;

import aStar.graph.Node;
import aStar.graph.Vector2D;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class GV {
    public static final double screenWidth  = Screen.getPrimary().getBounds().getWidth();
    public static final double screenHeight = Screen.getPrimary().getBounds().getHeight();
    public static final double radius = 5;
    public static final double maxRatio= 50000;
    public static final double obstacleRatio = maxRatio/2;
    public static final double nodeDistance= 20;
    public static final int W = 95;
    public static final int H = 50;
    public static Vector2D mousePosition = new Vector2D(0,0);
    public static Vector2D nodeCenterPosition = new Vector2D(0,0);
    public static Color nodeColor= Color.RED;
    public static Node start;
    public static Node target;
}
