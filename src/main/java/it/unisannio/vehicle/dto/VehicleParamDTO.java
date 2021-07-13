package it.unisannio.vehicle.dto;

import java.io.Serializable;

public class VehicleParamDTO implements Serializable {

    private int occupancyTarget;
    private int waitingTimeTarget;
    private int inertialTimeTarget;

    public VehicleParamDTO() { }

    public VehicleParamDTO(int occupancyTarget, int waitingTimeTarget, int inertialTimeTarget) {
        this.occupancyTarget = occupancyTarget;
        this.waitingTimeTarget = waitingTimeTarget;
        this.inertialTimeTarget = inertialTimeTarget;
    }

    public int getOccupancyTarget() {
        return occupancyTarget;
    }

    public void setOccupancyTarget(int occupancyTarget) {
        this.occupancyTarget = occupancyTarget;
    }

    public int getWaitingTimeTarget() {
        return waitingTimeTarget;
    }

    public void setWaitingTimeTarget(int waitingTimeTarget) {
        this.waitingTimeTarget = waitingTimeTarget;
    }

    public int getInertialTimeTarget() {
        return inertialTimeTarget;
    }

    public void setInertialTimeTarget(int inertialTimeTarget) {
        this.inertialTimeTarget = inertialTimeTarget;
    }
}
