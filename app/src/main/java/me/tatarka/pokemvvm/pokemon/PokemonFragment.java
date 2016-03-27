package me.tatarka.pokemvvm.pokemon;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import me.tatarka.loader.Loader;
import me.tatarka.loader.Result;
import me.tatarka.loader.RxLoader;
import me.tatarka.pokemvvm.BaseFragment;
import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.api.PokeService;
import me.tatarka.pokemvvm.api.Pokemon;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.dagger.Dagger;
import me.tatarka.pokemvvm.databinding.PokemonFragmentBinding;

public class PokemonFragment extends BaseFragment implements Loader.Callbacks<Result<Pokemon>>, PokemonViewModel.Callbacks {

    private static final String ARG_ITEM = "item";

    public static PokemonFragment newInstance(PokemonItem item) {
        PokemonFragment fragment = new PokemonFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    PokeService api;
    @Inject
    PokemonViewModel viewModel;

    private RxLoader<Pokemon> loader;
    private PokemonFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(new AutoTransition());
        // Because we are loading things async, these are really just for exit animations.
        setEnterTransition(new TransitionSet()
                .addTransition(new AnchorAppBarSlide(R.id.app_bar).addTarget(R.id.types))
                .addTransition(new SlideDownAndFadeOut().addTarget(R.id.content)));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.pokemon_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dagger.inject(this);

        PokemonItem item = getArguments().getParcelable(ARG_ITEM);
        if (item == null) {
            throw new IllegalArgumentException("PokemonFragment missing item");
        }
        viewModel.setPokemonItem(item);
        viewModel.setCallbacks(this);

        loader = loaderManager().init(0, RxLoader.create(api.pokemon(item.url().toString()).toObservable()), this);
        loader.start();

        binding.setViewModel(viewModel);
    }

    @Override
    public void onLoaderStart() {
        viewModel.startLoading();
    }

    @Override
    public void onLoaderResult(Result<Pokemon> result) {
        viewModel.setPokemon(result);
    }

    @Override
    public void onLoaderComplete() {

    }

    @Override
    public void onRequestRetry() {
        loader.restart();
    }
}
