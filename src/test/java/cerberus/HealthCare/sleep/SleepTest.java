package cerberus.HealthCare.sleep;

import cerberus.HealthCare.sleep.entity.SleepLog;
import cerberus.HealthCare.sleep.repository.SleepRepository;
import cerberus.HealthCare.sleep.service.SleepService;
import cerberus.HealthCare.sleep.dto.SleepDurationResponse;
import cerberus.HealthCare.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class SleepServiceTest {

    @Autowired
    private SleepService sleepService;

    @Autowired
    private SleepRepository sleepLogRepository;

    @Test
    @DisplayName("오늘 종료된 수면 시간 총합 테스트")
    void testTodaySleepCalculation() {

        log.info("===== [TEST START] 오늘 종료된 수면 시간 총합 테스트 =====");

        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        log.info("유저 생성 완료. userId={}", userId);

        // 1) 전날 23시 ~ 오늘 07시 (8시간)
        SleepLog log1 = new SleepLog();
        log1.setUser(user);
        log1.setStart(LocalDateTime.of(2025, 12, 5, 23, 0));
        log1.setEndTime(LocalDateTime.of(2025, 12, 6, 7, 0));
        sleepLogRepository.save(log1);

        log.info("수면 로그1 저장 완료: start={}, end={}",
            log1.getStart(), log1.getEndTime());

        // 2) 오늘 01시 ~ 오늘 09시 (8시간)
        SleepLog log2 = new SleepLog();
        log2.setUser(user);
        log2.setStart(LocalDateTime.of(2025, 12, 6, 1, 0));
        log2.setEndTime(LocalDateTime.of(2025, 12, 6, 9, 0));
        sleepLogRepository.save(log2);

        log.info("수면 로그2 저장 완료: start={}, end={}",
            log2.getStart(), log2.getEndTime());

        // when
        log.info("[SERVICE CALL] getTodaySleep() 호출");
        SleepDurationResponse result = sleepService.getTodaySleep(userId);

        log.info("수면 계산 결과: {}시간 {}분",
            result.getHours(), result.getMinutes());

        // then
        assertThat(result.getHours()).isEqualTo(16);
        assertThat(result.getMinutes()).isEqualTo(0);

        log.info("===== [TEST SUCCESS] 오늘 수면 총합 16시간 검증 완료 =====");
    }
}
