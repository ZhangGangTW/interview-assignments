package interview.assignments.zhanggang.core.shortid.adapter.repo.impl;

import interview.assignments.zhanggang.core.shortid.adapter.repo.ShortIdRepository;
import interview.assignments.zhanggang.core.shortid.model.ShortId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ShortIdRepositoryInMemoryImpl implements ShortIdRepository {
    private final AtomicLong seed;

    public ShortIdRepositoryInMemoryImpl() {
        seed = new AtomicLong(1);
    }


    @Override
    public Mono<ShortId> newShortId() {
        return Mono.fromCallable(() -> new ShortId(seed.getAndIncrement()));
    }
}
