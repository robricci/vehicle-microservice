package it.unisannio.vehicle;

import it.unisannio.vehicle.dto.internal.Station;
import it.unisannio.vehicle.dto.internal.StationStatsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, String> buildQueryMap(String query) {
        if (query == null)
            return null;

        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] currentParam = param.split("=");
            if (currentParam.length != 2)
                continue;
            String name = currentParam[0];
            String value = currentParam[1];
            map.put(name, value);
        }
        return map;
    }
}
