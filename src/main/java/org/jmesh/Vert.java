package org.jmesh;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Vert {
    public Vector3f pos = new Vector3f();
    public Edge edge;

    public Vert(Vector3fc v){
        pos.set(v);
    }

    public Vert(float x, float y, float z) {
        this.pos.set(x, y, z);
    }
}
