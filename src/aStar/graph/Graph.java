package aStar.graph;


import aStar.GV;
import javafx.application.Platform;
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

    private Stack<Node> drawPath(Map<Node , Node> cameFrom , Node current , Node Start){
        Stack<Node> totalPath = new Stack<>();
        while (cameFrom.containsKey(current) )
        {
            totalPath.push(current);
            System.out.println(current);
            current = cameFrom.get(current);
        }
        totalPath.push(Start);
        return totalPath;

    }
    public Optional<Stack<Node>> AStar(Node start , Node target )
    {
        Queue<Node> open = new PriorityQueue<>();
        Queue<Node> close = new PriorityQueue<>();
        Map<Node , Node> cameFrom = new HashMap<>();
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
            //TODO Fix this later
            if (current.equals(target))
            {
                System.out.println("Done");
                return Optional.ofNullable(drawPath(cameFrom , current , start));

            }
            close.add(current);
            for (Node neighbor : adj.get(current))
            {
                if(!neighbor.isObstacle())
                {
                    if(close.contains(neighbor))
                    {
                        //neighbor.setFill(Color.WHITE);
                        continue;
                    }
                    double tentativeGScore= current.getG() + current.distance(neighbor);
                    if(!open.contains(neighbor))
                        open.add(neighbor);
                    else if(tentativeGScore >= neighbor.getG())
                    {
                        //neighbor.setFill(Color.WHITE);
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
