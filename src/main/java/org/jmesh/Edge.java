package org.jmesh;

public class Edge {
    public static class JEdgeDisk {
        public Edge prev;
        public Edge next;

    }
    public Loop loop = null;

    public Vert v1 = null;
    public Vert v2 = null;

    public JEdgeDisk diskV1 = new JEdgeDisk();
    public JEdgeDisk diskV2 = new JEdgeDisk();


    public Edge(Vert v1, Vert v2) {
        this.v1 = v1;
        this.v2 = v2;
        this.attachDiskEdge(this.v1);
        this.attachDiskEdge(this.v2);
    }

    private JEdgeDisk getDiskLinkFromVert(Vert v){
        if(v == v2){
            return diskV2;
        }
        return diskV1;
    }

    public static Edge findEdge(Vert v1, Vert v2){
        Edge edgeV1, edgeV2;
        if ((edgeV1 = v1.edge) != null && (edgeV2 = v2.edge) != null) {
            Edge edgeV1Iter = edgeV1, edgeV2Iter = edgeV2;
            do {
                if (edgeV1Iter.isVertInEdge(v2)) {
                    return edgeV1Iter;
                }
                if (edgeV2Iter.isVertInEdge(v1)) {
                    return edgeV2Iter;
                }
            } while ((edgeV1Iter = edgeV1Iter.nextEdge(v1)) != edgeV1 && (edgeV2Iter = edgeV2Iter.nextEdge(v2)) != edgeV2);
        }
        return null;
    }

    public boolean isVertInEdge(Vert vert){
        return this.v1 == vert || this.v2 == vert;
    }

    public boolean isVertsInEdge(Vert v1, Vert v2) {
        return (this.v1 == v1 && this.v2 == v2) || (this.v1 == v2 && this.v2 == v1);
    }

    public Edge nextEdge(Vert v){
        return getDiskLinkFromVert(v).next;
    }

    public Edge prevEdge(Vert v){
        return getDiskLinkFromVert(v).prev;
    }

    private void attachDiskEdge(Vert v){
        if(v.edge == null){
            JEdgeDisk dl1 = this.getDiskLinkFromVert(v);
            v.edge = this;
            dl1.next = dl1.prev = this;
        } else {
            JEdgeDisk dl1 = this.getDiskLinkFromVert(v);
            JEdgeDisk dl2 = v.edge.getDiskLinkFromVert(v);
            JEdgeDisk dl3 = dl2.prev != null ? dl1.prev.getDiskLinkFromVert(v) : null;
            dl1.next = v.edge;
            dl1.prev = dl2.prev;

            dl2.prev = this;
            if(dl3 != null){
                dl3.next = this;
            }
        }
    }

    private void detachDiskEdge(Vert v){
        JEdgeDisk dl1, dl2;
        dl1 = getDiskLinkFromVert(v);
        if(dl1.prev != null){
            dl2 = dl1.prev.getDiskLinkFromVert(v);
            dl2.next = dl1.next;
        }
        if(dl1.next != null) {
            dl2 = dl1.next.getDiskLinkFromVert(v);
            dl2.prev = dl1.prev;
        }

        if(v.edge == this) {
            v.edge = (this != dl1.next) ? dl1.next : null;
        }

        dl1.next = dl1.prev = null;
    }

    private boolean swapDiskVert(Vert src, Vert dest) {
        if (v1 == src) {
            v1 = dest;
            diskV1.next = diskV1.prev = null;
            return true;
        }

        if (v2 == src) {
            v2 = dest;
            diskV2.next = diskV2.prev = null;
            return true;
        }
        return false;
    }

    public void swapEdgeVert(Vert src, Vert dest){
        if (loop != null)
        {
            Loop iter, first;
            iter = first = loop;
            do
            {
                if (iter.v == src)
                {
                    iter.v = dest;
                }
                else if (iter.radialNext.v == src)
                {
                    iter.radialNext.v = dest;
                }

            } while ((iter = iter.radialNext) != first);
        }

        diskVertReplace(dest, src);
    }

    public void diskVertReplace(Vert dest, Vert src) {
        detachDiskEdge(src);
        swapDiskVert(src, dest);
        attachDiskEdge(dest);
    }

}
