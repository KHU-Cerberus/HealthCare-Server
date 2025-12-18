package cerberus.HealthCare.meal.controller;

import cerberus.HealthCare.global.common.BaseResponse;
import cerberus.HealthCare.global.security.CustomUserDetails;  // 추가
import cerberus.HealthCare.meal.dto.SaveMealRequest;
import cerberus.HealthCare.meal.entity.Meal;
import cerberus.HealthCare.meal.service.MealService;
import cerberus.HealthCare.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Slf4j
@Tag(name = "음식 사진", description = "음식 사진 업로드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/food")
public class SaveMeal {
    private final MealService mealService;

    @Operation(summary = "식사 정보 저장", description = "음식 이름과 영양 정보를 저장합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식사 정보 저장 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/save")
    public ResponseEntity<BaseResponse<Meal>> saveMeal(
            @RequestBody SaveMealRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        log.info("식사 정보 저장 요청 수신");

        if (userDetails == null) {
            log.error("인증되지 않은 사용자의 요청");
            return BaseResponse.unauthorized("로그인이 필요합니다.", null);
        }

        try {


            Meal savedMeal = mealService.createMeal(request, userDetails.getUsername());
            return BaseResponse.ok("식사 정보가 저장되었습니다.", null);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청: {}", e.getMessage());
            return BaseResponse.badRequest(e.getMessage(), null);
        } catch (Exception e) {
            log.error("식사 정보 저장 중 오류 발생", e);
            return BaseResponse.internalServerError("식사 정보 저장 실패: "+e.getMessage(), null);
        }
    }
}