package astar.graph;

import astar.Constants;
import astar.exceptions.AlreadyRunningException;
import astar.exceptions.NoPathFoundException;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Graph {
    private final int vertecies, width, height;
    private final Map<Node, LinkedHashSet<Node>> adj;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public Graph(int width, int height) {
        this.vertecies = width * height;
        this.width = width;
        this.height = height;
        adj = new LinkedHashMap<>();
        init(this.width, this.height);
    }

    private void init(int width, int height) {
        if (!adj.isEmpty()) {
            for (var node : this.nodes()) {
                node.setF(Double.MAX_VALUE);
                node.setG(Double.MAX_VALUE);
                node.setObstacle(isObstacle());
            }

            return;
        }

        Node[][] nodes = new Node[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Node node = new Node(adj.size(), j + 1, i + 1, isObstacle());
                node.setF(Double.MAX_VALUE);
                node.setG(Double.MAX_VALUE);
                adj.put(node, new LinkedHashSet<>());
                nodes[i][j] = node;
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Node node = nodes[i][j];
                try {
                    adj.get(node).add(nodes[i + 1][j + 1]);
                } catch (Exception e) {
                }
                try {
                    adj.get(node).add(nodes[i - 1][j - 1]);
                } catch (Exception e) {
                }
                try {
                    adj.get(node).add(nodes[i - 1][j + 1]);
                } catch (Exception e) {
                }
                try {
                    adj.get(node).add(nodes[i + 1][j - 1]);
                } catch (Exception e) {
                }
                try {
                    adj.get(node).add(nodes[i + 1][j]);
                } catch (Exception e) {
                }
                try {
                    adj.get(node).add(nodes[i - 1][j]);
                } catch (Exception e) {
                }
                try {
                    adj.get(node).add(nodes[i][j + 1]);
                } catch (Exception e) {
                }
                try {
                    adj.get(node).add(nodes[i][j - 1]);
                } catch (Exception e) {
                }
            }
        }

    }

    public void ReArrange() {
        init(this.width, this.height);
    }

    public void Clear() {
        if (!adj.isEmpty()) {
            for (var node : this.nodes()) {
                node.setF(Double.MAX_VALUE);
                node.setG(Double.MAX_VALUE);
                node.setObstacle(node.isObstacle());
            }

            return;
        }

        init(width, height);

    }

    private Stack<Node> drawPath(Map<Node, Node> cameFrom, Node current) {
        Stack<Node> totalPath = new Stack<>();
        totalPath.push(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.push(current);
        }
        return totalPath;

    }

    class AStartNodeWrapper {
        private Node node;
        private boolean isBlue;

        AStartNodeWrapper(Node node, boolean isBlue) {
            this.node = node;
            this.isBlue = isBlue;
        }
    }

    public Stack<Node> AStar(Node start, Node target) throws AlreadyRunningException, NoPathFoundException {
        if (this.isRunning.getAndSet(true)) {
            throw new AlreadyRunningException("AStar");
        }

        try {
            Queue<Node> open = new PriorityQueue<>();
            Queue<Node> close = new PriorityQueue<>();
            Map<Node, Node> cameFrom = new HashMap<>();
            var nodesToColor = new ArrayList<AStartNodeWrapper>();

            double maxDistance = 0;
            open.add(start);
            start.setG(0);
            target.setFill(Color.GREEN);
            start.setF(start.distance(target));
            Node current;
            while (!open.isEmpty()) {
                current = open.poll();
                maxDistance = Math.max(current.distance(target), maxDistance);
                if (current.equals(target)) {
                    System.out.println("Done");
                    var path = drawPath(cameFrom, current);

                    for (var node : nodesToColor) {
                        try {
                            if (node.isBlue) {
                                node.node.setFill(Color.BLUE);
                            } else {
                                setColor(node.node, start, target, maxDistance);
                            }

                            Thread.sleep(Constants.SleepDelay);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }

                    return path;
                }
                close.add(current);
                for (Node neighbor : adj.get(current)) {

                    if (!neighbor.isObstacle()) {
                        if (close.contains(neighbor)) {
                            // setColor(neighbor, start, target);
                            nodesToColor.add(new AStartNodeWrapper(neighbor, false));
                            continue;
                        }
                        double tentativeGScore = current.getG() + current.distance(neighbor);
                        if (!open.contains(neighbor))
                            open.add(neighbor);
                        else if (tentativeGScore >= neighbor.getG()) {
                            // setColor(neighbor, start, target);
                            nodesToColor.add(new AStartNodeWrapper(neighbor, false));
                            continue;
                        }
                        // neighbor.setFill(Color.BLUE);
                        nodesToColor.add(new AStartNodeWrapper(neighbor, true));
                        cameFrom.put(neighbor, current);
                        neighbor.setG(tentativeGScore);
                        neighbor.setF(neighbor.getG() + neighbor.distance(target));
                    }
                }
            }

            // return Optional.empty();
            throw new NoPathFoundException(start, target);
        } finally {
            this.isRunning.set(false);
        }

    }

    class DijkstraNodeWrapper {
        private Node node;
        private boolean isWhite;

        DijkstraNodeWrapper(Node node, boolean isWhite) {
            this.node = node;
            this.isWhite = isWhite;
        }
    }

    public List<Node> dijkstra(Node start, Node target) throws AlreadyRunningException, NoPathFoundException {
        if (this.isRunning.getAndSet(true)) {
            throw new AlreadyRunningException("dijkstra");
        }

        try {
            Map<Node, Double> distTo = new HashMap<>();
            Map<Node, Node> edgeTo = new HashMap<>();
            var nodesToColor = new ArrayList<DijkstraNodeWrapper>();
            double maxDistance = 0;

            Queue<Node> nodes = new PriorityQueue<>((n1, n2) -> {
                if (distTo.get(n1) > distTo.get(n2))
                    return 1;
                else if (distTo.get(n1) < distTo.get(n2))
                    return -1;
                else
                    return 0;
            });
            for (Node node : nodes()) {
                distTo.put(node, Double.MAX_VALUE);
            }
            distTo.put(start, 0d);
            nodes.add(start);

            while (!nodes.isEmpty()) {
                Node current = nodes.poll();

                // System.out.println(nodes.size());
                if (current.equals(target)) {
                    System.out.println("Done");
                    // color everything
                    for (var nodeWrapper : nodesToColor) {
                        try {
                            if (nodeWrapper.isWhite) {
                                nodeWrapper.node.setFill(Color.WHITE);
                            } else {
                                setColor(nodeWrapper.node, start, target, maxDistance);
                            }

                            Thread.sleep(Constants.SleepDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }

                    List<Node> path = new ArrayList<>();
                    path.add(current);
                    while (edgeTo.containsKey(current)) {
                        current = edgeTo.get(current);
                        path.add(current);
                    }
                    for (Node node : path) {
                        node.setFill(Color.RED);
                    }
                    return path;
                }
                for (Node node : adj.get(current)) {
                    if (!nodes.contains(node) && !node.isObstacle()) {

                        double newDist = distTo.get(current) + node.distance(current);
                        maxDistance = Math.max(current.distance(target), maxDistance);
                        if (newDist < distTo.get(node)) {
                            nodesToColor.add(new DijkstraNodeWrapper(node, false));
                            // setColor(node, start, target);
                            distTo.put(node, newDist);
                            edgeTo.put(node, current);
                            if (nodes.contains(node)) {
                                nodesToColor.add(new DijkstraNodeWrapper(node, true));
                                // node.setFill(Color.WHITE);
                                nodes.remove(node);
                            } else
                                nodes.add(node);
                        }

                    }
                }

            }

            throw new NoPathFoundException(start, target);
        } finally {
            this.isRunning.set(false);
        }
    }

    private void setColor(Node neighbor, Node start, Node target, double maxDistance) {
        double green, red;

        red = neighbor.distance(target) / maxDistance;
        green = Math.abs(1 - red) * 255;
        red = red * 255;

        if (green > 255)
            green = 255;
        if (red > 255)
            red = 255;

        neighbor.setFill(Color.rgb((int) red, (int) green, 0));
    }

    public boolean isObstacle() {
        double ratio = Math.random() * Constants.maxRatio;
        if (ratio < Constants.obstacleRatio)
            return true;
        else
            return false;
    }

    public int getVertecies() {
        return vertecies;
    }

    public List<Node> nodes() {
        return new ArrayList<>(adj.keySet());
    }

    public Iterable<Node> adj(Node node) {
        return adj.get(node);
    }

}
