package it.unisannio.vehicle.dto;

public class PickPoint {

    public enum Status {WAIT, ONBOARDED}

    private String tripId;
    private Integer sourceNodeId;
    private Integer destinationNodeId;
    private Status status;

    public PickPoint() { }

    public PickPoint(String tripId, Integer sourceNodeId, Integer destinationNodeId) {
        this.tripId = tripId;
        this.sourceNodeId = sourceNodeId;
        this.destinationNodeId = destinationNodeId;
        this.status = Status.WAIT;
    }

    public PickPoint(Integer sourceNodeId, Integer destinationNodeId, Status status) {
        this.sourceNodeId = sourceNodeId;
        this.destinationNodeId = destinationNodeId;
        this.status = status;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Integer getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(Integer sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public Integer getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(Integer destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
