package it.unisannio.vehicle.dto;

import java.io.Serializable;

public class InsertVehicleDTO implements Serializable {

    private String licenseId;
    private Integer totalAvailableSeats;
    private Integer waitingTimeTarget;
    private Integer occupancyTarget;
    private Integer inertialTimeTarget;

    public InsertVehicleDTO() { }

    public InsertVehicleDTO(String licenseId, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.licenseId = licenseId;
        this.totalAvailableSeats = totalAvailableSeats;
        this.waitingTimeTarget = waitingTimeTarget;
        this.occupancyTarget = occupancyTarget;
        this.inertialTimeTarget = inertialTimeTarget;
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
