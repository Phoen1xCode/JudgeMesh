package com.judgemesh.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/** 统一响应包装。 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private T data;
    private Instant timestamp = Instant.now();

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = ErrorCode.SUCCESS.getCode();
        r.message = ErrorCode.SUCCESS.getMessage();
        r.data = data;
        return r;
    }

    public static <T> ApiResponse<T> fail(ErrorCode ec) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = ec.getCode();
        r.message = ec.getMessage();
        return r;
    }

    public static <T> ApiResponse<T> fail(ErrorCode ec, String detail) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = ec.getCode();
        r.message = ec.getMessage() + ": " + detail;
        return r;
    }
}
