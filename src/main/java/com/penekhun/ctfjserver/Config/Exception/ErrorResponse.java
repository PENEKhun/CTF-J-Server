package com.penekhun.ctfjserver.Config.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String errorCode;
    private final String message;
    private final String detail;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .errorCode(errorCode.getErrorCode())
                        .message(errorCode.name())
                        .detail(errorCode.getMessage())
                        .build()
                );
    }
}
