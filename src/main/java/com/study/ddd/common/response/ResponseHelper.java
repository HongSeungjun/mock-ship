package com.study.ddd.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHelper {

    public static <T> ResponseEntity<T> of(T data) {
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(data);
    }

    public static <T> ResponseEntity<Map<String, Object>> pagedResponse(T data, int page, int size, long totalElements) {
        if (data == null || (data instanceof Iterable && !((Iterable<?>) data).iterator().hasNext())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);

        Map<String, Object> meta = PagingMetaHelper.createPagingMeta(page, size, totalElements);
        response.put("meta", meta);

        return ResponseEntity.ok(response);
    }
}
