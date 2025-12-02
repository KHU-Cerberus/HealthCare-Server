package cerberus.HealthCare.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class BaseResponse<T> {
    private final String message;
    private final T data;

    public BaseResponse(String message, T body) {
        this.message = message;
        this.data = body;
    }

    public static <T> ResponseEntity<BaseResponse<T>> of(HttpStatus status, String message, T body) {
        String meta = status.value() + ": " + message;
        BaseResponse<T> response = new BaseResponse<>(meta, body);
        return ResponseEntity.status(status).body(response);
    }

    // 에러용 (body null)
    public static ResponseEntity<BaseResponse<?>> of(HttpStatus status, String message) {
        String meta = status.value() + ": " + message;
        BaseResponse<Object> response = new BaseResponse<>(meta, null);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<BaseResponse<T>> ok(String message, T body) {
        return of(HttpStatus.OK, message, body);
    }

    public static <T> ResponseEntity<BaseResponse<T>> created(String message, T body) {
        return of(HttpStatus.CREATED, message, body);
    }
    public static <T> ResponseEntity<BaseResponse<T>> badRequest(String message, T body) {
        return of(HttpStatus.BAD_REQUEST, message, body);
    }

    public static <T> ResponseEntity<BaseResponse<T>> unauthorized(String message, T body) {
        return of(HttpStatus.UNAUTHORIZED, message, body);
    }

    public static <T> ResponseEntity<BaseResponse<T>> forbidden(String message, T body) {
        return of(HttpStatus.FORBIDDEN, message, body);
    }

    public static <T> ResponseEntity<BaseResponse<T>> notFound(String message, T body) {
        return of(HttpStatus.NOT_FOUND, message, body);
    }
    public static <T> ResponseEntity<BaseResponse<T>> conflict(String message, T body) {
        return of(HttpStatus.CONFLICT, message, body);
    }


    public static <T> ResponseEntity<BaseResponse<T>> internalServerError(String message, T body) {
        return of(HttpStatus.INTERNAL_SERVER_ERROR, message, body);
    }
}
