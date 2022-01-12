package interview.assignments.zhanggang.core.shortener.application;

import interview.assignments.zhanggang.config.exception.error.OriginalUrlAlreadyExistException;
import interview.assignments.zhanggang.config.exception.error.ShortenerNotFoundException;
import interview.assignments.zhanggang.core.shortener.adapter.context.impl.ShortIdContextApplicationServiceImpl;
import interview.assignments.zhanggang.core.shortener.adapter.repo.ShortenerRepository;
import interview.assignments.zhanggang.core.shortener.model.Shortener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShortenerApplicationService {
    private final ShortenerRepository shortenerRepository;
    private final ShortIdContextApplicationServiceImpl shortIdContext;

    public Mono<Shortener> createNewShortener(String url) {
        return shortenerRepository.isExist(url)
                .flatMap(isExist -> {
                    if (Boolean.TRUE.equals(isExist)) {
                        return Mono.error(new OriginalUrlAlreadyExistException(url));
                    }
                    return shortIdContext.newShortId()
                            .map(id -> new Shortener(id, url))
                            .flatMap(shortenerRepository::save);
                });
    }

    public Mono<Shortener> findById(String id) {
        return shortenerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ShortenerNotFoundException(id)));
    }
}
