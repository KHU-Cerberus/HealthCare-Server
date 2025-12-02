package cerberus.HealthCare.global.exception.code;

import cerberus.HealthCare.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "User not found."),
    USER_INACTIVE(40301, HttpStatus.FORBIDDEN, "User is inactive."),
    USER_ALREADY_EXISTS(40901, HttpStatus.CONFLICT, "User already exists."),
    EMAIL_ALREADY_EXISTS(40902, HttpStatus.CONFLICT, "Email already exists."),
    PHONE_ALREADY_EXISTS(40903, HttpStatus.CONFLICT, "Phone number already exists."),

    PASSWORD_MISMATCH(40010, HttpStatus.BAD_REQUEST, "Password does not match."),
    PASSWORD_TOO_WEAK(40011, HttpStatus.BAD_REQUEST, "Password is too weak.");

    private final int code;
    private final HttpStatus status;
    private final String message;
}

