package cerberus.HealthCare.global.exception.code;

import cerberus.HealthCare.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(40001, HttpStatus.BAD_REQUEST, "Invalid parameter included."),
    MISSING_REQUIRED_FIELD(40002, HttpStatus.BAD_REQUEST, "Required field is missing."),
    INVALID_FORMAT(40003, HttpStatus.BAD_REQUEST, "Invalid format."),

    RESOURCE_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "Resource not found."),

    METHOD_NOT_ALLOWED(40501, HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed."),
    UNSUPPORTED_MEDIA_TYPE(41501, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type."),

    INTERNAL_SERVER_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),
    DATABASE_ERROR(50002, HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred."),
    EXTERNAL_API_ERROR(50003, HttpStatus.INTERNAL_SERVER_ERROR, "External API call failed."),
    UNKNOWN_ERROR(50004, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred.");

    private final int code;
    private final HttpStatus status;
    private final String message;
}

