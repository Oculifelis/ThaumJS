package com.thaumjs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.event.KubeEvent;

/**
 * ThaumJSEvents.infernalBonus(e => { ... }) writes the thaumaturge:infernal_bonus data map,
 * the extra drops the Infernal Furnace gives when it smelts an item.
 */
public class InfernalBonusKubeEvent implements KubeEvent {
    final DataMapFile file = new DataMapFile();

    /** e.add('#c:ores/tin', '#c:nuggets/tin', 2, 0.33) */
    public void add(String input, String bonus, int count, float chance) {
        JsonObject o = new JsonObject();
        o.addProperty("chance", chance);
        o.addProperty("count", count);
        o.addProperty("items", bonus);
        JsonArray a = new JsonArray();
        a.add(o);
        file.put(input, a);
    }

    public void remove(String input) {
        file.remove(input);
    }
}
