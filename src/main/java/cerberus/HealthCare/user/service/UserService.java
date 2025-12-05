package cerberus.HealthCare.user.service;

import cerberus.HealthCare.global.exception.CoreException;
import cerberus.HealthCare.global.exception.code.CommonErrorCode;
import cerberus.HealthCare.global.exception.code.UserErrorCode;
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
}
