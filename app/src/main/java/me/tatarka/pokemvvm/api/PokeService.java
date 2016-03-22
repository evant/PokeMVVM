package me.tatarka.pokemvvm.api; 
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Single;

public interface PokeService {
    @GET("pokemon")
    Single<Page<PokemonItem>> pokemon();

    @GET
    Single<Page<PokemonItem>> nextPokemon(@Url String url);
}
