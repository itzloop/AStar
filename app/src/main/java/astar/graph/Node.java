package astar.graph;

import astar.Constants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node extends Circle implements Comparable<Node> {
    private int label;
    private boolean isObstacle;
    private double f, g, h;

    public Node(int label, double x, double y, boolean isObstacle) {
        super(Constants.radius);
        this.setCenterX(x * Constants.nodeDistance);
        this.setCenterY(y * Constants.nodeDistance);
        this.label = label;
        if (isObstacle)
            this.setFill(Color.BLACK);
        else
            this.setFill(Color.WHITE);
        this.isObstacle = isObstacle;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        if (obstacle) {
            this.setFill(Color.BLACK);

        } else {
            this.setFill(Color.WHITE);

        }
        isObstacle = obstacle;

    }

    public double distance(Node node) {
        return new Vector2D(getCenterX(), getCenterY()).distance(new Vector2D(node.getCenterX(), node.getCenterY()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node)
            return this.label == (((Node) obj).label);
        return false;
    }

    @Override
    public int compareTo(Node that) {
        if (this.f > that.f)
            return 1;
        else if (this.f < that.f)
            return -1;
        else
            return 0;
    }
}
