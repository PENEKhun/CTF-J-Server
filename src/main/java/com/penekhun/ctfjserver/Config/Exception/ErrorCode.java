package com.penekhun.ctfjserver.Config.Exception;

public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "C003", "잘못된 접근입니다."),
    CANNOT_LOAD_USERNAME(500, "C004", "존재하지 않는 username입니다"),
    EXIST_TOKEN_ERR(501, "C005", "존재하지 않는 토큰"),

    // Member
    EMAIL_DUPLICATION(200, "M001", "존재하는 이메일"),
    NICKNAME_DUPLICATION(200, "M002", "존재하는 닉네임"),
    USERNAME_DUPLICATION(200, "M003", "존재하는 아이디"),
    MEMBER_NOT_FOUND(200, "M004", "존재하지 않은 회원정보"),
    //LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    ;
    private final String errorCode;
    private final String message;
    private int status;

    ErrorCode(final int status, final String errorCode, final String message) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
