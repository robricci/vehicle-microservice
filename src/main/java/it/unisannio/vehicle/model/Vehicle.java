package it.unisannio.vehicle.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document
public class Vehicle implements Serializable {

    @Id
    private String id;
    private String licenseId;
    private Integer totalAvailableSeats;
    private Integer waitingTimeTarget;
    private Integer occupancyTarget;
    private Integer inertialTimeTarget;

    public Vehicle() {}

    public Vehicle(String id, String licenseId, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.id = id;
        this.licenseId = licenseId;
        this.totalAvailableSeats = totalAvailableSeats;
        this.waitingTimeTarget = waitingTimeTarget;
        this.occupancyTarget = occupancyTarget;
        this.inertialTimeTarget = inertialTimeTarget;
    }

    public Vehicle(String licenseId, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.licenseId = licenseId;
        this.totalAvailableSeats = totalAvailableSeats;
        this.waitingTimeTarget = waitingTimeTarget;
        this.occupancyTarget = occupancyTarget;
        this.inertialTimeTarget = inertialTimeTarget;
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

    public Integer getTotalAvailableSeats() {
        return totalAvailableSeats;
    }

    public void setTotalAvailableSeats(Integer totalAvailableSeats) {
        this.totalAvailableSeats = totalAvailableSeats;
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
}
