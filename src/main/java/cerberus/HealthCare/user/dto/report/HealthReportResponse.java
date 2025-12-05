package cerberus.HealthCare.user.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HealthReportResponse {
    private Long healthReportId;
    private String content;

}
