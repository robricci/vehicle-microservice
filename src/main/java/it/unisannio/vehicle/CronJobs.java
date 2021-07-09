package it.unisannio.vehicle;

import it.unisannio.vehicle.service.VehicleService;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJobs {

    private VehicleService vehicleService;

    @Autowired
    public CronJobs(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // cron = "second, minute, hour, day of month, month, day(s) of week"
    @Scheduled(cron = "0 * * * * ?", zone = "UTC")
    @SchedulerLock(name = "vehiclesDisplacementEvery24Hours", lockAtLeastFor = "15m")
    public void vehiclesDisplacementEvery24Hours() {
        LockAssert.assertLocked();
        // TODO uncomment this.vehicleService.displacement();
    }
}
