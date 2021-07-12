package it.unisannio.vehicle.controller;


import it.unisannio.vehicle.dto.InsertVehicleDTO;
import it.unisannio.vehicle.dto.VehicleDTO;
import it.unisannio.vehicle.dto.VehicleParamDTO;
import it.unisannio.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/vehicles")
public class VehicleController {

    private VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GET
    public Response getVehiclesInfo() {
        List<VehicleDTO> vehicles = this.vehicleService.getVehiclesInfo();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/{id}")
    public Response getVehicleInfo(@PathParam(value = "id") String licensePlate) {
        VehicleDTO vehicleInfo = this.vehicleService.getVehicleInfo(licensePlate);
        return ((vehicleInfo != null) ? Response.ok(vehicleInfo) : Response.noContent()).build();
    }

    @POST
    public Response insertVehicles(List<InsertVehicleDTO> vehicleList) {
        this.vehicleService.insertVehicles(vehicleList);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteVehicle(@PathParam(value = "id") String licensePlate) {
        boolean isRemoved = this.vehicleService.removeVehicle(licensePlate);
        return Response.ok(isRemoved).build();
    }

    @POST
    @Path("/{id}/configuration")
    public Response settingParams(@PathParam(value = "id") String licensePlate, VehicleParamDTO vehicleParam) {
        this.vehicleService.settingParams(licensePlate, vehicleParam);
        return Response.ok().build();
    }
}
