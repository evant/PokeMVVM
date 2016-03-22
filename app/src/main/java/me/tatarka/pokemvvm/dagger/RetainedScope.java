package me.tatarka.pokemvvm.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Scoped to it's parent (activity/fragment) but retained across configuration changes.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface RetainedScope {
}
