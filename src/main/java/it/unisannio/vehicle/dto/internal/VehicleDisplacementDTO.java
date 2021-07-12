package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;

public class VehicleDisplacementDTO implements Serializable {

    private String licensePlate;
    private Station nextStation;

    public VehicleDisplacementDTO() { }

    public VehicleDisplacementDTO(String licensePlate, Station nextStation) {
        this.licensePlate = licensePlate;
        this.nextStation = nextStation;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public void setNextStation(Station nextStation) {
        this.nextStation = nextStation;
    }
}
