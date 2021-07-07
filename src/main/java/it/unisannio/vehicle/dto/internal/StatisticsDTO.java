package it.unisannio.vehicle.dto.internal;

import java.io.Serializable;
import java.util.List;

public class StatisticsDTO implements Serializable {

    private int allTripRequests;
    private List<RouteStatsDTO> routeStatsList;

    public StatisticsDTO() { }

    public StatisticsDTO(int allTripRequests, List<RouteStatsDTO> routeStatsList) {
        this.allTripRequests = allTripRequests;
        this.routeStatsList = routeStatsList;
    }

    public int getAllTripRequests() {
        return allTripRequests;
    }

    public void setAllTripRequests(int allTripRequests) {
        this.allTripRequests = allTripRequests;
    }

    public List<RouteStatsDTO> getRouteStatsList() {
        return routeStatsList;
    }

    public void setRouteStatsList(List<RouteStatsDTO> routeStatsList) {
        this.routeStatsList = routeStatsList;
    }
}
