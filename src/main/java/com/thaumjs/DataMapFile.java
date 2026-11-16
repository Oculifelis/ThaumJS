package com.thaumjs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/** One NeoForge data map file: values merged over the mods' own, plus removals. */
final class DataMapFile {
    final Map<String, JsonElement> values = new LinkedHashMap<>();
    final Set<String> remove = new LinkedHashSet<>();

    void put(String key, JsonElement value) {
        remove.remove(key);
        values.put(key, value);
    }

    void remove(String key) {
        values.remove(key);
        remove.add(key);
    }

    boolean isEmpty() { return values.isEmpty() && remove.isEmpty(); }

    JsonObject toJson() {
        JsonObject o = new JsonObject();
        JsonObject v = new JsonObject();
        values.forEach(v::add);
        o.add("values", v);
        if (!remove.isEmpty()) {
            JsonArray r = new JsonArray();
            remove.forEach(r::add);
            o.add("remove", r);
        }
        return o;
    }
}
