package me.tatarka.pokemvvm.log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.tatarka.pokemvvm.BuildConfig;
import timber.log.Timber;

@Module
public class LogModule {

    @Provides
    @Singleton
    public Timber.Tree providesTree() {
        if (BuildConfig.DEBUG) {
            return new Timber.DebugTree();
        } else {
            return new EmptyTree();
        }
    }
}
