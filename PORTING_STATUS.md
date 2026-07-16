# Porting status

Updated: 2026-07-16

## Completed

- Preserved the original JAR files unchanged.
- Decompiled Animation Director 1.3.1 (84 Java files) with CFR 0.152.
- Decompiled NeedsOfNature 1.3.1 (201 Java files) with CFR 0.152.
- Converted Fabric intermediary identifiers to readable Yarn 1.21.11 names.
- Created a two-module Fabric Loom project targeting Minecraft 1.20.1.
- Configured Java 17, Yarn `1.20.1+build.10`, Fabric API `0.92.9`,
  GeckoLib 4.8.4, Trinkets 3.7.2, and Fabric Loader 0.16.14.
- Extracted all original assets, data files, mixin configs, and language files.
- Repaired CFR's invalid pseudo-`GOTO` output in `AfwWildfireGenderCompat`.
- Repaired the second invalid pseudo-`GOTO` and synthetic lambda output in
  `NeedsOfNature`.
- Replaced the first Java 21 `List.getFirst/getLast` calls with Java 17 code.
- Began converting Fabric's 1.21 resource reload registration to the 1.20.1 API.
- Reached the Java compiler and saved the current diagnostic output in
  `animationdirector-compile.log`.
- Rewrote all Animation Director packets to Fabric 1.20.1 `Identifier` channels
  and `PacketByteBuf` codecs, including client/server receiver registration.
- Replaced post-1.20 server/client packet sending with the 1.20.1 networking API.
- Ported the core actor animatable, model adapter, resource cache lookup, and
  replacement renderer from GeckoLib 5 to GeckoLib 4.
- Replaced the 1.21 render-state/command-queue bridge with a direct 1.20.1
  `EntityRenderDispatcher` redirect. Actor UUIDs now provide distinct GeckoLib
  instance IDs and the AFW authored timeline drives GeckoLib 4 ticks.
- Ported whole-model translucent texture layers and emissive layers to the
  GeckoLib 4 render-layer lifecycle.
- Obtained the first clean `:animationdirector:compileJava` build on Minecraft
  1.20.1 / Java 17 / GeckoLib 4.
- Obtained a successful full `:animationdirector:build`, including resource
  processing, Mixin remapping, source JAR, and remapped mod JAR generation.
- Ported Animation Director's configuration screens, direct input locking,
  camera constraints, animation-definition resource loading, sound metadata,
  and vanilla texture copying to the 1.20.1 APIs.
- Rewrote CFR's Java 21 preview `typeSwitch` output in the server animation
  controller as Java 17 `instanceof` branches.
- Rewrote all 23 NeedsOfNature C2S/S2C payloads to the Fabric 1.20.1
  `Identifier` + `PacketByteBuf` protocol while preserving channel IDs, field
  order, integer encodings, enum fallbacks, and the 64 KiB skin upload limit.
- Added typed 1.20.1 client/server networking facades, migrated all eight server
  receivers and all fifteen client receivers, and converted every packet send
  site. No functional `CustomPayload`, `PayloadTypeRegistry`, `RegistryByteBuf`,
  or `PacketCodec` references remain in NeedsOfNature.
- Ported the Horse Liquid Collector entity, item, models, entity renderer, and
  item renderer from GeckoLib 5 render states/providers to GeckoLib 4 direct
  animatables and renderers. Its 1.21 data tracker, NBT, damage, item-use, entity
  registration, and renderer-registration signatures were also converted.
- Ported Pregnancy Egg's entity, dynamic model/texture selection, growth scale,
  hurt wobble, NBT persistence, data tracker, dimensions, spawning, and renderer
  to the Minecraft 1.20.1 and GeckoLib 4 APIs. Collector and egg classes no
  longer appear in the current compiler diagnostics.
- Ported all four special crafting recipes and their serializers from the 1.21
  recipe-input/display API to 1.20.1 `SpecialCraftingRecipe`,
  `RecipeInputInventory`, and `DynamicRegistryManager` signatures.
- Replaced Energy Stabilizer and liquid-bottle data components with persistent
  item NBT, converted potion creation/inspection to `PotionUtil`, and migrated
  custom brewing recipes to the 1.20.1 brewing registry. `NonItemSystem`, the
  recipe classes, potion registry, stabilizer item, and brewing registry no
  longer appear in compiler diagnostics. The current error-header count fell
  from 1,263 to 1,042 during this pass.
