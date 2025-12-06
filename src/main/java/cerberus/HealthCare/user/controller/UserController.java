package cerberus.HealthCare.user.controller;

import cerberus.HealthCare.global.common.BaseResponse;
import cerberus.HealthCare.global.security.CustomUserDetails;
import cerberus.HealthCare.user.dto.HealthAdviceResponse;
import cerberus.HealthCare.user.dto.HomeResponse;
import cerberus.HealthCare.user.dto.report.HealthAnalysisResponse;
import cerberus.HealthCare.user.dto.report.HealthReportResponse;
import cerberus.HealthCare.user.dto.SleepPatternRequest;
import cerberus.HealthCare.user.dto.SleepPatternResponse;
import cerberus.HealthCare.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 인증", description = "사용자 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DataSource dataSource;

    @Operation(summary = "수면 패턴 입력", description = "초기 사용자 수면 패턴 입력")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "수면 패턴 추가 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @PostMapping("/sleep-pattern")
    public ResponseEntity<BaseResponse<SleepPatternResponse>> createSleepPattern(
        @RequestBody SleepPatternRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        SleepPatternResponse sleepPatternResponse = userService.addSleepPattern(userDetails.getUsername(), request.getPattern());
        return BaseResponse.ok("수면 패턴 추가 완료", sleepPatternResponse);
    }

    @Operation(summary = "건강리포트 조회", description = "사용자 건강 리포트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "건강리포트 조회 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @GetMapping("/report/{date}")
    public ResponseEntity<BaseResponse<HealthAnalysisResponse>> getHealthReport(
        @PathVariable LocalDate date,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        HealthAnalysisResponse healthAnalysisResponse = userService.getHealthReport(userDetails.getUsername(), date);
        return BaseResponse.ok("건강리포트 조회 완료", healthAnalysisResponse);
    }

    @Operation(summary = "건강리포트 조회2", description = "사용자 건강 리포트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "건강리포트 조회 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @GetMapping("/report/{date}/2")
    public ResponseEntity<BaseResponse<HealthReportResponse>> getHealthReport2(
        @PathVariable LocalDate date,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        HealthReportResponse healthReportResponse = userService.getHealthReport2(userDetails.getUsername(), date);
        return BaseResponse.ok("건강리포트 조회 완료", healthReportResponse);
    }

    @Operation(summary = "건강 조언 조회", description = "사용자 건강 조언 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "건강 조언 조회 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @GetMapping("/home/advice/{type}")
    public ResponseEntity<BaseResponse<HealthAdviceResponse>> getHealthAdvice(
        @PathVariable Integer type,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        HealthAdviceResponse healthAdviceResponse = userService.getHealthAdvice(userDetails.getUsername(), type);
        return BaseResponse.ok("건강 조언 조회 완료", healthAdviceResponse);
    }

    @Operation(summary = "홈화면 조회", description = "사용자 홈화면 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "홈화면 조회 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @GetMapping("/home")
    public ResponseEntity<BaseResponse<HomeResponse>> getTodayHealthStatus(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        HomeResponse homeResponse = userService.getTodayHealthStatus(userDetails.getUsername());
        return BaseResponse.ok("홈화면 조회 완료", homeResponse);
    }


}
