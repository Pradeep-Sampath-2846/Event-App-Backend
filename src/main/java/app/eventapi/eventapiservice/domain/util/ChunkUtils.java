package app.eventapi.eventapiservice.domain.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChunkUtils {
    public static <T> List<List<T>> createChunkedList(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();
        if (Objects.isNull(list) || list.isEmpty()) return chunks;
        if (chunkSize<=1){
            chunks.add(list);
        }else {
            for (int i = 0; i < list.size(); i += chunkSize) {
                chunks.add(list.subList(i, Math.min(list.size(), i + chunkSize)));
            }
        }

        return chunks;
    }
}
