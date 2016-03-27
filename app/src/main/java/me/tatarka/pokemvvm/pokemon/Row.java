package me.tatarka.pokemvvm.pokemon;

public class Row {

    private final CharSequence name;
    private final CharSequence value;

    public Row(CharSequence name, CharSequence value) {
        this.name = name;
        this.value = value;
    }

    public CharSequence name() {
        return name;
    }

    public CharSequence value() {
        return value;
    }
}
