package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.internal.Coordinate;
import it.unisannio.vehicle.dto.internal.Intersection;
import it.unisannio.vehicle.dto.internal.Street;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "trafficServiceFeignClient", url = "${api.external.base-url}")
public interface TrafficService {

    @GetMapping("/intersections/{osmid}")
    Intersection getIntersections(@PathVariable Integer osmid);

    @GetMapping("/intersections/nearest")
    Intersection getIntersectionsNearest(@RequestParam Double latitude, @RequestParam Double longitude);

    @GetMapping("/streets/{linkId}")
    Street getStreetsById(@PathVariable Integer linkId);

    @GetMapping("/streets")
    Street getStreets(@RequestParam Integer osmidStart, @RequestParam Integer asmidDest);

    @GetMapping("/shortestPaths")
    List<Coordinate> shortestPath(@RequestParam Integer source, @RequestParam Integer destination);

    @GetMapping("/shortestPaths")
    List<Coordinate> shortestPath(@RequestParam Integer source, @RequestParam Integer destination,
                                  @RequestParam Integer type);
}
