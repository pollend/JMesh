package org.jmesh.iterable;

import org.jmesh.Edge;
import org.jmesh.Vert;

import java.util.Iterator;

public class EdgeIterable implements Iterable<Edge>{
    private final Vert vert;
    private final Edge initalEdge;
    private final Direction direction;

    public EdgeIterable(Vert vert, Direction dir){
        this.vert = vert;
        this.initalEdge = vert.edge;
        this.direction = dir;
    }

    public EdgeIterable(Vert vert){
        this.vert = vert;
        this.initalEdge = vert.edge;
        this.direction = Direction.FORWARD;
    }
    @Override
    public Iterator<Edge> iterator() {
        return new Iterator<>() {
            private Edge iterable = initalEdge;
            private boolean hasNext = (initalEdge != null);
            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Edge next() {
                Edge result = iterable;
                iterable = direction == Direction.FORWARD ? result.nextEdge(vert) : result.prevEdge(vert);
                if(iterable == initalEdge || iterable == null) {
                    hasNext = false;
                }
                return result;
            }
        };
    }
}
