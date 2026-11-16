package com.thaumjs;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.Context;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

/** ThaumJSEvents.research(e => { ... }) adds or replaces Thaumaturge research entries. */
public class ResearchKubeEvent implements KubeEvent {
    final Map<String, JsonObject> entries = new LinkedHashMap<>();

    /** Starts a new entry, or replaces one with the same id. */
    public ResearchBuilder create(String id) {
        ResearchBuilder b = new ResearchBuilder(Json.id(id));
        entries.put(b.id, b.json);
        return b;
    }

    /** Writes an entry from a plain object in Thaumaturge's own research_entry format. */
    public void json(Context cx, String id, Object json) {
        JsonElement j = Json.of(cx, json);
        entries.put(Json.id(id), j.getAsJsonObject());
    }
}
