package aStar.graph;


import aStar.GV;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import java.util.*;

public class Graph {
    private final int V;
    private final Map<Node, LinkedHashSet<Node>> adj;

    public Graph(int W , int H)
    {
        this.V = W * H;
        adj = new LinkedHashMap<>();
        Node[][] nodes = new Node[H][W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                Node node = new Node(adj.size(), j + 1, i+1  , isObstacle() );
                node.setF(Double.MAX_VALUE);
                node.setG(Double.MAX_VALUE);
                node.setOnMouseClicked(event -> {
                    System.out.println(event);
                    if(GV.start != null)
                    {
                        GV.target = (Node)event.getTarget();
                        GV.target.setFill(Color.GREEN);
                    }
                    else
                    {
                        GV.start= (Node)event.getTarget();
                        GV.start.setFill(Color.RED);
                    }
                });
                adj.put(node, new LinkedHashSet<>());
                nodes[i][j] = node;
            }
        }


        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                Node node = nodes[i][j];
                try {
                    adj.get(node).add(nodes[i+1][j+1]);
                }catch (Exception e){}
                try {
                    adj.get(node).add(nodes[i-1][j-1]);
                }catch (Exception e){}
                try {
                    adj.get(node).add(nodes[i-1][j+1]);
                }catch (Exception e){}
                try {
                    adj.get(node).add(nodes[i+1][j-1]);
                }catch (Exception e){}
                try {
                    adj.get(node).add(nodes[i+1][j]);
                }catch (Exception e){}
                try {
                    adj.get(node).add(nodes[i-1][j]);
                }catch (Exception e){}
                try {
                    adj.get(node).add(nodes[i][j+1]);
                }catch (Exception e){}
                try {
                    adj.get(node).add(nodes[i][j-1]);
                }catch (Exception e){}
            }
        }






    }

    private Stack<Node> drawPath(Map<Node , Node> cameFrom , Node current ){
        Stack<Node> totalPath = new Stack<>();
        totalPath.push(current);
        while (cameFrom.containsKey(current) )
        {
            current = cameFrom.get(current);
            totalPath.push(current);
        }
        return totalPath;

    }
    public Optional<Stack<Node>> AStar(Node start , Node target )
        {
        Queue<Node> open = new PriorityQueue<>();
        Queue<Node> close = new PriorityQueue<>();
        Map<Node , Node> cameFrom = new HashMap<>();
        Color color = Color.RED;
        open.add(start);
        start.setG(0);
        target.setFill(Color.GREEN);
        start.setF(start.distance(target));
        Node current;
        while (!open.isEmpty())
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            current = open.poll();
            if (current.equals(target))
            {
                return Optional.ofNullable(drawPath(cameFrom , current ));
            }
            close.add(current);
            for (Node neighbor : adj.get(current))
            {
                if(!neighbor.isObstacle()) {
                    if (close.contains(neighbor)) {
                        setColor(neighbor , start , target);
                        continue;
                    }
                    double tentativeGScore = current.getG() + current.distance(neighbor);
                    if (!open.contains(neighbor))
                        open.add(neighbor);
                    else if (tentativeGScore >= neighbor.getG()) {
                        setColor(neighbor , start , target);
                        continue;
                    }
                    neighbor.setFill(Color.BLUE);
                    cameFrom.put(neighbor, current);
                    neighbor.setG(tentativeGScore);
                    neighbor.setF(neighbor.getG() + neighbor.distance(target));

                }
            }

        }
        return Optional.empty();

    }


    private void setColor(Node neighbor , Node start , Node target)
    {
        int green = (int)((neighbor.distance(start) / start.distance(target)) * 255);
        int red = (int)((Math.abs(1 - neighbor.distance(start) / start.distance(target))) * 255);
        if (green> 255)
            green = 255;
        if(red> 255)
            red = 255;
        System.out.println(red +"|"+ green);
        GV.nodeColor = Color.rgb( red, green, 0);

        neighbor.setFill(GV.nodeColor);
    }
    public boolean isObstacle()
    {
        double ratio = Math.random()*GV.maxRatio;
        if (ratio < GV.obstacleRatio)
            return true;
        else
            return false;
    }

//    public void addEdge(Edge e)
//    {
//        Node v = e.either() , w = e.other(v);
//        v.setObstacle(false);
//        w.setObstacle(false);
//        adj.get(v).add(e);
//        adj.get(w).add(e);
//    }

    public int getV() {
        return V;
    }

    public List<Node> nodes() {
        return new ArrayList<>(adj.keySet());
    }

    public Iterable<Node> adj(Node node)
    {
        return adj.get(node);
    }

}
