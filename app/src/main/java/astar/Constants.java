package astar;

import javafx.scene.paint.Color;

public class Constants {
    public static final double Radius = 5;
    public static final double MaxRatio = 50000;
    public static final double ObstacleRatio = MaxRatio / 4;
    public static final double NodeDistance = 20;
    public static final int SleepDelay = 1;
    public static final Color BackgroupColor = Color.valueOf(Constants.BackgroupColorString);
    public static final String BackgroupColorString = "#FFFFFF";
    public static final Color DotColor = Color.valueOf(Constants.DotColorString);
    public static final String DotColorString = "#e5e5e5";
    public static final Color ObstacleColor = Color.valueOf(Constants.ObstacleColorString);
    public static final String ObstacleColorString = "#14213D";
}
