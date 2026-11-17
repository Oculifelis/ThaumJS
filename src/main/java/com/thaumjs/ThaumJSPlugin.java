package com.thaumjs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.Identifier;

/**
 * Recipe schemas live in data/thaumaturge/kubejs/recipe_schema. This class adds the two key types they use and the
 * ThaumJSEvents group, whose results go into the data pack KubeJS builds (it loads after the mods).
 */
public class ThaumJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
        registry.unit(AspectsComponent.TYPE, AspectsComponent.INSTANCE);
        registry.unit(ResearchComponent.TYPE, ResearchComponent.INSTANCE);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(ThaumJSEvents.GROUP);
    }

    @Override
    public void generateData(KubeDataGenerator gen) {
        if (ThaumJSEvents.ASPECTS.hasListeners()) {
            AspectsKubeEvent e = new AspectsKubeEvent();
            ThaumJSEvents.ASPECTS.post(ScriptType.SERVER, e);
            if (!e.items.isEmpty()) gen.json(id("thaumaturge", "data_maps/item/base_aspects"), e.items.toJson());
            if (!e.entities.isEmpty()) gen.json(id("thaumaturge", "data_maps/entity_type/entity_aspects"), e.entities.toJson());
        }

        if (ThaumJSEvents.INFERNAL_BONUS.hasListeners()) {
            InfernalBonusKubeEvent e = new InfernalBonusKubeEvent();
            ThaumJSEvents.INFERNAL_BONUS.post(ScriptType.SERVER, e);
            if (!e.file.isEmpty()) gen.json(id("thaumaturge", "data_maps/item/infernal_bonus"), e.file.toJson());
        }

        if (ThaumJSEvents.RESEARCH.hasListeners()) {
            ResearchKubeEvent e = new ResearchKubeEvent();
            ThaumJSEvents.RESEARCH.post(ScriptType.SERVER, e);
            e.entries.forEach((id, json) -> {
                Identifier rl = Identifier.parse(id);
                gen.json(rl.withPath("thaumaturge/research_entry/" + rl.getPath()), json);
            });
            e.categories.forEach((id, json) -> {
                Identifier rl = Identifier.parse(id);
                gen.json(rl.withPath("thaumaturge/research_category/" + rl.getPath()), json);
            });
        }
    }

    private static Identifier id(String ns, String path) {
        return Identifier.fromNamespaceAndPath(ns, path);
    }
}
