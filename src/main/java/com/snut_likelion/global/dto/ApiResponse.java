package com.snut_likelion.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.snut_likelion.global.error.BaseError;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    @Builder
    private ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private final static String SUCCESS_CODE = "OK";

    public static ApiResponse<Object> success(String message) {
        return ApiResponse.builder()
                .code(SUCCESS_CODE)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(SUCCESS_CODE)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(SUCCESS_CODE)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> fail(BaseError error) {
        return ApiResponse.<T>builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> fail(BaseError error, String message) {
        return ApiResponse.<T>builder()
                .code(error.getCode())
                .message(message)
                .build();
    }
}