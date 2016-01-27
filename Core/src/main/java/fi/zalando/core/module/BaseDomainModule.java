package fi.zalando.core.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fi.zalando.core.data.LocationRepository;
import fi.zalando.core.domain.LocationService;

/**
 * Dagger module that provides domain layer related dependencies
 *
 * Created by jduran on 07/12/15.
 */
@Module
public class BaseDomainModule {

    /**
     * Provides a {@link LocationService} instance dependency
     *
     * @param locationRepository {@link LocationRepository} to inject
     * @return {@link LocationService} instance
     */
    @Provides
    @Singleton
    public LocationService provideLocationService(LocationRepository locationRepository) {

        return new LocationService(locationRepository);
    }
}
