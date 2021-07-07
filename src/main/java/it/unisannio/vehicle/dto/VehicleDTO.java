package it.unisannio.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.unisannio.vehicle.dto.internal.Coordinate;
import it.unisannio.vehicle.dto.internal.Station;
import it.unisannio.vehicle.model.Vehicle;

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
    private Coordinate location;
    private Integer totalAvailableSeats;
    private Integer occupiedSeats;
    private Date creationDate;

    private Date startDate;
    private List<Station> route;
    private List<PickPoint> pickPoint;

    public VehicleDTO() { }

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licenseId = vehicle.getLicenseId();
        this.totalAvailableSeats = vehicle.getTotalAvailableSeats();
        this.waitingTimeTarget = vehicle.getWaitingTimeTarget();
        this.occupancyTarget = vehicle.getOccupancyTarget();
        this.inertialTimeTarget = vehicle.getInertialTimeTarget();
        this.location = vehicle.getLocation();
        this.creationDate = vehicle.getCreationDate();
        this.occupiedSeats = vehicle.getOccupiedSeats();

        if (vehicle.getRide() != null) {
            this.startDate = vehicle.getRide().getStartDate();
            this.route = vehicle.getRide().getRoute();
            this.pickPoint = vehicle.getRide().getPickPoints();
        }
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<Station> getRoute() {
        return route;
    }

    public void setRoute(List<Station> route) {
        this.route = route;
    }

    public List<PickPoint> getPickPoint() {
        return pickPoint;
    }

    public void setPickPoint(List<PickPoint> pickPoint) {
        this.pickPoint = pickPoint;
    }
}
