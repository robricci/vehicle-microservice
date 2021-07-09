package it.unisannio.vehicle.pojo;

import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.internal.Station;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ride implements Serializable {

    private Station assignedStation;
    private List<Station> route;
    private List<PickPoint> pickPoints;
    private boolean moving;
    private Date initialWaitingDate;


    public Ride() {
        this.pickPoints = new ArrayList<>();
        this.route = new ArrayList<>();
    }

    public Ride(List<Station> route) {
        this.route = route;
    }

    public Station getAssignedStation() {
        return assignedStation;
    }

    public void setAssignedStation(Station assignedStation) {
        this.assignedStation = assignedStation;
    }

    public List<Station> getRoute() {
        return route;
    }

    public void setRoute(List<Station> route) {
        this.route = route;
    }

    public List<PickPoint> getPickPoints() {
        return pickPoints;
    }

    public void setPickPoints(List<PickPoint> pickPoints) {
        this.pickPoints = pickPoints;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Date getInitialWaitingDate() {
        return initialWaitingDate;
    }

    public void setInitialWaitingDate(Date initialWaitingDate) {
        this.initialWaitingDate = initialWaitingDate;
    }
}
