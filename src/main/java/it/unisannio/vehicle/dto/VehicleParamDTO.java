package it.unisannio.vehicle.dto;

import java.io.Serializable;

public class VehicleParamDTO implements Serializable {

    private int occupancyTarget;
    private int waitingTimeTarget;
    private int inertialTime;

    public VehicleParamDTO() { }

    public VehicleParamDTO(int occupancyTarget, int waitingTimeTarget, int inertialTime) {
        this.occupancyTarget = occupancyTarget;
        this.waitingTimeTarget = waitingTimeTarget;
        this.inertialTime = inertialTime;
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

    public int getInertialTime() {
        return inertialTime;
    }

    public void setInertialTime(int inertialTime) {
        this.inertialTime = inertialTime;
    }
}
