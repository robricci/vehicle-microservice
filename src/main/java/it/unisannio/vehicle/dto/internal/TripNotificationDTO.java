package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;

public class TripNotificationDTO implements Serializable {

    public enum Status { APPROVED, REJECTED }

    private String tripId;
    private String vehicleLicenseId;
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

    public TripNotificationDTO(String tripId, String vehicleLicenseId, Integer pickUpNodeId) {
        this.tripId = tripId;
        this.vehicleLicenseId = vehicleLicenseId;
        this.pickUpNodeId = pickUpNodeId;
        this.status = Status.APPROVED;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehicleLicenseId() {
        return vehicleLicenseId;
    }

    public void setVehicleLicenseId(String vehicleLicenseId) {
        this.vehicleLicenseId = vehicleLicenseId;
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
