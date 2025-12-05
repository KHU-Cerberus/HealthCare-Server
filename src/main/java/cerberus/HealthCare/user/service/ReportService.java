package cerberus.HealthCare.user.service;

import cerberus.HealthCare.meal.entity.Meal;
import cerberus.HealthCare.meal.repository.MealRepository;
import cerberus.HealthCare.openAI.ChatGPT;
import cerberus.HealthCare.sleep.entity.SleepLog;
import cerberus.HealthCare.sleep.repository.SleepRepository;
import cerberus.HealthCare.user.dto.report.HealthAnalysisResponse;
import cerberus.HealthCare.user.dto.report.MealDto;
import cerberus.HealthCare.user.dto.report.SleepLogDto;
import cerberus.HealthCare.user.entity.HealthReport;
import cerberus.HealthCare.user.entity.User;
import cerberus.HealthCare.user.repository.HealthReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final ChatGPT chatGPT;
    private final HealthReportRepository healthReportRepository;
    private final MealRepository mealRepository;
    private final SleepRepository sleepRepository;


    public String buildPrompt(List<SleepLogDto> sleeps, List<MealDto> meals) {

        return """
                당신은 건강 데이터 분석 전문가입니다.
                아래 제공되는 "수면 정보"와 "식사 정보"를 기반으로 사용자의 건강 상태를 분석하세요.

                분석 규칙:
                1. 위험성이 증가한 질환 2개 + 각 질환의 원인 2개.
                2. 위험성이 감소한 질환 2개 + 각 질환의 원인 2개.
                3. 부족 영양소 1~2개 + 이를 채우기 좋은 음식 3가지.
                4. "반드시 JSON만 출력하라. 코드블록(```json 또는 ```)을 절대 포함하지 마라."
            

                JSON 형식:
                {
                  "increasedDiseases": [
                    { "name": "", "causes": ["", ""] },
                    { "name": "", "causes": ["", ""] }
                  ],
                  "decreasedDiseases": [
                    { "name": "", "causes": ["", ""] },
                    { "name": "", "causes": ["", ""] }
                  ],
                  "nutrientDeficiency": {
                    "nutrients": ["", ""],
                    "recommendedFoods": ["", "", ""]
                  }
                }
                """
            + "\n\n[수면 정보]\n" + sleeps.toString()
            + "\n\n[식사 정보]\n" + meals.toString();
    }

    @Async
    public void updateReportAsync(User user, LocalDateTime end){
        log.info("[ASYNC START] {}", Thread.currentThread().getName());
        LocalDate date = end.toLocalDate();
        String report = generateDailyReport(user, date);

        HealthReport healthReport = healthReportRepository.findByUserAndDate(user, date)
            .orElse(new HealthReport(date, user));
        healthReport.setContent(report);
        healthReportRepository.save(healthReport);
    }


    public String generateDailyReport(User user, LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Meal> meals = mealRepository.findByUserIdAndEatTimeBetween(user.getId(), start, end);
        List<SleepLog> sleeps = sleepRepository.findByUserIdAndStartBetween(user.getId(), start, end);

        List<MealDto> mealDtos = meals.stream().map(MealDto::toMealDto).toList();
        List<SleepLogDto> sleepDtos = sleeps.stream().map(SleepLogDto::toSleepLogDto).toList();

        String prompt = buildPrompt(sleepDtos, mealDtos);

        // GPT 호출
        return chatGPT.getCompletionMessageBlocking(prompt);


//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.readValue(json, HealthAnalysisResponse.class);
//        } catch (Exception e) {
//            throw new RuntimeException("GPT 응답 파싱 실패", e);
//        }
    }

}
