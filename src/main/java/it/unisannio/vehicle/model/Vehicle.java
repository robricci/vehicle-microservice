package it.unisannio.vehicle.model;

import it.unisannio.vehicle.dto.internal.Coordinate;
import it.unisannio.vehicle.pojo.Ride;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


@Document
public class Vehicle implements Serializable {

    // serve indicare chi sta guidando il veicolo? forse no, quindi si assume
    // che il guidatore che sale sul veicolo inserisca (ad esempio) la targa
    // e quindi viene mappato a runtime con la targa (esempio: websocket guidatore-> <Session, Vehicle>
    @Id
    private String id;
    private String licenseId;
    private Integer waitingTimeTarget;
    private Integer occupancyTarget;
    private Integer inertialTimeTarget;
    private Coordinate location;
    private Integer totalAvailableSeats;
    private Integer occupiedSeats;
    private Date creationDate;
    private Ride ride;

    public Vehicle() {
        this.creationDate = new Date();
        this.occupiedSeats = 0;
    }

    public Vehicle(String id, String licenseId, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.id = id;
        this.licenseId = licenseId;
        this.totalAvailableSeats = totalAvailableSeats;
        this.waitingTimeTarget = waitingTimeTarget;
        this.occupancyTarget = occupancyTarget;
        this.inertialTimeTarget = inertialTimeTarget;
        this.creationDate = new Date();
        this.occupiedSeats = 0;
    }

    public Vehicle(String licenseId, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.licenseId = licenseId;
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

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
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

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
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
}
