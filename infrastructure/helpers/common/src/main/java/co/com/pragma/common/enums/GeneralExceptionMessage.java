package co.com.pragma.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneralExceptionMessage {

    ACCESS_DENIED("GE001", "Access Denied", 401),
    INVALID_JWT("GE002", "Invalid or expired JWT token", 401);

    private final String code;
    private final String message;
    private final int statusCode;

}
