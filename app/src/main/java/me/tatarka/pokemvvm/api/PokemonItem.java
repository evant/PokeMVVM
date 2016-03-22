package me.tatarka.pokemvvm.api;

import auto.parcel.AutoParcel;
import me.tatarka.gsonvalue.annotations.GsonConstructor;
import okhttp3.HttpUrl;

@AutoParcel
public abstract class PokemonItem {
   
    @GsonConstructor
    public static PokemonItem create(String name, HttpUrl url) {
        return new AutoParcel_PokemonItem(name, url);
    }

    public abstract String name();

    public abstract HttpUrl url();
}
