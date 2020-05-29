package org.jmesh;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    public List<Vert> verts = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();
    public List<Face> faces = new ArrayList<>();
    public List<Loop> loops = new ArrayList<>();

    public Face findFace(Vert[] verts){
        if(verts.length == 0){
            return null;
        }
        if(verts[0].edge != null){

        }
        return null;
    }

    public void EdgesFromVerts(Edge[] edges, Vert[] verts) {

    }

    private Edge CreateEdge(Vert v1, Vert v2, boolean allowDouble){
        Edge e;
        if(!allowDouble && (e = Edge.findEdge(v1,v2)) != null){
            return e;
        }
        e = new Edge(v1,v2);
        return e;
    }

    public boolean FindEdges(Vert[] verts, Edge[] edges){
        int i = 0;
        int last = verts.length -1;
        for(i = 0; i < verts.length; i++){
            edges[last] = Edge.findEdge(verts[i],verts[last]);
            if(edges[last] == null){
                return false;
            }
            last = i;
        }
        return true;
    }

    public Face createFace(Vert[] vert, Edge[] edges, boolean allowDouble) throws Exception {
        if (vert.length != edges.length)
            throw new Exception("faces and edges needs to match");

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

}
