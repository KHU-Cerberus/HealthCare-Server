package cerberus.HealthCare.global.security;


import cerberus.HealthCare.user.entity.User;
import cerberus.HealthCare.user.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository userAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 정보 조회: username={}", username);
        return userAuthRepository.findByEmail(username)
            .map(user -> {
                log.info("사용자 발견: username={}", username);
                return createUserDetails(user);
            })
            .orElseThrow(() -> {
                log.warn("사용자 없음: username={}", username);
                return new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다.");
            });
    }

    private UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getEncryptPwd())
            .roles(user.getRoles().stream()
                .map(Enum::name)
                .toArray(String[]::new))
            .build();
    }
}

