package com.thaumjs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.rhino.Context;

import java.util.function.Consumer;

/** Builds one research_entry JSON. Name and stage text are lang keys or plain text. */
public class ResearchBuilder {
    final String id;
    final JsonObject json = new JsonObject();

    ResearchBuilder(String id) {
        this.id = id;
        String ns = id.substring(0, id.indexOf(':'));
        String path = id.substring(id.indexOf(':') + 1);
        json.addProperty("category", "thaumaturge:basics");
        json.addProperty("name", "research." + ns + "." + path + ".title");
        json.add("parents", new JsonArray());
        json.addProperty("column", 0);
        json.addProperty("row", 0);
        json.add("icons", new JsonArray());
        json.add("stages", new JsonArray());
        json.add("note_aspects", new JsonArray());
        json.addProperty("complexity", 1);
    }

    public ResearchBuilder category(String category) {
        json.addProperty("category", Json.id(category));
        return this;
    }

    public ResearchBuilder name(String name) {
        json.addProperty("name", name);
        return this;
    }

    public ResearchBuilder position(int column, int row) {
        json.addProperty("column", column);
        json.addProperty("row", row);
        return this;
    }

    public ResearchBuilder parent(String... parents) {
        for (String p : parents) json.getAsJsonArray("parents").add(Json.id(p));
        return this;
    }

    public ResearchBuilder icon(String... icons) {
        for (String i : icons) json.getAsJsonArray("icons").add(i);
        return this;
    }

    public ResearchBuilder complexity(int complexity) {
        json.addProperty("complexity", complexity);
        return this;
    }

    /** Aspects the research notes ask for, e.g. {potentia: 3, ignis: 1}. */
    public ResearchBuilder noteAspects(Context cx, Object aspects) {
        json.add("note_aspects", Json.aspects(Json.of(cx, aspects)));
        return this;
    }

    public ResearchBuilder stage(Consumer<StageBuilder> stage) {
        StageBuilder s = new StageBuilder(id, json.getAsJsonArray("stages").size());
        stage.accept(s);
        json.getAsJsonArray("stages").add(s.json);
        return this;
    }

    /** Sets any other field in Thaumaturge's research_entry format. */
    public ResearchBuilder set(Context cx, String key, Object value) {
        json.add(key, Json.of(cx, value));
        return this;
    }

    public static class StageBuilder {
        final JsonObject json = new JsonObject();

        StageBuilder(String id, int index) {
            String ns = id.substring(0, id.indexOf(':'));
            String path = id.substring(id.indexOf(':') + 1);
            json.addProperty("text", "research." + ns + "." + path + ".stage_" + index);
        }

        public StageBuilder text(String text) {
            json.addProperty("text", text);
            return this;
        }

        /** Knowledge points needed to pass the stage; type is "observation" or "theory". */
        public StageBuilder knowledge(String type, String category, int amount) {
            JsonObject k = new JsonObject();
            k.addProperty("type", type);
            k.addProperty("category", Json.id(category));
            k.addProperty("amount", amount);
            array("required_knowledge").add(k);
            return this;
        }

        /** Items handed in to pass the stage. */
        public StageBuilder obtain(String item, int amount) {
            JsonObject o = new JsonObject();
            JsonArray items = new JsonArray();
            items.add(item);
            o.add("items", items);
            o.addProperty("amount", amount);
            array("obtain").add(o);
            return this;
        }

        public StageBuilder requireResearch(String... ids) {
            for (String r : ids) array("required_research").add(Json.id(r));
            return this;
        }

        /** Recipes this stage shows in the book. */
        public StageBuilder recipe(String... ids) {
            for (String r : ids) array("recipes").add(r);
            return this;
        }

        public StageBuilder set(Context cx, String key, Object value) {
            json.add(key, Json.of(cx, value));
            return this;
        }

        private JsonArray array(String key) {
            if (!json.has(key)) json.add(key, new JsonArray());
            return json.getAsJsonArray(key);
        }
    }
}
