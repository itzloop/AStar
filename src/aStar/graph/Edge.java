package aStar.graph;

import javafx.scene.Group;
import javafx.scene.shape.Line;


public class Edge extends Line implements Comparable<Edge>{
    private final Node v , w;
    private final double weight;

    Edge(Node  v , Node w  , double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }


    public Node either() { return v; }

    public Node other(Node node)
    {
        if (this.v.equals(node))
            return w;
        else return v;
    }

    @Override
    public int compareTo(Edge that) {
        if(this.weight > that.weight ) return -1;
        else if (this.weight < that.weight) return 1;
        else return 0;
    }
}



