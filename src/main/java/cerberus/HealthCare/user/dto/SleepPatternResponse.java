package cerberus.HealthCare.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SleepPatternResponse {
    private Long userId;
    private String pattern;
}
