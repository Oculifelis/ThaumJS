package com.thaumjs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.util.JsonUtils;
import dev.latvian.mods.rhino.Context;

/** Turns script values into the JSON shapes Thaumaturge reads. */
public final class Json {
    private Json() {}

    /** "ignis" means "thaumaturge:ignis"; ids with a namespace stay as they are. */
    public static String id(String s) {
        s = s.trim();
        if (s.startsWith("#")) return "#" + id(s.substring(1));
        return s.indexOf(':') >= 0 ? s : "thaumaturge:" + s;
    }

    public static JsonElement of(Context cx, Object o) {
        if (o instanceof JsonElement j) return j;
        return JsonUtils.of(cx, o);
    }

    /**
     * Aspect lists. {ignis: 10, potentia: 5}, [{aspect: 'ignis', amount: 10}] and [['ignis', 10]]
     * all turn into [{"amount": 10, "aspect": "thaumaturge:ignis"}, ...].
     */
    public static JsonArray aspects(JsonElement in) {
        JsonArray out = new JsonArray();
        if (in == null || in.isJsonNull()) return out;
        if (in.isJsonObject()) {
            JsonObject o = in.getAsJsonObject();
            if (o.has("aspect")) {
                out.add(entry(o.get("aspect").getAsString(), o.has("amount") ? o.get("amount").getAsInt() : 1));
            } else {
                for (var e : o.entrySet()) out.add(entry(e.getKey(), e.getValue().getAsInt()));
            }
        } else if (in.isJsonArray()) {
            for (JsonElement e : in.getAsJsonArray()) {
                if (e.isJsonArray()) {
                    JsonArray a = e.getAsJsonArray();
                    out.add(entry(a.get(0).getAsString(), a.size() > 1 ? a.get(1).getAsInt() : 1));
                } else {
                    out.addAll(aspects(e));
                }
            }
        } else if (in.isJsonPrimitive()) {
            out.add(entry(in.getAsString(), 1));
        } else {
            throw new IllegalArgumentException("Not an aspect list: " + in);
        }
        return out;
    }

    private static JsonObject entry(String aspect, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Aspect amount must be above 0: " + aspect + " " + amount);
        JsonObject o = new JsonObject();
        o.addProperty("amount", amount);
        o.addProperty("aspect", id(aspect));
        return o;
    }

    /** "thaumaturge:alumentum" gives {"entry": "thaumaturge:alumentum"}; objects pass through with the entry id filled in. */
    public static JsonObject research(JsonElement in) {
        if (in.isJsonPrimitive()) {
            JsonObject o = new JsonObject();
            o.addProperty("entry", id(in.getAsString()));
            return o;
        }
        JsonObject o = in.getAsJsonObject().deepCopy();
        if (o.has("entry")) o.add("entry", new JsonPrimitive(id(o.get("entry").getAsString())));
        return o;
    }
}
