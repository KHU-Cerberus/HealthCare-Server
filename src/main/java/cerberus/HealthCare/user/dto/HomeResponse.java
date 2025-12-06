package cerberus.HealthCare.user.dto;

import cerberus.HealthCare.global.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeResponse {
    private String nickname;
    private long sleepHours;
    private long sleepMinutes;
    private Status sleepStatus;
    private long mealCount;
    private Status mealStatus;

}
