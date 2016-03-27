package me.tatarka.pokemvvm.api;

import android.os.Parcelable;

import java.util.List;

import auto.parcel.AutoParcel;
import me.tatarka.gsonvalue.annotations.GsonConstructor;
import okhttp3.HttpUrl;

@AutoParcel
public abstract class PokemonItem implements Parcelable {

    @GsonConstructor
    public static PokemonItem create(String name, HttpUrl url) {
        return new AutoParcel_PokemonItem(name, url);
    }

    public abstract String name();

    public abstract HttpUrl url();

    public String number() {
        List<String> pathSegments = url().pathSegments();
        return pathSegments.get(pathSegments.size() - 2);
    }
}
