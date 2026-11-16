package com.thaumjs;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.Context;

/**
 * ThaumJSEvents.aspects(e => { ... }) writes the thaumaturge:base_aspects and thaumaturge:entity_aspects data maps.
 * An item or tag set here replaces the aspects the mods gave it.
 */
public class AspectsKubeEvent implements KubeEvent {
    final DataMapFile items = new DataMapFile();
    final DataMapFile entities = new DataMapFile();

    /** Item id or #tag, e.g. e.item('minecraft:stick', {arbor: 2}). */
    public void item(Context cx, String item, Object aspects) {
        items.put(key(item), Json.aspects(Json.of(cx, aspects)));
    }

    /** Same as item(); reads better for tags. */
    public void add(Context cx, String itemOrTag, Object aspects) {
        item(cx, itemOrTag, aspects);
    }

    /** Takes all aspects off an item or tag. */
    public void remove(String itemOrTag) {
        items.remove(key(itemOrTag));
    }

    public void entity(Context cx, String entityType, Object aspects) {
        entities.put(key(entityType), Json.aspects(Json.of(cx, aspects)));
    }

    public void removeEntity(String entityTypeOrTag) {
        entities.remove(key(entityTypeOrTag));
    }

    private static String key(String id) {
        id = id.trim();
        if (id.startsWith("#")) return "#" + mc(id.substring(1));
        return mc(id);
    }

    private static String mc(String id) {
        return id.indexOf(':') >= 0 ? id : "minecraft:" + id;
    }
}
