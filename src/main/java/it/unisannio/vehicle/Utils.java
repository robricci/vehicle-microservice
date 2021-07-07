package it.unisannio.vehicle;

import it.unisannio.vehicle.dto.internal.Station;
import it.unisannio.vehicle.dto.internal.StationStatsDTO;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Station> convertStationStatsDtoListToStationList(List<StationStatsDTO> stationStatsList) {
        List<Station> stations = new ArrayList<>();
        for (StationStatsDTO stationStats : stationStatsList) {
            Station station = new Station();
            station.setNodeId(stationStats.getNodeId());
            station.setPosition(stationStats.getPosition());
            stations.add(station);
        }
        return stations;
    }
}
