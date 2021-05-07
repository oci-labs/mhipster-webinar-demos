package hello.mhipster.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import hello.mhipster.util.JHipsterProperties;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import org.hibernate.cache.jcache.ConfigSettings;

import javax.cache.CacheManager;
import javax.inject.Singleton;

@Factory
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Caffeine caffeine = jHipsterProperties.getCache().getCaffeine();
            
        CaffeineConfiguration<Object, Object> caffeineConfiguration = new CaffeineConfiguration<>();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(caffeine.getMaxEntries()));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(TimeUnit.SECONDS.toNanos(caffeine.getTimeToLiveSeconds())));
        caffeineConfiguration.setStatisticsEnabled(true);
        jcacheConfiguration = caffeineConfiguration;
    }

    @Singleton
    public CacheManager cacheManager(ApplicationContext applicationContext) {
        CacheManager cacheManager = new CaffeineCachingProvider().getCacheManager(
                null, applicationContext.getClassLoader(), new Properties());
        customizeCacheManager(cacheManager);
        return cacheManager;
    }

    private void customizeCacheManager(CacheManager cm) {
        createCache(cm, hello.mhipster.repository.UserRepository.USERS_BY_LOGIN_CACHE);
        createCache(cm, hello.mhipster.repository.UserRepository.USERS_BY_EMAIL_CACHE);
        createCache(cm, hello.mhipster.domain.User.class.getName());
        createCache(cm, hello.mhipster.domain.Authority.class.getName());
        createCache(cm, hello.mhipster.domain.User.class.getName() + ".authorities");
        createCache(cm, hello.mhipster.domain.Fish.class.getName());
        createCache(cm, hello.mhipster.domain.School.class.getName());
        createCache(cm, hello.mhipster.domain.School.class.getName() + ".fish");
        // jhipster-needle-caffeine-add-entry
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
