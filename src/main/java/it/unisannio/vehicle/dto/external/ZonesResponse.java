package it.unisannio.vehicle.dto.external;

import java.util.List;
import java.util.Map;

public class ZonesResponse {

    private boolean success;
    private Map<Integer, List<Integer>> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<Integer, List<Integer>> getResult() {
        return result;
    }

    public void setResult(Map<Integer, List<Integer>> result) {
        this.result = result;
    }
}
