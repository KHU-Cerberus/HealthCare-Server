package cerberus.HealthCare.user.dto.report;

import cerberus.HealthCare.sleep.entity.SleepLog;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SleepLogDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private int durationMinutes;

    public static SleepLogDto toSleepLogDto(SleepLog log) {
        int duration = (int) java.time.Duration.between(log.getStart(), log.getEndTime()).toMinutes();

        return new SleepLogDto(
            log.getStart(),
            log.getEndTime(),
            duration
        );
    }
}
