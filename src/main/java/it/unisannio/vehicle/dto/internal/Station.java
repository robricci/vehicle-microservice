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
}