- Ported the Destroyed Skin dynamic-texture pipeline to 1.20.1 player skin,
  `NativeImage`, and `NativeImageBackedTexture` APIs; repaired its compositing
  generics and CFR variable artifacts; and migrated the player data tracker and
  player renderer texture replacement Mixins. The matching server sync/state
  code was also repaired. These core Destroyed Skin classes no longer appear in
  compiler diagnostics, reducing the current error-header count to 966.
- Ported the Trinkets compatibility layer to Trinkets 3.7.2: slot identifiers
  are reconstructed from `SlotType` group/name, equipped-stack iteration is
  strongly typed, the 1.20.1 sound and tooltip drawing APIs are used, and the
  Cardinal Components 5.2.0 compile interfaces are declared explicitly. The
  accessory effects, pregnancy protection, durability, shed overlay, active
  tank, equip-validation, and unavailable-slot UI classes are compiler-clean.
- Ported data-driven accessory item registration from 1.21 registry keys and
  data components to 1.20.1 item settings and registry calls. JSON-defined
  stack size and durability remain supported.
- Migrated all eleven remaining server-data reload listeners from Fabric's
  removed `resource.v1.ResourceLoader` API to 1.20.1
  `ResourceManagerHelper`/`SimpleSynchronousResourceReloadListener`, including
  stable listener IDs. The latest estimated unique error-header count is 887.
- Ported all six custom particle implementations and their registry from the
  1.21 particle API to 1.20.1 `DefaultParticleType`, sprite billboard,
  texture-sheet, factory, client-world spawn, sound, and previous-position
  APIs. Animated UV frames and tinting remain intact, and puddles use a custom
  horizontal quad so their original ground-plane orientation is preserved.
- Ported the complete gameplay HUD renderer to 1.20.1 direct texture drawing,
  `MatrixStack`, tick interpolation, status-effect lookup, and explicit shader
  tinting. Energy/escape bars, attack prompt frames, liquid fill tint, preview
  placement, and pulsing energy-heart alpha are retained.
- Rewrote the armor/accessory shed prop renderer from 1.21 world render states
  and command queues to Fabric 1.20.1 `WorldRenderEvents` and direct
  `ItemRenderer` calls. Prop interpolation, flight physics, landing, rotation,
  fade scale, and full-bright item rendering remain active. These client groups
  are compiler-clean, reducing the estimated unique error-header count to 765.
- Ported `NeedsOfNatureClient` itself to 1.20.1. Client renderer/event/network
  initialization remains wired; key binding categories use the legacy string
  API; player skin and visible model-part access use 1.20.1 methods; dynamic
  texture tinting now handles `NativeImage`'s ABGR layout correctly.
- Replaced the removed 1.21 equipment-asset model lookup with 1.20.1 horse
  armor item textures and dye NBT, mapped horse markings to legacy vanilla
  resources, mapped sheep wool to `sheep_fur.png`, and retained tinted wolf
  collars. Saddle geometry in 1.20.1 uses the horse model/base texture, while
  wolf armor is intentionally absent because it did not exist in that version.
- Reworked the HUD Mixin around the 1.20.1 `InGameHud.render(DrawContext,float)`
  entry point. Animation HUD replacement and energized-effect filtering now
  compile against the old HUD lifecycle. The removed 1.21 inventory
  `StatusEffectsDisplay` target is safely retargeted to its 1.20.1 host pending
  restoration of custom inventory-panel icons. Estimated unique errors: 681.
- Migrated entity/player persistence from the 1.21 `ReadView`/`WriteView` API to
  the 1.20.1 `Entity.writeNbt/readNbt` lifecycle. Existing NBT key names are
  unchanged for energy, gain tuning, gender, destroyed-skin state, mess state,
  stored liquid/composition, pregnancy timing/variant, and offspring count.
- Ported `NonLiquidSystem` away from potion data components and registry-entry
  status effects. Bottle tint refresh now writes vanilla `CustomPotionColor`
  NBT, while Filled uses the direct 1.20.1 status-effect API and a safe long
  duration. Capacity enforcement, decay contexts, networking, particles,
  advancement hooks, and accessory modifiers remain connected.
- Ported liquid-bottle consumption and filled/pregnancy movement modifiers.
  Legacy UUID attribute modifiers are replaced atomically each tick using
  `GENERIC_MOVEMENT_SPEED` and `MULTIPLY_TOTAL`; emergency snack, milk, and diet
  streak hooks remain active. Estimated unique compiler errors: 626.
