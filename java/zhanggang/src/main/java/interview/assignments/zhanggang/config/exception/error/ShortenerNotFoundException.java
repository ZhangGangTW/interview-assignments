package interview.assignments.zhanggang.config.exception.error;

import interview.assignments.zhanggang.config.exception.base.BizException;

public class ShortenerNotFoundException extends BizException {
    public ShortenerNotFoundException(String id) {
        super("The shortener code: [" + id + "] not found.");
    }
}