package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.InsertVehicleDTO;
import it.unisannio.vehicle.dto.VehicleDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleServiceTest {


    @Autowired
    private VehicleService vehicleService;

    @Before
    public void setup() {
        List<InsertVehicleDTO> vehicleDTOList = new ArrayList<>();
        InsertVehicleDTO insertVehicleDTO1 = new InsertVehicleDTO("AA123AA", 7, 120, 3, 44);
        vehicleDTOList.add(insertVehicleDTO1);
        vehicleService.insertVehicles(vehicleDTOList);

    }

    @Test
    public void getVehicleInfo() {

        VehicleDTO vehicleDTO = vehicleService.getVehicleInfo("AA123AA");

        assertEquals(vehicleDTO.getLicensePlate(),"AA123AA");

        assertEquals(vehicleDTO.getWaitingTimeTarget(), 120);
        assertEquals(vehicleDTO.getOccupancyTarget(), 3);
        assertEquals(vehicleDTO.getInertialTimeTarget(), 44);
        assertEquals(vehicleDTO.getTotalAvailableSeats(), 7);
        assertEquals(vehicleDTO.getOccupiedSeats(),0);


    }

    @Test
    public void getVehiclesInfo() {

        List<VehicleDTO> vehicleDTOList = new ArrayList<VehicleDTO>();
        vehicleDTOList = vehicleService.getVehiclesInfo();
        assertNotNull(vehicleDTOList);
    }





    @After
    public void tearDown() {
        vehicleService.removeVehicle("AA123AA");

    }


}