package cerberus.HealthCare.user.dto;


import cerberus.HealthCare.user.entity.User.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {
    String email;
    String nickname;
    List<Role> roles;
}
