package it.unisannio.vehicle.pojo;

import it.unisannio.vehicle.model.Vehicle;

public class VehicleStatus {

    public enum Status {
        WAIT_PASSENGERS, FULL, READY_TO_START, MOVING
    }

    private Vehicle vehicle;
    private Status status;

    public VehicleStatus() {
    }

    public VehicleStatus(Status status) {
        this.status = status;
    }

    public VehicleStatus(Vehicle vehicle, Status status) {
        this.vehicle = vehicle;
        this.status = status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
