package me.tatarka.pokemvvm.api;

import android.support.annotation.Nullable;

import java.util.List;

import auto.parcel.AutoParcel;
import me.tatarka.gsonvalue.annotations.GsonConstructor;
import okhttp3.HttpUrl;

@AutoParcel
public abstract class Page<T> {

    @GsonConstructor
    public static <T> Page<T> create(int count, @Nullable HttpUrl next, @Nullable HttpUrl previous, List<T> results) {
        return new AutoParcel_Page<>(count, next, previous, results);
    }

    public abstract int count();

    @Nullable
    public abstract HttpUrl next();

    @Nullable
    public abstract HttpUrl previous();

    public abstract List<T> results();
}
