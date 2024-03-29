package com.penekhun.ctfjserver.Config.Exception;

public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " method not allowed"),
    HANDLE_ACCESS_DENIED(403, "C003", "잘못된 접근입니다."),
    CANNOT_LOAD_USERNAME(500, "C004", "존재하지 않는 username입니다"),
    DOESNT_EXIST_TOKEN(501, "C005", "존재하지 않는 토큰"),
    ONLY_ACCESS_USER(501, "C006", "유저만 사용 할 수 있는 기능입니다."),
    SERVER_NOT_OPEN(501, "C007", "서버가 오픈되어있지 않습니다."),
    OPEN_TIME_ERROR(501, "C008", "enable time format 에러"),
    TIME_ZONE_ERROR(501, "C009", "time-zone 에러"),
    FILE_REMOVE_ERROR(500, "C010", "파일 삭제 오류"),

    // Member
    EMAIL_DUPLICATION(409, "M001", "존재하는 이메일"),
    NICKNAME_DUPLICATION(409, "M002", "존재하는 닉네임"),
    USERNAME_DUPLICATION(409, "M003", "존재하는 아이디"),
    MEMBER_NOT_FOUND(403, "M004", "존재하지 않은 회원정보"),
    ALREADY_CORRECT(403, "M005", "이미 맞춘 문제입니다."),
    INCORRECT_FLAG(404, "M006", "잘못된 플래그 값입니다."),
    DUPLICATE_INFORMATION(409, "M007", "중복되는 정보가 있습니다."),
    MEMBER_DOESNT_EXIST(404, "M008", "멤버를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(409, "M009", "기존 비밀번호가 맞지 않아 변경할 수 없습니다."),
    // unchcked
    UNCHECKED_ERROR(500, "U001", "확인 할 수 없는 에러"),
    FILE_UPLOAD_FAIL(500, "U002", "파일업로드 실패"),

    ;
    private final String errorCodeStr;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String errorCodeStr, final String message) {
        this.status = status;
        this.message = message;
        this.errorCodeStr = errorCodeStr;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCodeStr;
    }
}
