# ThaumJS

ThaumJS adds KubeJS support for Thaumaturge. Scripts can add crucible, infusion and arcane workbench recipes, set the
aspects of items, tags and mobs, add Infernal Furnace bonus drops and write new research entries.

Requires Thaumaturge 1.1.0 or newer and KubeJS 8 on NeoForge 26.1.2.

## Recipes

Recipe scripts go in `kubejs/server_scripts`. Aspects are written as an object, such as `{ ordo: 5, terra: 3 }`.
Aspect and research ids without a namespace are read as Thaumaturge's.

```js
ServerEvents.recipes(function (e) {
  // result, catalyst, aspects
  e.recipes.thaumaturge.crucible('minecraft:diamond', 'minecraft:coal', { ordo: 5, terra: 3 })

  // result, central item, pedestal items, aspects
  e.recipes.thaumaturge.infusion('minecraft:netherite_ingot', 'minecraft:gold_ingot',
      ['minecraft:diamond', 'minecraft:iron_ingot'], { potentia: 10 })
    .instability(2)
    .research('first_steps')

  // result, pattern, key, vis
  e.recipes.thaumaturge.arcane_workbench_shaped('minecraft:emerald', ['AA', 'AA'], { A: 'minecraft:dirt' }, 10)
    .crystals({ aer: 1, ignis: 2 })

  // result, ingredients, vis
  e.recipes.thaumaturge.arcane_workbench_shapeless('minecraft:apple', ['minecraft:stick', 'minecraft:dirt'], 5)
})
```

Every recipe takes `.research('<entry>')`, the research that unlocks it. Recipes without it are unlocked by
`thaumaturge:first_steps`. Arcane recipes always need a vis cost above 0.

## Aspects

```js
ThaumJSEvents.aspects(function (e) {
  e.item('minecraft:stick', { herba: 7 })      // replaces the stick's aspects
  e.add('#minecraft:wool', { bestia: 4 })      // tags work too
  e.entity('minecraft:pig', { bestia: 9 })
  e.remove('minecraft:dirt')                   // takes all aspects off an item or tag
  e.removeEntity('minecraft:cow')
})
```

## Infernal Furnace bonuses

```js
ThaumJSEvents.infernalBonus(function (e) {
  // input, bonus item or tag, count, chance
  e.add('minecraft:iron_ore', 'minecraft:iron_nugget', 3, 0.5)
  e.remove('minecraft:gold_ore')               // clears that item's bonus drops
})
```

A bonus added with `add` goes on top of the ones Thaumaturge and other mods already give.

## Research

```js
ThaumJSEvents.research(function (e) {
  e.create('kubejs:better_sticks')
    .category('basics')
    .parent('first_steps')
    .position(2, 3)
    .icon('minecraft:stick')
    .stage(function (s) { s.text('kubejs.research.better_sticks.0').knowledge('observation', 'basics', 1) })
    .stage(function (s) { s.text('kubejs.research.better_sticks.1').recipe('kubejs:better_stick') })

  // or Thaumaturge's own research_entry JSON as a plain object
  e.json('kubejs:raw_entry', { category: 'thaumaturge:basics', name: 'kubejs.research.raw', /* ... */ })
})
```

`create` with the id of an existing entry replaces that entry. Stages also take `obtain(item, amount)`,
`requireResearch(id)` and `set(key, value)` for any other field.

Everything set from scripts goes into the data pack KubeJS builds on each reload, so `/reload` picks up changes.

## License

MIT
