package cerberus.HealthCare.sleep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SleepDurationResponse {
    private long hours;
    private long minutes;
}

