package aStar.api;

import aStar.GV;
import aStar.graph.Graph;
import aStar.graph.Node;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.*;


public class Views extends Stage {
    private Graph graph;

    public Views()
    {
        graph = new Graph(GV.W , GV.H);
        Pane pane = new Pane();

        for (Node n : graph.nodes())
        {
            pane.getChildren().add(n);
        }


       new Thread(() -> {
           Optional<Stack<Node>> nodes = graph.AStar((Node)pane.getChildren().get(0), (Node)pane.getChildren().get(pane.getChildren().size()-1));
           if(nodes.isPresent())
           {

               Node A, B;
               for (int i = 1; i < nodes.get().size(); i++) {
                    A = nodes.get().get(i-1);
                    B = nodes.get().get(i);
                    A.setFill(Color.RED);
                    B.setFill(Color.RED);
                   Line line = new Line(B.getCenterX() , B.getCenterY() , A.getCenterX() , A.getCenterY());
                   line.setStrokeWidth(10);
                   line.setStroke(Color.RED);
                   Platform.runLater(()->pane.getChildren().add(line));
               }




           }
           else {
               Platform.runLater(() -> new Alert(Alert.AlertType.CONFIRMATION,"No Path Found").showAndWait());
           }
       }).start();

        Scene scene = new Scene(pane , GV.W*GV.radius , GV.H*GV.radius);
        this.setScene(scene);
        this.setTitle("A-Star");
        this.setFullScreen(true);
    }
}
