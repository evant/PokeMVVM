package me.tatarka.pokemvvm.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import auto.parcel.AutoParcel;
import me.tatarka.gsonvalue.annotations.GsonConstructor;
import okhttp3.HttpUrl;

@AutoParcel
public abstract class Pokemon {

    @GsonConstructor
    public static Pokemon create(int id, String name, int height, int weight, Sprites sprites, List<StatEntry> stats, List<TypeEntry> types) {
        return new AutoParcel_Pokemon(id, name, height, weight, sprites, stats, types);
    }

    public abstract int id();

    public abstract String name();

    public abstract int height();

    public abstract int weight();

    public abstract Sprites sprites();
    
    public abstract List<StatEntry> stats();

    public abstract List<TypeEntry> types();

    @AutoParcel
    public static abstract class Sprites {

        @GsonConstructor
        public static Sprites create(HttpUrl frontDefault) {
            return new AutoParcel_Pokemon_Sprites(frontDefault);
        }

        @SerializedName("front_default")
        public abstract HttpUrl frontDefault();
    }

    @AutoParcel
    public static abstract class TypeEntry {

        @GsonConstructor
        public static TypeEntry create(int slot, Type type) {
            return new AutoParcel_Pokemon_TypeEntry(slot, type);
        }

        public abstract int slot();

        public abstract Type type();
    }

    @AutoParcel
    public static abstract class Type {

        @GsonConstructor
        public static Type create(String name) {
            return new AutoParcel_Pokemon_Type(name);
        }

        public abstract String name();
    }

    @AutoParcel
    public static abstract class StatEntry {
        
        @GsonConstructor
        public static StatEntry create(int baseStat, Stat stat) {
            return new AutoParcel_Pokemon_StatEntry(baseStat, stat);
        }
        
        @SerializedName("base_stat")
        public abstract int baseStat();
        
        public abstract Stat stat();
    }

    @AutoParcel
    public static abstract class Stat {
        
        @GsonConstructor
        public static Stat create(String name) {
            return new AutoParcel_Pokemon_Stat(name);
        }
        
        public abstract String name();
    }
}
