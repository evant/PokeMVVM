package me.tatarka.pokemvvm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.pokedex.PokedexFragment;
import me.tatarka.pokemvvm.pokedex.PokemonItemViewModel;
import me.tatarka.pokemvvm.pokemon.PokemonFragment;

public class MainActivity extends BaseActivity implements PokemonItemViewModel.OnSelectListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, PokedexFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void selectItem(PokemonItem item) {
        //TODO: tablet mater detail support.
        PokedexFragment currentFragment = (PokedexFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, PokemonFragment.newInstance(item))
                .addToBackStack(item.name());
        currentFragment.addSharedElements(ft);
        ft.commit();
    }
}
