package com.mock_ship.common.response;

import java.util.HashMap;
import java.util.Map;

public class PagingMetaHelper {

    public static Map<String, Object> createPagingMeta(int page, int size, long totalElements) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("page", page);
        meta.put("size", size);
        meta.put("totalElements", totalElements);
        meta.put("totalPages", (int) Math.ceil((double) totalElements / size));
        return meta;
    }
}
