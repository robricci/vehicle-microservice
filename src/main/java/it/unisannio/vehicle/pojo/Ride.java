package it.unisannio.vehicle.pojo;

import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.internal.Station;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ride implements Serializable {

    private List<Station> route;
    private List<PickPoint> pickPoints;
    private Date startDate;


    public Ride() {
        this.pickPoints = new ArrayList<>();
        this.route = new ArrayList<>();
    }

    public Ride(List<Station> route) {
        this.route = route;
    }

    public Ride(List<Station> route, List<PickPoint> pickPoints, Date startDate) {
        this.route = route;
        this.pickPoints = pickPoints;
        this.startDate = startDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
