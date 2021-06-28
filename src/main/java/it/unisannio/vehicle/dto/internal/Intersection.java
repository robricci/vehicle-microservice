package it.unisannio.vehicle.dto.internal;

import java.util.List;

public class Intersection {

    private Integer osmid;
    private Coordinate coordinate;
    private List<Street> streets;

    public Intersection() {
    }

    public Intersection(Integer osmid, Coordinate coordinate, List<Street> streets) {
        this.osmid = osmid;
        this.coordinate = coordinate;
        this.streets = streets;
    }

    public Integer getOsmid() {
        return osmid;
    }

    public void setOsmid(Integer osmid) {
        this.osmid = osmid;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }
}
