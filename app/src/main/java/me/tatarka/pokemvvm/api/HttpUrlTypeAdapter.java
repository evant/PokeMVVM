package me.tatarka.pokemvvm.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import okhttp3.HttpUrl;

public class HttpUrlTypeAdapter extends TypeAdapter<HttpUrl> {
    @Override
    public void write(JsonWriter out, HttpUrl value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString());
        }
    }

    @Override
    public HttpUrl read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return HttpUrl.parse(in.nextString());
        }
    }
}
