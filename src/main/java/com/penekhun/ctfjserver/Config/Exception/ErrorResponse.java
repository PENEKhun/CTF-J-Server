package com.penekhun.ctfjserver.Config.Exception;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
