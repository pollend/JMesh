package org.jmesh;

import org.jmesh.iterable.Direction;
import org.jmesh.iterable.EdgeIterable;
import org.jmesh.iterable.LoopIterable;
import org.jmesh.iterable.LoopRadialIterable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mesh {
    public List<Vert> verts = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();
    public List<Face> faces = new ArrayList<>();
    public List<Loop> loops = new ArrayList<>();

    public Face findFace(Vert[] verts) {
        for(var ed: new EdgeIterable(verts[0])) {
            if (ed.loop != null) {
                for (var loopRadial : new LoopRadialIterable(ed.loop)) {
                    int i_walk = 2;
                    if (loopRadial.next.v == verts[1] && loopRadial.f.numVerts == verts.length) {
                        Iterator<Loop> loopIter = new LoopIterable(loopRadial.next.next).iterator();
                        while (loopIter.hasNext() && i_walk != verts.length) {
                            if (loopIter.next().v != verts[i_walk]) {
                                break;
                            }
                            i_walk++;
                        }

                    } else if (loopRadial.prev.v == verts[1]) {
                        Iterator<Loop> loopIter = new LoopIterable(loopRadial.prev.prev, Direction.BACKWARD).iterator();
                        while (loopIter.hasNext() && i_walk != verts.length) {
                            if (loopIter.next().v != verts[i_walk]) {
                                break;
                            }
                            i_walk++;
                        }
                    }
                    if (i_walk == verts.length) {
                        return loopRadial.f;
                    }
                }
            }
        }
        return null;
    }

    public Face createFace(Vert[] verts, boolean createEdges) throws Exception {
        Edge[] edges = new Edge[verts.length];
        if (createEdges)
        {
            fillEdgesFromVerts(edges, verts);
        }
        else
        {
            if (!findEdges(verts, edges))
            {
                return null;
            }
        }
        return createFace(verts,edges);
    }

    public Face createFace(Vert[] vert, Edge[] edges) throws Exception {
        if (vert.length != edges.length)
            throw new IllegalArgumentException("faces and edges needs to match");

        Loop startLoop, lastLoop, l;
        startLoop = lastLoop = new Loop(edges[0], vert[0]);
        Face face = new Face(new Vector3f(), startLoop);
        for (int i = 1; i < vert.length; i++) {
            l = new Loop(edges[i], vert[i], face);

            l.prev = lastLoop;
            lastLoop.next = l;
            lastLoop = l;
        }
        startLoop.prev = lastLoop;
        lastLoop.next = startLoop;

        face.numVerts = vert.length;
        return face;
    }


    public void killVert(Vert v){
        while (v.edge != null){
            killEdge(v.edge);
        }
    }

    public void killEdge(Edge e){
        while (e.loop != null){
            killFace(e.loop.f);
        }
        e.detachDiskEdges();
    }

    public void killFace(Face face){
        if(face.first != null){
            for(var fc: new LoopIterable(face.first)){
                fc.detachedRadialLoop(fc.e);
                face.numVerts--;
            }
        }
    }

    public void fillEdgesFromVerts(Edge[] edges, Vert[] verts) {
        int i_prev = verts.length - 1;
        for (int i = 0; i < verts.length; i++) {
            edges[i_prev] = createEdge(verts[i_prev], verts[i], false);
            i_prev = i;
        }
    }

    public boolean findEdges(Vert[] verts, Edge[] edges) {
        int i = 0;
        int last = verts.length - 1;
        for (i = 0; i < verts.length; i++) {
            edges[last] = Edge.findEdge(verts[i], verts[last]);
            if (edges[last] == null) {
                return false;
            }
            last = i;
        }
        return true;
    }

    private Edge createEdge(Vert v1, Vert v2, boolean allowDouble) {
        Edge e;
        if (!allowDouble && (e = Edge.findEdge(v1, v2)) != null) {
            return e;
        }
        e = new Edge(v1, v2);
        return e;
    }


}
