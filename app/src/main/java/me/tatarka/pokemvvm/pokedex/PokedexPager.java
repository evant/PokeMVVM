package me.tatarka.pokemvvm.pokedex;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.tatarka.pokemvvm.api.Page;
import me.tatarka.pokemvvm.api.PokeService;
import me.tatarka.pokemvvm.api.PokemonItem;
import okhttp3.HttpUrl;
import rx.Observable;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

@Singleton
public class PokedexPager {
    private PokeService api;
    private BehaviorSubject<HttpUrl> nextRequests = BehaviorSubject.create((HttpUrl) null);
    private AtomicReference<HttpUrl> nextPage = new AtomicReference<>();

    @Inject
    public PokedexPager(PokeService api) {
        this.api = api;
    }

    /**
     * Returns an observable that emits pages as they are requested. The first page will be
     * immediately emitted when subscribed. If there is an error, you may call this again and
     * re-subscribe to pick up where you left off.
     */
    public Observable<List<PokemonItem>> pokemon() {
        return Observable.concat(nextRequests.map(new Func1<HttpUrl, Observable<Page<PokemonItem>>>() {
            @Override
            public Observable<Page<PokemonItem>> call(HttpUrl url) {
                Single<Page<PokemonItem>> call = url == null
                        ? api.pokemon()
                        : api.nextPokemon(url.toString());
                return call.doOnSuccess(new Action1<Page<PokemonItem>>() {
                    @Override
                    public void call(Page<PokemonItem> page) {
                        nextPage.set(page.next());
                    }
                }).toObservable();
            }
        })).takeUntil(new Func1<Page<PokemonItem>, Boolean>() {
            @Override
            public Boolean call(Page<PokemonItem> page) {
                return page.next() == null;
            }
        }).map(new Func1<Page<PokemonItem>, List<PokemonItem>>() {
            @Override
            public List<PokemonItem> call(Page<PokemonItem> page) {
                return page.results();
            }
        });
    }

    /**
     * Sets the next page, notifying any subscriber. Setting this to null will 'reset' the pager to
     * the first page.
     */
    public void setNextPage(@Nullable HttpUrl nextPage) {
        this.nextPage.set(nextPage);
        nextRequests.onNext(nextPage);
    }

    /**
     * Returns the current next page or null if there isn't one.
     */
    @Nullable
    public HttpUrl nextPage() {
        return nextPage.get();
    }

    /**
     * Requests the next page if there is a next page available and it hasn't already been
     * requested. This method only has an effect <em>after</em> the previous page has been loaded.
     */
    public void requestNextPage() {
        HttpUrl next = nextPage.getAndSet(null);
        if (next != null) {
            nextRequests.onNext(next);
        }
    }
}
