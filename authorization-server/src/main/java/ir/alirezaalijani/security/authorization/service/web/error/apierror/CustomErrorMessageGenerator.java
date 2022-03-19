package ir.alirezaalijani.security.authorization.service.web.error.apierror;



import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class CustomErrorMessageGenerator {

    public static String generateMessage(String entity,String message, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                message +
                searchParams;
    }

    public static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, String... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
}
