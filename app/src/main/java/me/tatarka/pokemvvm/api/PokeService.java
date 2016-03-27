package me.tatarka.pokemvvm.api;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Single;

public interface PokeService {
    @GET("pokemon")
    Single<Page<PokemonItem>> firstPokemonPage();

    @GET
    Single<Page<PokemonItem>> nextPokemonPage(@Url String url);

    @GET
    Single<Pokemon> pokemon(@Url String url);
}
