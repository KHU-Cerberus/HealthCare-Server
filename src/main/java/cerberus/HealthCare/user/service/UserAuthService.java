package cerberus.HealthCare.user.service;

import cerberus.HealthCare.global.security.JwtToken;
import cerberus.HealthCare.global.security.JwtTokenProvider;
import cerberus.HealthCare.user.dto.SignUpRequest;
import cerberus.HealthCare.user.dto.SignUpResponse;
import cerberus.HealthCare.user.entity.User;
import cerberus.HealthCare.user.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtToken login(String email, String password) {
        log.info("AuthenticationToken 생성: email={}", email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
            log.info("Authentication 성공: email={}", email);
        } catch (Exception e) {
            log.error("Authentication 실패: email={}, 이유={}", email, e.getMessage());
            throw e;
        }

        JwtToken token = jwtTokenProvider.generateToken(authentication);
        log.info("JWT 토큰 생성 완료: email={}", email);
        return token;
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        if (userAuthRepository.existsByEmail(signUpRequest.getEmail())){
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        User user = userAuthRepository.save(signUpRequest.toUser(encodedPassword));
        return new SignUpResponse(user.getEmail(), user.getNickname(), user.getRoles());

    }
}

