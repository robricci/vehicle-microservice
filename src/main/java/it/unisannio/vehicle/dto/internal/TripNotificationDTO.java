package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;

public class TripNotificationDTO implements Serializable {

    public enum Status { APPROVED, REJECTED }

    private String tripId;
    private String vehicleLicensePlate;
    private Integer pickUpNodeId;

    private Status status;

    public TripNotificationDTO() { }

    public TripNotificationDTO(Status status) {
        this.status = status;
    }

    public TripNotificationDTO(String tripId, Status status) {
        this.tripId = tripId;
        this.status = status;
    }

    public TripNotificationDTO(String tripId, String vehicleLicensePlate, Integer pickUpNodeId) {
        this.tripId = tripId;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.pickUpNodeId = pickUpNodeId;
        this.status = Status.APPROVED;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    public Integer getPickUpNodeId() {
        return pickUpNodeId;
    }

    public void setPickUpNodeId(Integer pickUpNodeId) {
        this.pickUpNodeId = pickUpNodeId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isRejected() {
        return this.status != null && this.status.equals(Status.REJECTED);
    }

    public boolean isApproved() {
        return this.status != null && this.status.equals(Status.APPROVED);
    }
}
