package it.unisannio.vehicle.dto.external;

import java.io.Serializable;
import java.util.List;

public class PredictionZonesResponse implements Serializable {

    private boolean success;
    private List<Prediction> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Prediction> getResult() {
        return result;
    }

    public void setResult(List<Prediction> result) {
        this.result = result;
    }
}
