package it.unisannio.vehicle.model;

import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.internal.Coordinate;
import it.unisannio.vehicle.dto.internal.Station;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Document
public class VehicleRide implements Serializable {

    @Id
    private String id;
    private String vehicleId;
    private Integer availableSeats;
    // private Station location; // ultima stazione in cui è stato rilevato il veicolo
    private Coordinate location;
    private Date startDate = new Date();
    private Date endDate;
    private List<Station> route;
    // distinguere tra pickUp e pickDown
    private List<PickPoint> pickPoints;

    public VehicleRide() {}

    public VehicleRide(String id, String vehicleId, Integer availableSeats, Coordinate location, Date startDate, Date endDate, List<Station> route, List<PickPoint> pickPoints) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.availableSeats = availableSeats;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.route = route;
        this.pickPoints = pickPoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
}
