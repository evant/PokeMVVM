package me.tatarka.pokemvvm.api;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.loader.Loader;
import me.tatarka.loader.Result;
import me.tatarka.pokemvvm.pokedex.PagedResult;
import me.tatarka.retainstate.RetainState;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

public class RxPagedLoader<T> extends Loader<Result<PagedResult<T>>> {

    public static <T> RetainState.OnCreate<RxPagedLoader<T>> create(final Observable<List<T>> observable) {
        return new RetainState.OnCreate<RxPagedLoader<T>>() {
            @Override
            public RxPagedLoader<T> onCreate() {
                return new RxPagedLoader<>(observable);
            }
        };
    }

    private final Observable<List<T>> observable;
    private Subscription subscription;
    private List<T> results = new ArrayList<>();

    public RxPagedLoader(Observable<List<T>> observable) {
        this.observable = observable;
    }

    @Override
    protected void onStart(final Receiver receiver) {
        subscription = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<T>>() {
                    @Override
                    public void call(List<T> items) {
                        results.addAll(items);
                        receiver.deliverResult(Result.success(new PagedResult<>(results, results.size() - items.size())));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable error) {
                        receiver.deliverResult(Result.<PagedResult<T>>error(error));
                        receiver.complete();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        receiver.complete();
                    }
                });
    }

    @Override
    protected void onCancel() {
        subscription.unsubscribe();
    }
}
