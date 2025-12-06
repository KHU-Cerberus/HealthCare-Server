package cerberus.HealthCare.user.service;

import cerberus.HealthCare.global.common.enums.Status;
import cerberus.HealthCare.global.exception.CoreException;
import cerberus.HealthCare.global.exception.code.CommonErrorCode;
import cerberus.HealthCare.global.exception.code.UserErrorCode;
import cerberus.HealthCare.meal.service.MealService;
import cerberus.HealthCare.sleep.dto.SleepDurationResponse;
import cerberus.HealthCare.sleep.service.SleepService;
import cerberus.HealthCare.user.dto.HealthAdviceResponse;
import cerberus.HealthCare.user.dto.HomeResponse;
import cerberus.HealthCare.user.dto.report.HealthAnalysisResponse;
import cerberus.HealthCare.user.dto.report.HealthReportResponse;
import cerberus.HealthCare.user.dto.SleepPatternResponse;
import cerberus.HealthCare.user.entity.HealthReport;
import cerberus.HealthCare.user.entity.User;
import cerberus.HealthCare.user.repository.HealthReportRepository;
import cerberus.HealthCare.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HealthReportRepository healthReportRepository;
    private final ReportService reportService;
    private final MealService mealService;
    private final SleepService sleepService;

    @Transactional
    public SleepPatternResponse addSleepPattern(String username, String pattern) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));
        user.setSleepPattern(pattern);
        return new SleepPatternResponse(user.getId(), user.getSleepPattern());
    }

    public HealthReportResponse getHealthReport2(String username, LocalDate date) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));
        HealthReport healthReport = healthReportRepository.findByUserAndDate(user,date)
            .orElseThrow(()->new CoreException(CommonErrorCode.RESOURCE_NOT_FOUND));
        return new HealthReportResponse(healthReport.getId(), healthReport.getContent());
    }

    public HealthAnalysisResponse getHealthReport(String username, LocalDate date) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));
        HealthReport healthReport = healthReportRepository.findByUserAndDate(user,date)
            .orElseThrow(()->new CoreException(CommonErrorCode.RESOURCE_NOT_FOUND));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(healthReport.getContent(), HealthAnalysisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패", e);
        }
    }

    public HealthAdviceResponse getHealthAdvice(String username, Integer type) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));

        return new HealthAdviceResponse(user.getNickname() + "님, " + reportService.generateAdvice(user, LocalDate.now(), type));
    }

    public HomeResponse getTodayHealthStatus(String username) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));


        // 이미 구현한 메소드 활용
        long mealCount = mealService.getTodayMealCount(user.getId());
        SleepDurationResponse sleep = sleepService.getTodaySleep(user.getId());

        Status sleepStatus = evaluateSleep(sleep.getHours(), sleep.getMinutes());
        Status mealStatus = evaluateMeal(mealCount);

        return new HomeResponse(
            user.getNickname(),
            sleep.getHours(),
            sleep.getMinutes(),
            sleepStatus,
            mealCount,
            mealStatus
        );
    }

    private Status evaluateSleep(long hours, long minutes) {
        long totalMinutes = hours * 60 + minutes;

        if (totalMinutes < 5 * 60) {
            return Status.DANGER;
        } else if (totalMinutes < 7 * 60) {
            return Status.CAUTION;
        } else {
            return Status.GOOD;
        }
    }

    private Status evaluateMeal(long mealCount) {
        if (mealCount <= 1) {
            return Status.DANGER;
        } else if (mealCount == 2) {
            return Status.CAUTION;
        } else {
            return Status.GOOD;
        }
    }
}
