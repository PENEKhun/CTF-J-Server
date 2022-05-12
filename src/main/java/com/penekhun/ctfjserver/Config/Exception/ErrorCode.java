package com.penekhun.ctfjserver.Config.Exception;

public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "존재하는 이메일"),
    NICKNAME_DUPLICATION(400, "M002", "존재하는 닉네임"),
    USERNAME_DUPLICATION(400, "M003", "존재하는 아이디"),
    //LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    ;
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
