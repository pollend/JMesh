package org.jmesh;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Face {
   public int numVerts;
   public Vector3f normal = new Vector3f();
   public Loop first;

    public Face(Vector3fc normal, Loop l){
        this.normal.set(normal);
        this.first = l;
        l.f = this;
    }

    public void refreshCount(){

    }

}
