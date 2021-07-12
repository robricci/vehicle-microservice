package it.unisannio.vehicle;

import it.unisannio.vehicle.service.MovingService;
import it.unisannio.vehicle.service.VehicleService;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJobs {

    private MovingService movingService;

    @Autowired
    public CronJobs(MovingService movingService) {
        this.movingService = movingService;
    }

    // cron = "second, minute, hour, day of month, month, day(s) of week"
    @Scheduled(cron = "0 * * * * ?", zone = "UTC")
    @SchedulerLock(name = "vehiclesDisplacementEvery24Hours", lockAtLeastFor = "15m")
    public void vehiclesDisplacementEvery24Hours() {
        LockAssert.assertLocked();
        // TODO uncomment
        // this.movingService.displacement();
    }

    @Scheduled(cron = "0 3 * * * ?", zone = "UTC")
    @SchedulerLock(name = "checkVehicleTemporalParameters", lockAtLeastFor = "2m")
    public void checkVehicleTemporalParameters() {
        LockAssert.assertLocked();
        this.movingService.checkVehicleTemporalParameters();
    }
}
