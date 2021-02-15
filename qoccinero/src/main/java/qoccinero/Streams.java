package qoccinero;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Streams
{
    static <E> Collection<List<E>> batched(Stream<E> stream, int batchSize)
    {
        final AtomicInteger counter = new AtomicInteger();
        return stream
            .collect(Collectors.groupingBy(m -> counter.getAndIncrement() / batchSize))
            .values();
    }
}
