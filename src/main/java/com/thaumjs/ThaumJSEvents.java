package com.thaumjs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface ThaumJSEvents {
    EventGroup GROUP = EventGroup.of("ThaumJSEvents");

    EventHandler ASPECTS = GROUP.server("aspects", () -> AspectsKubeEvent.class);
    EventHandler INFERNAL_BONUS = GROUP.server("infernalBonus", () -> InfernalBonusKubeEvent.class);
    EventHandler RESEARCH = GROUP.server("research", () -> ResearchKubeEvent.class);
}
