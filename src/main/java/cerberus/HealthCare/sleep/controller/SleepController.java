package cerberus.HealthCare.sleep.controller;

import cerberus.HealthCare.global.common.BaseResponse;
import cerberus.HealthCare.global.security.CustomUserDetails;
import cerberus.HealthCare.sleep.dto.CreateSleepRequest;
import cerberus.HealthCare.sleep.dto.CreateSleepResponse;
import cerberus.HealthCare.sleep.dto.DeleteSleepRequest;
import cerberus.HealthCare.sleep.dto.EditSleepRequest;
import cerberus.HealthCare.sleep.dto.EditSleepResponse;
import cerberus.HealthCare.sleep.dto.SleepLog24HResponse;
import cerberus.HealthCare.sleep.service.SleepService;
import cerberus.HealthCare.user.dto.SleepPatternRequest;
import cerberus.HealthCare.user.dto.SleepPatternResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수면 관련", description = "사용자 수면 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sleep")
public class SleepController {

    private final SleepService sleepService;

    @Operation(summary = "수면 기록 입력", description = "사용자 수면 정보 입력")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "수면 기록 추가 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<CreateSleepResponse>> createSleep(
        @RequestBody CreateSleepRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        CreateSleepResponse createSleepResponse = sleepService.createSleep(userDetails.getUsername(), request.getStart(), request.getEnd());
        return BaseResponse.created("수면 기록 추가 완료", createSleepResponse);
    }

    @Operation(summary = "수면 시간 조회(24시간)", description = "사용자 수면 시간 조회(24시간)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수면 기록 조회 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @GetMapping("/get")
    public ResponseEntity<BaseResponse<List<SleepLog24HResponse>>> getSleep24Hours(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SleepLog24HResponse> sleepLog24HResponse = sleepService.getSleep24Hours(userDetails.getUsername());
        return BaseResponse.ok("수면 기록 추가 완료", sleepLog24HResponse);
    }

    @Operation(summary = "수면 시간 삭제", description = "사용자 수면 시간 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "수면 기록 삭제 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<Void>> deleteSleep(
        @RequestBody DeleteSleepRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        sleepService.deleteSleep(userDetails.getUsername(), request);
        return BaseResponse.noContent("수면 기록 삭제 완료", null);
    }

    @Operation(summary = "수면 기록 수정", description = "사용자 수면 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수면 기록 추가 완료"),
        @ApiResponse(responseCode = "401", description = "유저를 찾을 수 없습니다")
    })
    @PutMapping("/edit")
    public ResponseEntity<BaseResponse<EditSleepResponse>> editSleep(
        @RequestBody EditSleepRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        EditSleepResponse editSleepResponse = sleepService.editSleep(userDetails.getUsername(), request);
        return BaseResponse.ok("수면 기록 수정 완료", editSleepResponse);
    }

}
