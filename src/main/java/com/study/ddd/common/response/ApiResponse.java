package com.study.ddd.common.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ApiResponse<T> {
    private final String status;
    private final String message;
    private final T data;
    private final Map<String, Object> meta;
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .meta(null)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, Map<String, Object> meta) {
        return ApiResponse.<T>builder()
                .status("success")
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .meta(meta)
                .build();
    }
}
