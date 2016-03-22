package me.tatarka.pokemvvm.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.tatarka.gsonvalue.ValueTypeAdapterFactory;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class ApiModule {
    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB

    private Context context;

    public ApiModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), CACHE_SIZE))
                .build();
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder()
                .registerTypeAdapter(HttpUrl.class, new HttpUrlTypeAdapter())
                .registerTypeAdapterFactory(new ValueTypeAdapterFactory())
                .create();
    }

    @Provides
    @Singleton
    public PokeService providesPokeService(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://pokeapi.co/api/v2/")
                .build();
        return retrofit.create(PokeService.class);
    }
}
