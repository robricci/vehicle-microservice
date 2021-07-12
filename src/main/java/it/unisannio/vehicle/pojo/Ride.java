package it.unisannio.vehicle.pojo;

import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.internal.Station;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ride implements Serializable {

    private static final long serialVersionUID = 6744782914717822071L;

    private Station currentStation;
    private List<Station> route;
    private List<PickPoint> pickPoints;
    private boolean moving;
    private Date initialWaitingDate;

    public Ride() {
        this.pickPoints = new ArrayList<>();
        this.route = new ArrayList<>();
        this.initialWaitingDate = new Date();
        this.moving = false;
    }

    public Ride(List<Station> route) {
        this.pickPoints = new ArrayList<>();
        this.route = route;
        this.initialWaitingDate = new Date();
        this.moving = false;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
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
