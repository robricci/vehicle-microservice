package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;


public class Station implements Serializable {

    private Integer nodeId;
    private Coordinate position;

    public Station() {}

    public Station(Integer nodeId, Coordinate position) {
        this.nodeId = nodeId;
        this.position = position;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof Station)) return false;

        Station s = (Station) obj;
        return this.getNodeId().equals(s.getNodeId())
                /*&& this.getPosition() != null
                && this.getPosition().getLatitude().equals(s.getPosition().getLatitude())
                && this.getPosition().getLongitude().equals(s.getPosition().getLongitude())*/;
    }
}
