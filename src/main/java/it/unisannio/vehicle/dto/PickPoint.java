package it.unisannio.vehicle.dto;

import it.unisannio.vehicle.dto.internal.Station;

public class PickPoint {

    public enum Status {WAIT, ONBOARDED, RELEASED}

    private Station source;
    private Station destination;
    private Status status;

    public PickPoint() { }

    public PickPoint(Station source, Station destination, Status status) {
        this.source = source;
        this.destination = destination;
        this.status = status;
    }

    public Station getSource() {
        return source;
    }

    public void setSource(Station source) {
        this.source = source;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