- Ported player/mob energy and gender tracked data from the 1.21
  `DataTracker.Builder` lifecycle to 1.20.1 `startTracking`. Generic tracker
  writes now use typed integer values, and player Mixin self access uses safe
  `(Object)` bridges required by Java's compile-time type rules.
- Repaired the full mob-energy behavior path: nearby-animation gain scaling,
  attack failsafe, energy thresholds, mob/mob and mob/player candidate search,
  same-gender rolls, multi-actor joins, linger navigation, partner cooldowns,
  and typed entity registry lookups now compile on 1.20.1.
- Ported liquid/pregnancy jump slowdown to direct status-effect lookups and
  repaired pregnancy-friendly mob target clearing. These tracker and behavior
  Mixins are compiler-clean; estimated unique errors are now 591.
- Completed the 1.20.1 migration of the 5,500-line `NeedsOfNature` initializer.
  Item/entity registration, lifecycle callbacks, animation selection/joining,
  attack/defeat flows, liquid progression, bottle triggers, pregnancy state,
  offspring spawning/taming, and status effects are compiler-clean. The 1.21
  entity data-component variant system was replaced with a narrow legacy-NBT
  variant bridge for 1.20.1 mobs. Estimated unique project errors: 415; the
  main initializer now contributes zero errors.
- Ported all three external/generated resource-pack implementations to the
  1.20.1 pack-profile and pack-factory API. Generated packs now advertise pack
  format 15, while directory, ordinary ZIP, and nested-prefix ZIP packs retain
  configured ordering, mandatory enablement, and symlink validation.
- Cleaned the public API event bridge plus the attack, gathering, and manual-peak
  gameplay paths. Legacy anger UUIDs, damage/attribute signatures, entity
  registry typing, Java collection accessors, and decompiler-damaged enum
  switches are now 1.20.1-compatible. Estimated unique project errors: 327.
- Repaired the shared ModMenu widget registration pattern across eleven screens,
  removing invalid decompiler-inserted `Element` casts. Gameplay and system
  settings screens are compiler-clean. The large debug screen now uses the
  1.20.1 six-argument list constructors, fixed-height entry API, legacy entry
  render/mouse signatures, scroll API, typed resource lists, and exhaustive enum
  switches. Gender initialization and scoreboard-tag removal are also clean.
  Estimated unique project errors: 160; debug-screen errors fell from 128 to 30.
- Completed the remaining ModMenu UI migration. Debug, HUD layout, player skin
  tint, gameplay, system settings, and the shared settings-list/widget helpers
  are compiler-clean. This includes legacy mouse/key callbacks, ABGR skin pixel
  conversion, dynamic pack icons, fixed-height list entries, and 1.20.1 texture
  drawing. Commands and energy-adjust items are also clean after operator,
  scoreboard-tag, creative-mode, particle, and server-world API fixes.
  Estimated unique project errors: 82.
- Completed the remaining client, gameplay, persistence, advancement, skin,
  resource-pack, and Mixin migrations. Repaired the last decompiler-generated
  exception-flow and uninitialized-variable artifacts. Both modules now compile
  with zero Java errors.
- Ported the complete Animation Director client hook set to 1.20.1: movement
  input locking, first-person actor rendering, camera movement/clipping, and
  interaction blocking now target methods and descriptors present in 1.20.1.
- Ran a clean multi-module `build`. Compilation, resources, Mixin remapping,
  source JARs, remapped runtime JARs, assembly, and checks all completed with
  `BUILD SUCCESSFUL`.
- Verified both runtime JARs contain their Fabric metadata and Mixin configs.
  The final artifacts are:
  - `animationdirector/build/libs/animationdirector-1.20.1-1.3.1-port.1.jar`
  - `needsofnature/build/libs/needsofnature-1.20.1-1.3.1-port.1.jar`

## Remaining verification

1. A full Fabric 1.20.1 client/world smoke test was completed against the user's
   172-mod PCL instance. Animation Director initialized, 3,990 advancements
   loaded, the integrated server started, and a world remained running without
   a NeedsOfNature/Animation Director crash.
2. The build emits two source-remapping notices for the optional debug-spin
   Mixin's Animation Director methods. Those methods exist in the compiled
   Animation Director module and are explicitly marked `remap=false`; the
   runtime JAR remap succeeds.
3. The Java compiler emits three annotation-classpath warnings from a transitive
   Jackson dependency. They are warnings only and do not prevent either module
   from building.
