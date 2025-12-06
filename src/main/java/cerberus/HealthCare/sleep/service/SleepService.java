package cerberus.HealthCare.sleep.service;

import cerberus.HealthCare.global.exception.CoreException;
import cerberus.HealthCare.global.exception.code.CommonErrorCode;
import cerberus.HealthCare.global.exception.code.UserErrorCode;
import cerberus.HealthCare.sleep.dto.CreateSleepResponse;
import cerberus.HealthCare.sleep.dto.DeleteSleepRequest;
import cerberus.HealthCare.sleep.dto.EditSleepRequest;
import cerberus.HealthCare.sleep.dto.EditSleepResponse;
import cerberus.HealthCare.sleep.dto.SleepDurationResponse;
import cerberus.HealthCare.sleep.dto.SleepLog24HResponse;
import cerberus.HealthCare.sleep.entity.SleepLog;
import cerberus.HealthCare.sleep.repository.SleepRepository;
import cerberus.HealthCare.user.entity.User;
import cerberus.HealthCare.user.repository.UserRepository;
import cerberus.HealthCare.user.service.ReportService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SleepService {

    private final SleepRepository sleepRepository;
    private final UserRepository userRepository;
    private final ReportService reportService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDateTime parse(String timeStr) {
        return LocalDateTime.parse(timeStr, FORMATTER);
    }

    @Transactional
    public CreateSleepResponse createSleep(String username, String start, String end) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));

        LocalDateTime startTime = parse(start);
        LocalDateTime endTime = parse(end);

        SleepLog sleepLog = SleepLog.builder()
            .user(user)
            .start(startTime)
            .endTime(endTime)
            .build();
        sleepRepository.save(sleepLog);

        reportService.updateReportAsync(user, endTime);

        log.info("[createSleep END] {}", Thread.currentThread().getName());
        return new CreateSleepResponse(user.getId(), sleepLog.getId(), start, end);
    }

    @Transactional
    public List<SleepLog24HResponse> getSleep24Hours(String username) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));

        LocalDateTime after = LocalDateTime.now().minusHours(24);

        List<SleepLog> logs = sleepRepository.findAllByUserAndStartAfterOrderByStartDesc(user, after);

        return logs.stream()
            .map(SleepLog24HResponse::fromEntity)
            .toList();
    }

    @Transactional
    public void deleteSleep(String username, DeleteSleepRequest request) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));
        SleepLog sleep = sleepRepository.findByIdAndUser(request.getSleepId(), user)
            .orElseThrow(() -> new CoreException(CommonErrorCode.RESOURCE_NOT_FOUND));
        LocalDateTime endTime = sleep.getEndTime();

        sleepRepository.delete(sleep);

        reportService.updateReportAsync(user, endTime);

        log.info("[deleteSleep END] {}", Thread.currentThread().getName());
    }

    @Transactional
    public EditSleepResponse editSleep(String username, EditSleepRequest request) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));
        SleepLog sleep = sleepRepository.findByIdAndUser(request.getSleepId(), user)
            .orElseThrow(() -> new CoreException(CommonErrorCode.RESOURCE_NOT_FOUND));

        sleep.setStart(parse(request.getStart()));
        sleep.setEndTime(parse(request.getEnd()));

        reportService.updateReportAsync(user, sleep.getEndTime());

        log.info("[editSleep END] {}", Thread.currentThread().getName());
        return new EditSleepResponse(request.getSleepId(), request.getStart(), request.getEnd());
    }

    public SleepDurationResponse getTodaySleep(Long userId) {

        LocalDate today = LocalDate.now();
        List<SleepLog> logs = sleepRepository.findSleepsEndedToday(userId, today);

        long totalMinutes = 0;

        for (SleepLog log : logs) {
            totalMinutes += Duration.between(log.getStart(), log.getEndTime()).toMinutes();
        }

        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return new SleepDurationResponse(hours, minutes);
    }

}
