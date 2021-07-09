package it.unisannio.vehicle.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document
public class Websocket implements Serializable {

    @Id
    private String id;
    private String instanceSessionId;
    private String licensePlate;

    public Websocket() {}

    public Websocket(String instanceSessionId, String licensePlate) {
        this.instanceSessionId = instanceSessionId;
        this.licensePlate = licensePlate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstanceSessionId() {
        return instanceSessionId;
    }

    public void setInstanceSessionId(String instanceSessionId) {
        this.instanceSessionId = instanceSessionId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
