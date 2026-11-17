// Boot test for ThaumJS. Every line it prints starts with THAUMJS-TEST.
ServerEvents.recipes(e => {
  e.recipes.thaumaturge.crucible('minecraft:diamond', 'minecraft:coal', { ordo: 5, terra: 3 }).id('thaumjs:test/crucible')
  e.recipes.thaumaturge.infusion('minecraft:netherite_ingot', 'minecraft:gold_ingot', ['minecraft:diamond', 'minecraft:iron_ingot'], { potentia: 10 })
    .instability(2).research('thaumaturge:first_steps').id('thaumjs:test/infusion')
  e.recipes.thaumaturge.arcane_workbench_shaped('minecraft:emerald', ['AA', 'AA'], { A: 'minecraft:dirt' }, 10)
    .crystals({ aer: 1, ignis: 2 }).id('thaumjs:test/arcane_shaped')
  e.recipes.thaumaturge.arcane_workbench_shapeless('minecraft:apple', ['minecraft:stick', 'minecraft:dirt'], 5).id('thaumjs:test/arcane_shapeless')
  e.recipes.thaumaturge.infusion_enchantment('arcing', ['minecraft:enchanted_book', 'minecraft:redstone'], { potentia: 20 }, 'minecraft:stone_sword').id('thaumjs:test/enchant')
  try {
    e.recipes.thaumaturge.infusion_enchantment('arcing', ['minecraft:book'], { potentia: 5 }).displayCatalyst('minecraft:iron_sword').id('thaumjs:test/enchant_setter')
    console.info('THAUMJS-TEST displayCatalyst setter = ok')
  } catch (err) { console.info('THAUMJS-TEST displayCatalyst setter = missing ' + err) }
  e.recipes.thaumaturge.dust_trigger_simple('minecraft:diamond_block', 'minecraft:coal_block').id('thaumjs:test/dust_simple')
  e.recipes.thaumaturge.dust_trigger_tag('minecraft:emerald_block', 'minecraft:logs').id('thaumjs:test/dust_tag')
  e.recipes.thaumaturge.dust_trigger_multiblock('minecraft:gold_block', 'thaumaturge:infernal_furnace').id('thaumjs:test/dust_multi')
  console.info('THAUMJS-TEST recipes queued')
})

ThaumJSEvents.aspects(e => {
  e.item('minecraft:stick', { herba: 7 })
  e.add('#minecraft:wool', [['bestia', 4]])
  e.entity('minecraft:pig', { bestia: 9 })
  console.info('THAUMJS-TEST aspects event ran')
})

ThaumJSEvents.infernalBonus(e => {
  e.add('minecraft:iron_ore', 'minecraft:iron_nugget', 3, 0.5)
  console.info('THAUMJS-TEST infernal bonus event ran')
})

ThaumJSEvents.research(e => {
  e.category('thaumjs:test_tab', {
    background: 'thaumaturge:textures/gui/gui_research_back_3_smooth.png',
    formula: [{ aspect: 'thaumaturge:ordo', amount: 10 }],
    icon: 'thaumaturge:textures/research/cat_alchemy.png',
    index: 20
  })
  e.create('thaumjs:test_entry')
    .parent('first_steps')
    .position(2, 3)
    .icon('minecraft:stick')
    .stage(s => s.text('Test stage').knowledge('observation', 'basics', 1))
    .stage(s => s.text('Done').recipe('thaumjs:test/crucible'))
  console.info('THAUMJS-TEST research event ran')
})

ServerEvents.loaded(e => {
  const s = e.server
  const report = (name, fn) => {
    try { console.info('THAUMJS-TEST ' + name + ' = ' + fn()) } catch (err) { console.error('THAUMJS-TEST ' + name + ' FAILED ' + err) }
  }
  const Identifier = Java.loadClass('net.minecraft.resources.Identifier')
  const BuiltInRegistries = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
  const AspectDataMaps = Java.loadClass('com.leclowndu93150.thaumaturge.api.aspect.AspectDataMaps')
  const IResearchEntry = Java.loadClass('com.leclowndu93150.thaumaturge.api.research.IResearchEntry')

  for (const id of ['crucible', 'infusion', 'arcane_shaped', 'arcane_shapeless', 'enchant', 'dust_simple', 'dust_tag', 'dust_multi']) {
    report('recipe ' + id, () => {
      const r = s.recipeManager.getRecipes().stream().filter(h => String(h.id().identifier()) == 'thaumjs:test/' + id).findFirst()
      return r.isPresent() ? 'present ' + String(r.get().value()).split('@')[0] : 'MISSING'
    })
  }
  report('stick aspects', () => BuiltInRegistries.ITEM.wrapAsHolder(Item.of('minecraft:stick').item).getData(AspectDataMaps.BASE_ASPECTS))
  report('white wool aspects', () => BuiltInRegistries.ITEM.wrapAsHolder(Item.of('minecraft:white_wool').item).getData(AspectDataMaps.BASE_ASPECTS))
  report('pig aspects', () => BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse('minecraft:pig'))).getData(AspectDataMaps.ENTITY_ASPECTS))
  const InfernalBonus = Java.loadClass('com.leclowndu93150.thaumaturge.content.infernalfurnace.InfernalBonus')
  report('iron ore infernal bonus', () => BuiltInRegistries.ITEM.wrapAsHolder(Item.of('minecraft:iron_ore').item).getData(InfernalBonus.DATA_MAP))
  const IResearchCategory = Java.loadClass('com.leclowndu93150.thaumaturge.api.research.IResearchCategory')
  report('research category', () => s.registryAccess().lookupOrThrow(IResearchCategory.REGISTRY_KEY).getValue(Identifier.parse('thaumjs:test_tab')))
  report('research entry', () => s.registryAccess().lookupOrThrow(IResearchEntry.REGISTRY_KEY).getValue(Identifier.parse('thaumjs:test_entry')))
})
