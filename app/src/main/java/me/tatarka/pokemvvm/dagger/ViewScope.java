package me.tatarka.pokemvvm.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Scoped to the same lifecycle as view. It will be recreated on configuration changes.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewScope {
}
