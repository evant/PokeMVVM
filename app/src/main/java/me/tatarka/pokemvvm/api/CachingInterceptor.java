package me.tatarka.pokemvvm.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * The pokeapi has mostly static data but does not include cache headers. Add them manually to
 * reduce the number of network requests since this data doesn't often change. Also, if you don't
 * have a network connection we can still return old data.
 */
public class CachingInterceptor implements Interceptor {
    private final ConnectivityManager cm;

    public CachingInterceptor(Context context) {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isConnected()) {
            Response response = chain.proceed(chain.request());
            return response
                    .newBuilder()
                    .addHeader("Cache-Control", "public,max-age=" + (24 * 3600))
                    .build();
        } else {
            return chain.proceed(chain.request().newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build());
        }
    }

    private boolean isConnected() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
