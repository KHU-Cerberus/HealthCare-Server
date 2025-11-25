package cerberus.HealthCare.user.dto;

import cerberus.HealthCare.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data

public class SignUpRequest {

    String email;
    String password;
    String nickname;
    LocalDate birthday;
    String gender;

    public User toUser(String encodedPassword) {
        return new User(email, encodedPassword, List.of(User.Role.USER), nickname, birthday, gender);
    }
}


