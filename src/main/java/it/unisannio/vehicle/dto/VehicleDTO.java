package it.unisannio.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.unisannio.vehicle.dto.internal.Coordinate;
import it.unisannio.vehicle.dto.internal.Station;
import it.unisannio.vehicle.model.Vehicle;
import it.unisannio.vehicle.model.VehicleRide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDTO implements Serializable {

    private String id;
    private String licenseId;
    private Integer waitingTimeTarget;
    private Integer occupancyTarget;
    private Integer inertialTimeTarget;
    private Integer totalAvailableSeats;
    private Integer availableSeats;
    private Coordinate location;
    private Date startDate;
    private Date endDate;
    private List<Station> route;
    private List<Integer> pickPoint;

    public VehicleDTO() { }

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licenseId = vehicle.getLicenseId();
        this.totalAvailableSeats = vehicle.getTotalAvailableSeats();
        this.waitingTimeTarget = vehicle.getWaitingTimeTarget();
        this.occupancyTarget = vehicle.getOccupancyTarget();
        this.inertialTimeTarget = vehicle.getInertialTimeTarget();
    }

    public VehicleDTO(Vehicle vehicle, VehicleRide vehicleRide) {
        this.id = vehicle.getId();
        this.licenseId = vehicle.getLicenseId();
        this.totalAvailableSeats = vehicle.getTotalAvailableSeats();
        this.waitingTimeTarget = vehicle.getWaitingTimeTarget();
        this.occupancyTarget = vehicle.getOccupancyTarget();
        this.inertialTimeTarget = vehicle.getInertialTimeTarget();
        this.availableSeats = vehicleRide.getAvailableSeats();
        this.location = vehicleRide.getLocation();
        this.startDate = vehicleRide.getStartDate();
        this.endDate = vehicleRide.getEndDate();
        this.route = vehicleRide.getRoute();
        this.pickPoint = vehicleRide.getPickPoint();
    }

    public static List<VehicleDTO> convert(List<Vehicle> vehicleList) {
        List<VehicleDTO> result = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            result.add(new VehicleDTO(vehicle));
        }
        return result;
    }

    public static VehicleDTO convert(Vehicle vehicle) {
        return new VehicleDTO(vehicle);
    }

    public static VehicleDTO convert(Vehicle vehicle, VehicleRide vehicleRide) {
        return new VehicleDTO(vehicle, vehicleRide);
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

    public Integer getTotalAvailableSeats() {
        return totalAvailableSeats;
    }

    public void setTotalAvailableSeats(Integer totalAvailableSeats) {
        this.totalAvailableSeats = totalAvailableSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Station> getRoute() {
        return route;
    }

    public void setRoute(List<Station> route) {
        this.route = route;
    }

    public List<Integer> getPickPoint() {
        return pickPoint;
    }

    public void setPickPoint(List<Integer> pickPoint) {
        this.pickPoint = pickPoint;
    }
}
