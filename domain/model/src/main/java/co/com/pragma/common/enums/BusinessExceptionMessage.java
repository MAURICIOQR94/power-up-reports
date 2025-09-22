package co.com.pragma.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionMessage {

    FAIL_PROCESSING_REPORT_MESSAGE("BE001", "Error processing loan report message."),
    MESSAGE_NOT_NULL("BE002", "Message cannot be null."),
    MESSAGE_NOT_NULL_OR_EMPTY("BE003", "Message status cannot be null or empty."),
    INVALID_AMOUNT_VALUE ("BE004", "Message amount must be greater than zero");

    private final String code;
    private final String message;
}
