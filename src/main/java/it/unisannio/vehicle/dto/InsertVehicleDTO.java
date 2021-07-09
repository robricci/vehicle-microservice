package it.unisannio.vehicle.dto;

import java.io.Serializable;

public class InsertVehicleDTO implements Serializable {

    private String licensePlate;
    private Integer totalAvailableSeats;
    private Integer waitingTimeTarget;
    private Integer occupancyTarget;
    private Integer inertialTimeTarget;

    public InsertVehicleDTO() { }

    public InsertVehicleDTO(String licensePlate, Integer totalAvailableSeats, Integer waitingTimeTarget, Integer occupancyTarget, Integer inertialTimeTarget) {
        this.licensePlate = licensePlate;
        this.totalAvailableSeats = totalAvailableSeats;
        this.waitingTimeTarget = waitingTimeTarget;
        this.occupancyTarget = occupancyTarget;
        this.inertialTimeTarget = inertialTimeTarget;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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
