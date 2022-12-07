package org.autotaker.gpt_gen.meeting.reservations;

public enum ErrorCode {
    RESERVATION_MANAGER_NOT_FOUND(1),
    ;

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
