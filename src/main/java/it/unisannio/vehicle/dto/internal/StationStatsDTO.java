package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;

public class StationStatsDTO implements Serializable {
    private int nodeId;
    private Coordinate position;
    private int requests;

    public StationStatsDTO() {
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }
}
