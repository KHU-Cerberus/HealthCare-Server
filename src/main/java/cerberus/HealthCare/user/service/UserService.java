package cerberus.HealthCare.user.service;

import cerberus.HealthCare.global.exception.CoreException;
import cerberus.HealthCare.global.exception.code.UserErrorCode;
import cerberus.HealthCare.user.dto.SleepPatternResponse;
import cerberus.HealthCare.user.entity.User;
import cerberus.HealthCare.user.repository.UserRepository;
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

    @Transactional
    public SleepPatternResponse addSleepPattern(String username, String pattern) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CoreException(UserErrorCode.USER_NOT_FOUND));
        user.setSleepPattern(pattern);
        return new SleepPatternResponse(user.getId(), user.getSleepPattern());
    }
}
