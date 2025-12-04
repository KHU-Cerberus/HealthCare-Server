package cerberus.HealthCare.sleep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditSleepResponse {
    private Long sleepId;
    private String start;
    private String end;
}
