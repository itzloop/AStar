package astar.api;

import astar.Constants;
import astar.exceptions.AlreadyRunningException;
import astar.exceptions.NoPathFoundException;
import astar.graph.Graph;
import astar.graph.Node;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Views extends Stage {
    private Graph graph;
    private Pane pane;
    private int width, height;
    private double radius;
    private Node start, target;
    private Button btnAStar, btnDijkstra, btnReArrange, btnClear;
    private HBox bottomContainer;

    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private AtomicBoolean isChoosingStart = new AtomicBoolean(true);

    public Views(int width, int height, double radius) {
        this.graph = new Graph(width, height);
        this.pane = new Pane();
        BackgroundFill backgroundFill = new BackgroundFill(
                Constants.BackgroupColor,
                new CornerRadii(0),
                new Insets(0));

        Background background = new Background(backgroundFill);

        this.pane.setBackground(background);

        this.width = width;
        this.height = height;
        this.radius = radius;

        // set node handlers
        for (var node : this.graph.nodes()) {
            node.setOnMouseClicked(event -> {
                System.out.println(event);
                if (isChoosingStart.getAndSet(false)) {
                    if (this.start != null) {
                        this.start.setObstacle(this.start.isObstacle());
                    }
                    this.start = (Node) event.getTarget();
                    this.start.setFill(Color.RED);
                } else {
                    isChoosingStart.set(true);
                    if (this.target != null) {
                        this.target.setObstacle(this.target.isObstacle());
                    }

                    this.target = (Node) event.getTarget();
                    this.target.setFill(Color.GREEN);
                }
            });
        }

        var borderPane = new BorderPane();
        borderPane.setCenter(pane);
        this.btnAStar = new Button("run A Star");
        this.btnAStar.setOnAction(event -> this.aStar(event));

        this.btnDijkstra = new Button("Dijkstra");
        this.btnDijkstra.setOnAction(event -> this.dijkstra(event));

        this.btnReArrange = new Button("rearrange");
        this.btnReArrange.setOnAction(event -> this.reArrange(event));

        this.btnClear = new Button("clear");
        this.btnClear.setOnAction(event -> this.clear(event));
        this.bottomContainer = new HBox(this.btnAStar, this.btnDijkstra, this.btnReArrange, this.btnClear);
        borderPane.setBottom(this.bottomContainer);

        for (Node n : graph.nodes()) {
            pane.getChildren().add(n);
        }

        Scene scene = new Scene(borderPane, this.width * this.radius, this.height * this.radius);
        this.setScene(scene);
        this.setTitle("A-Star");
        this.setMaximized(true);
    }

    private void aStar(ActionEvent event) {
        if (this.isRunning.getAndSet(true)) {
            return;
        }

        setButtonsEnable(false);

        new Thread(() -> {
            try {
                if (this.start == null || this.target == null)
                    return;
                Stack<Node> nodes = graph.AStar(this.start, this.target);
                Platform.runLater(() -> {
                    Node a, b;
                    for (int i = 1; i < nodes.size(); i++) {
                        a = nodes.get(i - 1);
                        b = nodes.get(i);
                        a.setFill(Color.RED);
                        b.setFill(Color.RED);
                        Line line = new Line(b.getCenterX(), b.getCenterY(), a.getCenterX(), a.getCenterY());
                        line.setStrokeWidth(5);
                        line.setStroke(Color.CYAN);
                        pane.getChildren().add(line);
                    }
                });

                this.start = null;
                this.target = null;
            } catch (NoPathFoundException e) {
                Platform.runLater(() -> new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).showAndWait());
            } catch (AlreadyRunningException e) {
                Platform.runLater(() -> new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).showAndWait());
                return;
            } finally {
                setButtonsEnable(true);
                this.isRunning.getAndSet(false);
            }
        }).start();
    }

    private void dijkstra(ActionEvent event) {
        if (this.isRunning.getAndSet(true)) {
            return;
        }

        setButtonsEnable(false);

        new Thread(() -> {
            try {
                List<Node> path = graph.dijkstra(this.start, this.target);
                if (this.start == null || this.target == null)
                    return;

                Platform.runLater(() -> {
                    Node a, b;
                    for (int i = 1; i < path.size(); i++) {
                        a = path.get(i - 1);
                        b = path.get(i);
                        a.setFill(Color.RED);
                        b.setFill(Color.RED);
                        Line line = new Line(b.getCenterX(), b.getCenterY(), a.getCenterX(), a.getCenterY());
                        line.setStrokeWidth(5);
                        line.setStroke(Color.CYAN);
                        this.pane.getChildren().add(line);
                    }
                });

                this.start = null;
                this.target = null;
            } catch (NoPathFoundException e) {
                System.out.println(e.getMessage());
                Platform.runLater(() -> new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).showAndWait());
            } catch (AlreadyRunningException e) {
                return;
            } finally {
                setButtonsEnable(true);
                this.isRunning.getAndSet(false);
            }

        }).start();
    }

    private void reArrange(ActionEvent event) {
        this.graph.ReArrange();
        this.start = null;
        this.target = null;
        this.removePath();
    }

    private void clear(ActionEvent event) {
        this.graph.Clear();
        this.start = null;
        this.target = null;
        this.removePath();
    }

    private void removePath() {
        var childrenToRemove = new ArrayList<Line>();
        for (var child : this.pane.getChildren()) {
            if (child instanceof Line) {
                childrenToRemove.add((Line) child);
            }
        }

        this.pane.getChildren().removeAll(childrenToRemove);
    }

    private void setButtonsEnable(boolean enable) {
        this.bottomContainer.setDisable(!enable);
    }
}
