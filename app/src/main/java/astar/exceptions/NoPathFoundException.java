package astar.exceptions;

import astar.graph.Node;

public class NoPathFoundException extends Exception {
    public NoPathFoundException(Node start, Node target) {
        super(String.format("No path found from %s -> %s", start, target));
    }
}
