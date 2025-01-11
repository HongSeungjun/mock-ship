package com.mock_ship.common.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class PagingResponse<T> {

    private final T data;
    private final Map<String, Object> meta;
    public static <T> PagingResponse<T> of(T data, Map<String, Object> meta) {
        return PagingResponse.<T>builder()
                .data(data)
                .meta(meta)
                .build();
    }
}