4. The latest null-safe emissive-render fix has passed a clean build. A final
   in-world `M` animation/render test is still recommended because the previous
   interactive verification was interrupted before that exact path completed.

## Runtime fixes from PCL smoke testing

- Removed the inherited `PlayerEntity.getDataTracker` Shadow that failed at
  runtime, and replaced it with a direct typed bridge.
- Corrected 1.20.1 Mixin descriptors for living-entity damage, mob attacks,
  target predicates, mob initialization NBT, camera updates, handled-screen slot
  drawing, and GameRenderer projection FOV.
- Converted the legacy brewing callback to a static injection and the inventory
  mouse handler to a cancellable injection.
- Reconstructed the ripped-skin fallback generator from the original bytecode;
  automatic generated skin upload no longer disables itself with the CFR
  `Decompilation failed` placeholder.
- Added the Forge/Sinytra-compatible GeckoLib `GeoItem` renderer bridge for the
  Horse Liquid Collector while retaining the native Fabric path.
- Replaced the generated resource-pack provider's immutable collection with a
  mutable collection so Forge/Connector can append packs during discovery.
- Marked the optional debug-spin Mixin injections with `require = 0`, preventing
  a missing optional Animation Director hook from aborting startup.
- Made replacement-model emissive texture resolution null-safe. Models that do
  not define an emissive texture no longer crash in `AfwGeckoReplacedRender`.
- Kept the external default ZIP on its original GeckoLib 5 layout and embedded
  the GeckoLib 4 `geo` and `animations` copies only in the mod JAR. External
  GL4 aliases were removed after Forge/Connector selected them over valid JAR
  resources and produced malformed input during parallel initial reload. An
  earlier virtual ZIP mapping attempt was removed for the same reason.
- Added the GeckoLib 4 horse collector model path and corrected AFW animation
  resource IDs, fixing the manual-animation red placeholder and collector
  placement crash reported on Forge/Connector.
- Embedded the default GeckoLib 4 AFW animation/model copies in the mod JAR as
  well as the external pack. This avoids Connector reload-listener ordering
  leaving GeckoLib's cache empty even though the external pack is enabled.
- Deferred complete model-path canonicalization until after model override
  events so player and mob `.f`/`.m` gender model variants can be selected.
- Removed UTF-8 BOM markers from 41 embedded animation/model JSON resources.
  GeckoLib 4's strict Gson reader treated the BOM as malformed JSON at line 1,
  column 1 even though the visible document began with `{`.
- Restored the original default-pack ZIP byte-for-byte after a PowerShell
  recompression made Connector expose the pack profile but lose its item
  textures and all AFW definitions. Root and nested external ZIPs now use the
  same custom resource-pack reader.
- Added and validated Simplified Chinese localization: 685 NeedsOfNature keys
  and 102 Animation Director keys, with format placeholders kept in sync.
- Verified the Forge 47.4.21 + Connector test instance loads 38 animation
  definitions, five accessory definitions, enters a world, and completes the
  dynamic-skin upload path.
- Fixed manual carrot/golden-carrot playback visibility by retaining the
  data-driven `player_manual_peak-carrot` selection and adding a client-side
  held-prop fallback when the override packet arrives before the animation.
- Locked replacement-model body/head orientation to the animation start
  transform. First-person mouse look now rotates only the camera instead of
  writing the camera yaw back into the animated player model.
- Added the missing GeckoLib 4 per-bone item render layer. AFW stage props and
  packet overrides on `propright`/`propleft`, including the carrot manual-peak
  prop, now reach the replacement renderer instead of being discarded.
- Reworked the core Simplified Chinese gameplay terminology and documented the
  exact pregnancy trigger, fertility brewing chain, and energy-adjust item use.

## Compatibility notes

- The port targets Minecraft 1.20.1, Java 17, Fabric Loader 0.16.14, Fabric API
  0.92.9, GeckoLib 4.8.4, Mod Menu 7.2.2, and Trinkets 3.7.2.
- Features introduced after Minecraft 1.20.1 use deliberate fallbacks where no
  vanilla equivalent exists (notably wolf armor and newer data components).
- The original input JARs and the untouched decompiler output remain preserved.

## Recommended smoke-test order

1. Allow Gradle/Loom to finish `downloadAssets`, then run
   `gradlew.bat :needsofnature:runClient`.
2. Verify main-menu load, create a disposable world, and exercise animation
   start/stop, camera/input locking, networking, pregnancy/liquid state, dynamic
   skins, external packs, and Mod Menu screens.
