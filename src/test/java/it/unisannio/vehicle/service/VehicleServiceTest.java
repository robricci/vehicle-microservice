package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.InsertVehicleDTO;
import it.unisannio.vehicle.dto.VehicleDTO;
import it.unisannio.vehicle.dto.VehicleParamDTO;
import it.unisannio.vehicle.service.VehicleService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class VehicleServiceTest {


    @Autowired
    private VehicleService vehicleService;

    @BeforeEach
    public void setup() {
        List<InsertVehicleDTO> vehicleDTOList = new ArrayList<>();
        InsertVehicleDTO insertVehicleDTO1 = new InsertVehicleDTO("AA123AA", 7, 120, 3, 44);
        InsertVehicleDTO insertVehicleDTO2 = new InsertVehicleDTO("RR321AA", 7, 110, 5, 66);
        InsertVehicleDTO insertVehicleDTO3 = new InsertVehicleDTO("AW324DF", 7, 90, 4, 88);

        vehicleDTOList.add(insertVehicleDTO1);
        vehicleDTOList.add(insertVehicleDTO2);
        vehicleDTOList.add(insertVehicleDTO3);

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

        List<VehicleDTO> allVehicleDTOList;
        allVehicleDTOList = vehicleService.getVehiclesInfo();
        assertNotNull(allVehicleDTOList);
    }

    @Test
    public void settingParams() {

        //BlackBox
        VehicleParamDTO vehicleParamDTO = new VehicleParamDTO(4, 55, 100);
        vehicleService.settingParams("AA123AA", vehicleParamDTO );
        VehicleDTO vehicleDTO = vehicleService.getVehicleInfo("AA123AA");
        assertEquals(vehicleDTO.getOccupancyTarget(), 4);
        assertEquals(vehicleDTO.getWaitingTimeTarget(), 55);
        assertEquals(vehicleDTO.getInertialTimeTarget(), 100);

        //White Box - Vehicle not found
        vehicleParamDTO = new VehicleParamDTO(4, 55, 100);
        vehicleService.settingParams("AD221QA", vehicleParamDTO);

    }

    @Test
    public void removeVehicle() {
        boolean flag = vehicleService.removeVehicle("AA123AA");
        assertTrue(flag);
        assertNull(vehicleService.getVehicleInfo("AA123AA"));
    }


    @AfterEach
    public void tearDown() {
        vehicleService.removeVehicle("AA123AA");
        vehicleService.removeVehicle("RR321AA");
        vehicleService.removeVehicle("AW324DF");

    }


}