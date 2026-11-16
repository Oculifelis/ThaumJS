package com.thaumjs;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;

/** Recipe key type "thaumjs:aspects": aspect amounts for crucible and infusion, or crystals for the arcane workbench. */
public final class AspectsComponent implements RecipeComponent<JsonElement> {
    public static final ResourceKey<RecipeComponentType<?>> TYPE =
            RecipeComponentType.key(Identifier.fromNamespaceAndPath(ThaumJS.MOD_ID, "aspects"));
    public static final AspectsComponent INSTANCE = new AspectsComponent();

    @Override public ResourceKey<RecipeComponentType<?>> type() { return TYPE; }
    @Override public Codec<JsonElement> codec() { return ExtraCodecs.JSON; }
    @Override public TypeInfo typeInfo() { return TypeInfo.OBJECT; }

    @Override
    public JsonElement wrap(RecipeScriptContext cx, Object from) {
        return Json.aspects(Json.of(cx.cx(), from));
    }

    @Override
    public boolean isEmpty(JsonElement value) {
        return value == null || value.isJsonNull() || (value.isJsonArray() && value.getAsJsonArray().isEmpty());
    }

    @Override public boolean allowEmpty() { return true; }

    @Override public String toString() { return "thaumjs:aspects"; }
}
