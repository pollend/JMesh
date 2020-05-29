package org.jmesh;

public class Loop {
    public Edge e;
    public Vert v;
    public Face f;

    public Loop radialNext;
    public Loop radialPrev;

    public Loop next;
    public Loop prev;

    public Loop(Edge e, Vert v, Face f){
        this.e = e;
        this.v = v;
        this.f = f;
        this.attachToRadialLoop(e);
    }

    public Loop(Edge e, Vert v){
        this.e = e;
        this.v = v;
        this.attachToRadialLoop(e);
    }

    void attachToRadialLoop(Edge e){
        if(e.loop == null){
            e.loop = this;
            radialNext = radialPrev = this;
        } else {
            radialPrev = e.loop;
            radialNext = e.loop.radialNext;

            e.loop.radialNext.radialPrev = this;
            e.loop.radialNext = this;

            e.loop = this;
        }
        this.e = e;
    }

    void detachedRadialLoop(Edge e){
        if(radialNext != this){
            if(this == e.loop) {
                e.loop = radialNext;
            }
            radialNext.radialPrev = radialPrev;
            radialPrev.radialNext = radialNext;
        } else {
            if(this == e.loop) {
                e.loop = null;
            }
        }
    }
}
