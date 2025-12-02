package cerberus.HealthCare.user.controller;

import cerberus.HealthCare.global.common.BaseResponse;
import cerberus.HealthCare.global.security.JwtToken;
import cerberus.HealthCare.user.dto.LoginRequest;
import cerberus.HealthCare.user.dto.SignUpRequest;
import cerberus.HealthCare.user.dto.SignUpResponse;
import cerberus.HealthCare.user.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 인증", description = "사용자 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserAuthController {
    private final UserAuthService userAuthService;

    @Operation(summary = "로그인", description = "사용자 로그인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호가 일치하지 않습니다.")
    })
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtToken>> login(@RequestBody LoginRequest loginRequest){
        JwtToken jwtToken = userAuthService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return BaseResponse.ok("로그인 성공", jwtToken);
    }

    @Operation(summary = "회원가입", description = "사용자 회원가입")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 회원입니다")
    })
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest signUpRequest){
        SignUpResponse signUpResponse = userAuthService.signUp(signUpRequest);
        return BaseResponse.created("회원가입", signUpResponse);
    }

}
