package dev.fernando.dscatalog.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static<ID> List<? extends HasId<ID>> replace(List<? extends HasId<ID>> ordered, List<? extends HasId<ID>> unordered) {
        Map<ID, HasId<ID>> map = new HashMap<>();
        unordered.forEach(item -> map.put(item.getId(), item));
        return ordered.stream()
        .map(productProjection -> map.get(productProjection.getId()))
        .toList();
    }
}
