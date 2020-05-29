package org.jmesh.iterable;

import org.jmesh.Loop;

import java.util.Iterator;

public class LoopIterable implements Iterable<Loop> {
    private final Loop initalLoop;
    private final Direction direction;

    public LoopIterable(Loop loop, Direction direction) {
        this.initalLoop = loop;
        this.direction = direction;
    }

    public LoopIterable(Loop loop) {
        this.initalLoop = loop;
        this.direction = Direction.FORWARD;
    }

    @Override
    public Iterator<Loop> iterator() {
        return new Iterator<>() {
            private Loop iterable = initalLoop;
            private boolean hasNext = (initalLoop != null);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Loop next() {
                Loop result = iterable;
                iterable = direction == Direction.FORWARD ? result.next : result.prev;
                if (iterable == initalLoop || iterable == null) {
                    hasNext = false;
                }
                return result;
            }
        };
    }
}
