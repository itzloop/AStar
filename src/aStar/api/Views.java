package aStar.api;

import aStar.GV;
import aStar.graph.Graph;
import aStar.graph.Node;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.*;


public class Views extends Stage {
    private Graph graph;
    private Pane pane;


    public Views()
    {
        graph = new Graph(GV.W , GV.H);
        Pane pane = new Pane();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        Button btnRun = new Button("run A Star");
        btnRun.setOnAction( event -> {
            new Thread(() -> {
                if(GV.start == null || GV.target == null)
                    return;
                Optional<Stack<Node>> nodes = graph.AStar(GV.start , GV.target);
                if(nodes.isPresent())
                {
                    Node A, B;
                    for (int i = 1; i < nodes.get().size(); i++) {
                        A = nodes.get().get(i-1);
                        B = nodes.get().get(i);
                        A.setFill(Color.RED);
                        B.setFill(Color.RED);
                        Line line = new Line(B.getCenterX() , B.getCenterY() , A.getCenterX() , A.getCenterY());
                        line.setStrokeWidth(5);
                        line.setStroke(Color.CYAN);
                        Platform.runLater(()->pane.getChildren().add(line));
                    }
                }
                else {
                    Platform.runLater(() -> new Alert(Alert.AlertType.CONFIRMATION,"No Path Found").showAndWait());
                }
                GV.start = null;
                GV.target = null;
            }).start();
        });


        Button btnDijkstra =  new Button("Dijkstra");
        btnDijkstra.setOnAction(event -> {
            new Thread(() -> {
                Optional<List<Node>> path= graph.dijkstra(GV.start , GV.target);
                if(GV.start == null || GV.target == null)
                    return;
                if(path.isPresent())
                {
                    Node A, B;
                    for (int i = 1; i < path.get().size(); i++) {
                        A = path.get().get(i-1);
                        B = path.get().get(i);
                        A.setFill(Color.RED);
                        B.setFill(Color.RED);
                        Line line = new Line(B.getCenterX() , B.getCenterY() , A.getCenterX() , A.getCenterY());
                        line.setStrokeWidth(5);
                        line.setStroke(Color.CYAN);
                        Platform.runLater(()->pane.getChildren().add(line));
                    }
                }else {
                    Platform.runLater(() -> new Alert(Alert.AlertType.CONFIRMATION,"No Path Found").showAndWait());
                }

                GV.start = null;
                GV.target = null;
            }).start();
        });

        Button btnReset = new Button("reset");
        btnReset.setOnAction(event -> {
            graph = new Graph(GV.W , GV.H);
            pane.getChildren().clear();
            for (Node n : graph.nodes())
            {
                pane.getChildren().add(n);
            }
            GV.start = null;
            GV.target = null;
        });
        HBox hBox = new HBox(btnRun,btnDijkstra, btnReset);
        borderPane.setBottom(hBox);

        for (Node n : graph.nodes())
        {
            pane.getChildren().add(n);
        }


        Scene scene = new Scene(borderPane , GV.W*GV.radius , GV.H*GV.radius);
        this.setScene(scene);
        this.setTitle("A-Star");
        this.setFullScreen(true);
    }
}
