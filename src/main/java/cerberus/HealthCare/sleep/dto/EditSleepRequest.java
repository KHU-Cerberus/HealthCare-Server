package cerberus.HealthCare.sleep.dto;

import lombok.Data;

@Data
public class EditSleepRequest {
    private Long sleepId;
    private String start;
    private String end;
}
