package it.unisannio.vehicle.model;

import it.unisannio.vehicle.dto.PickPoint;
import it.unisannio.vehicle.dto.internal.Station;
import it.unisannio.vehicle.pojo.Ride;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


@Document
public class Vehicle implements Serializable {

    @Id
    private String id;
    private String licensePlate;
    private Integer waitingTimeTarget;
    private Integer occupancyTarget;
    private Integer inertialTimeTarget;
    private Station lastKnownStation;
    private Integer totalAvailableSeats;
    private Integer occupiedSeats;
    private Date creationDate;
    private Ride ride;

    public Vehicle() {
        this.creationDate = new Date();
        this.occupiedSeats = 0;
    }

    public Vehicle(String id, String licensePlate, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.totalAvailableSeats = totalAvailableSeats;
        this.waitingTimeTarget = waitingTimeTarget;
        this.occupancyTarget = occupancyTarget;
        this.inertialTimeTarget = inertialTimeTarget;
        this.creationDate = new Date();
        this.occupiedSeats = 0;
    }

    public Vehicle(String licensePlate, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.licensePlate = licensePlate;
        this.totalAvailableSeats = totalAvailableSeats;
        this.waitingTimeTarget = waitingTimeTarget;
        this.occupancyTarget = occupancyTarget;
        this.inertialTimeTarget = inertialTimeTarget;
        this.creationDate = new Date();
        this.occupiedSeats = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getWaitingTimeTarget() {
        return waitingTimeTarget;
    }

    public void setWaitingTimeTarget(Integer waitingTimeTarget) {
        this.waitingTimeTarget = waitingTimeTarget;
    }

    public Integer getOccupancyTarget() {
        return occupancyTarget;
    }

    public void setOccupancyTarget(Integer occupancyTarget) {
        this.occupancyTarget = occupancyTarget;
    }

    public Integer getInertialTimeTarget() {
        return inertialTimeTarget;
    }

    public void setInertialTimeTarget(Integer inertialTimeTarget) {
        this.inertialTimeTarget = inertialTimeTarget;
    }

    public Station getLastKnownStation() {
        return lastKnownStation;
    }

    public void setLastKnownStation(Station lastKnownStation) {
        this.lastKnownStation = lastKnownStation;
    }

    public Integer getTotalAvailableSeats() {
        return totalAvailableSeats;
    }

    public void setTotalAvailableSeats(Integer totalAvailableSeats) {
        this.totalAvailableSeats = totalAvailableSeats;
    }

    public Integer getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOccupiedSeats(Integer occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public void incrementOccupiedSeat() {
        this.occupiedSeats++;
    }

    public void decrementOccupiedSeat() {
        this.occupiedSeats--;
    }

    public int getAvailableSeats() {
        return this.getTotalAvailableSeats() - this.getOccupiedSeats();
    }

    public void addPickPoint(PickPoint pickPoint) {
        this.ride.getPickPoints().add(pickPoint);
        this.incrementOccupiedSeat();
    }

    public boolean isOccupancyTargetReached() {
        return this.occupiedSeats >= this.occupancyTarget;
    }
}
