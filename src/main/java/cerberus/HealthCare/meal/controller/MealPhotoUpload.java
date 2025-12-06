package cerberus.HealthCare.meal.controller;

import cerberus.HealthCare.global.common.BaseResponse;
import cerberus.HealthCare.meal.dto.UploadImageRequest;
import cerberus.HealthCare.meal.dto.UploadImageResponse;
import cerberus.HealthCare.meal.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "음식 사진", description = "음식 사진 업로드/저장 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/food")
public class MealPhotoUpload {
    private final MealService mealService;

    @Operation(summary = "음식 사진 분석", description = "음식 종류, 영양소 분석")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 분석 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 없음)"),
            @ApiResponse(responseCode = "500", description = "이미지 분석 실패")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<UploadImageResponse>> uploadImage(
            @ModelAttribute UploadImageRequest uploadImageRequest) {

        log.info("음식 이미지 업로드 요청 수신");

        if (uploadImageRequest.getFile() == null || uploadImageRequest.getFile().isEmpty()) {
            return BaseResponse.badRequest("이미지 파일이 필요합니다.", null);
        }

        try {
            UploadImageResponse response = mealService.upload(uploadImageRequest);
            return BaseResponse.ok("분석 완료", response);
        } catch (Exception e) {
            log.error("이미지 분석 중 오류 발생", e);
            return BaseResponse.internalServerError("이미지 분석 실패: "+e.getMessage(), null);
        }
    }
}