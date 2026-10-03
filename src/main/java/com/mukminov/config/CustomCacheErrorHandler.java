package com.mukminov.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.lang.NonNull;

@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.warn("Кэш недоступен. Graceful degradation: чтение ключа {} из {} не удалось. Запрос пойдет напрямую в источник.", key, cache.getName());
    }

    @Override
    public void handleCachePutError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key, Object value) {
        log.warn("Кэш недоступен. Graceful degradation: запись ключа {} в {} не удалась.", key, cache.getName());
    }

    @Override
    public void handleCacheEvictError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.warn("Кэш недоступен. Graceful degradation: удаление ключа {} из {} не удалось.", key, cache.getName());
    }

    @Override
    public void handleCacheClearError(@NonNull RuntimeException exception, @NonNull Cache cache) {
        log.warn("Кэш недоступен. Graceful degradation: очистка кэша {} не удалась.", cache.getName());
    }
}