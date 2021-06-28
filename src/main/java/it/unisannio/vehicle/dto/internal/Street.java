package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;
import java.util.List;

public class Street implements Serializable {

    private Integer linkId;
    private Integer from;
    private Integer to;
    private Double length;
    private Integer speedlimit;
    private String name;
    private Double weight;
    private Double ffs;
    private List<Coordinate> coordinates;

    public Street() {
    }

    public Street(Integer linkId, Integer from, Integer to, Double length, Integer speedlimit, String name, Double weight, Double ffs, List<Coordinate> coordinates) {
        this.linkId = linkId;
        this.from = from;
        this.to = to;
        this.length = length;
        this.speedlimit = speedlimit;
        this.name = name;
        this.weight = weight;
        this.ffs = ffs;
        this.coordinates = coordinates;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getSpeedlimit() {
        return speedlimit;
    }

    public void setSpeedlimit(Integer speedlimit) {
        this.speedlimit = speedlimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getFfs() {
        return ffs;
    }

    public void setFfs(Double ffs) {
        this.ffs = ffs;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
