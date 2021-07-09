package it.unisannio.vehicle.dto;

import it.unisannio.vehicle.dto.internal.Station;

import java.io.Serializable;

public class NextStationRequestDTO implements Serializable {

    private Station currentStation;

    public NextStationRequestDTO() {
    }

    public NextStationRequestDTO(Station currentStation) {
        this.currentStation = currentStation;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }
}
