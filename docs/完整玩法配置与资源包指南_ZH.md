# NeedsOfNature 1.20.1 完整玩法、配置与资源包指南

适用构建：Minecraft 1.20.1、Forge 47.4.21 + Sinytra Connector，也适用于本仓库的原生 Fabric 1.20.1 构建。

本项目由两个模组组成：

- `NeedsOfNature`：性别、性欲、主动遭遇、体液、怀孕、污渍、破损皮肤、饰品、物品和 HUD。
- `Animation Director / Animation Framework`：读取动画定义，选择演员，定位模型，控制阶段、镜头、网络同步与 GeckoLib 渲染。

这是成人向模组。请只在符合法律、平台和服务器规则，且参与者均明确同意的环境中使用。

## 1. 安装与文件位置

### Forge + Connector 必需组件

- Forge 47.4.21
- Sinytra Connector（实测 `1.0.0-beta.49+1.20.1`）
- Connector 对应的 Forgified Fabric API
- GeckoLib Forge 4.8.4
- Trinkets 3.7.2（穿戴防护器和隔膜时必需）
- 本项目的 `animationdirector-1.20.1-1.3.1-port.1.jar`
- 本项目的 `needsofnature-1.20.1-1.3.1-port.1.jar`

目录应类似：

```text
游戏版本目录/
├─ mods/
│  ├─ animationdirector-1.20.1-1.3.1-port.1.jar
│  ├─ needsofnature-1.20.1-1.3.1-port.1.jar
│  └─ 依赖模组……
├─ config/
│  ├─ needsofnature.json
│  └─ animationframework.json
└─ needsofnature/
   └─ needs_of_nature_default_packv1.3.0.zip
```

默认内容包不能只放进普通 `resourcepacks`。模组会从 `游戏版本目录/needsofnature/` 扫描 ZIP 或文件夹，并把同一个包同时加载为客户端资源与服务端数据。

正常启动后，`logs/latest.log` 应出现：

```text
[NoN] Registered 5 startup data-driven accessory items.
[AFW] Loaded 38 AFW definition(s) from datapacks
```

## 2. 第一次进入世界

玩家性别支持：

- `male`：男性
- `female`：女性
- `both`：同时具有男性与女性标签

性别决定可匹配的动画、V/D 饰品槽是否可用、是否能怀孕以及部分模型。默认样例配置为女性。

默认按键：

- `M`：开始玩家手动高潮动画。
- 空手按 `M`：使用普通手动动画。
- 女性角色手持胡萝卜或金胡萝卜按 `M`：优先使用胡萝卜专用动画，并把物品挂到右手道具骨骼。

若按键无反应，先确认玩家在地面、未骑乘、未处于其他动画中、当前性别有匹配定义，且日志显示加载了 38 个定义。

## 3. 核心玩法循环

### 3.1 性欲值

代码和配置中仍使用 `energy`，中文界面将其解释为“性欲”。玩家和生物会随时间积累性欲；附近正在播放的动画、玩家性欲光环、破损阶段和饰品都会改变增长速度。

生物达到足够性欲且找到符合定义的演员、空间与方块后，会尝试进入主动遭遇或普通动画。性欲增幅剂、抑制剂和锁定器可永久改变某只生物的行为。

### 3.2 动画阶段

动画定义通常包含：

- 普通循环阶段：默认进度约 15 秒。
- 高潮阶段：默认进度约 7 秒。
- 收尾或不可高潮阶段：由定义中的 `non_peak`、`use_stage` 和 `allow_join` 控制。

动画开始后人物位置和身体朝向会被锁定，第三人称镜头仍可转动。受到伤害是否中止由 Animation Framework 的伤害策略决定。

### 3.3 主动遭遇与挣脱

高性欲生物可能主动接近并触发遭遇。HUD 出现挣脱条后，持续进行普通攻击操作可累计挣脱进度：

- 默认需要 12 次有效操作。
- 进度默认每秒衰减 0.2。
- 挣脱后有 3 秒无敌和动画保护时间。
- 创造模式玩家默认不会被主动攻击。

防护器会阻止对应 V/A 类型的插入动画，并消耗耐久。

## 4. 体液系统

玩家默认拥有 200 ml 的体内储槽。带 `injector` 标记的演员在高潮时会向接收者增加对应来源与颜色的体液。

- `V`：阴道接收槽。
- `A`：肛门接收槽。
- `M`：口部/其他定义使用的接收类型。

储槽超过 30%、50%、80% 时分别进入三个“被灌满”阶段，并按配置降低移动和跳跃能力。体液会按秒自然流失；潜行、泡水和饰品可能改变流失速度。

手持玻璃瓶使用可触发装瓶动画：

- 单一来源得到“某生物的精液瓶”。
- 多个来源混合后得到“混合精液瓶”。
- 精液瓶可用于药水、性欲调节剂和生物性欲锁定器。

## 5. 怀孕与生产

### 5.1 正常受孕条件

必须同时满足：

1. `pregnancyEnabled` 为 `true`。
2. 玩家性别包含女性；仅男性不能怀孕。
3. 玩家尚未怀孕。
4. 玩家没有装备能阻止怀孕的避孕隔膜。
5. 动画中存在非玩家生物，并且该演员带 `injector: "V"`。
6. 动画进入高潮判定。
7. 随机受孕判定成功。

默认受孕概率为 5%，胎儿/后代来源取自完成阴道内射的生物。默认妊娠 3 分钟，结束后播放生产动画，按实体配置直接生成后代或产下待孵化的蛋。

### 5.2 易孕药水

- 易孕药水 I：受孕概率至少提高到 80%。
- 易孕药水 II：受孕概率至少提高到 95%。
- 精液蜂蜜饮剂：任意本模组精液瓶 + 蜂蜜瓶，无序合成。
- 易孕药水：精液蜂蜜饮剂在酿造台加入鸡蛋。
- 强效版本：加入萤石粉。
- 延长版本：加入红石。

### 5.3 管理员测试命令

```text
/needsofnature pregnancy get [玩家]
/needsofnature pregnancy set <玩家> <实体ID> [分钟] [后代数量]
/needsofnature pregnancy complete [玩家]
/needsofnature pregnancy clear [玩家]
```

例：

```text
/needsofnature pregnancy set Steve minecraft:wolf 1 2
```

## 6. 物品、药水与配方

### 6.1 催情花粉混合物

3 朵任意小型花无序合成。它只是合成材料，不能穿戴或直接对生物使用。

### 6.2 性欲增幅剂

拿在手里对生物右键：

- 永久把该生物的性欲自然增长倍率增加 0.2。
- 上限为 6.0。
- 若生物被锁定，会同时解除锁定。
- 非创造模式消耗一个。

无序配方：一瓶精液 + 蜂巢 + 糖 + 催情花粉混合物 + 绯红菌。

### 6.3 性欲抑制剂

对生物右键，永久把增长倍率降低 0.2，最低为 0.4；非创造模式消耗一个。

配方与增幅剂相同，但使用诡异菌。

### 6.4 生物性欲锁定器

成品名称会显示绑定的生物，只能对同种生物使用。成功后把性欲压到 199 以下、清除攻击/愤怒目标并写入锁定标记。使用性欲增幅剂可解除。

3×3 配方：

```text
同源精液瓶 | 诡异菌     | 同源精液瓶
性欲抑制剂 | 发光浆果   | 性欲抑制剂
同源精液瓶 | 诡异菌     | 同源精液瓶
```

四个角必须来自同一种生物。

### 6.5 药水

- 催情药水：精液瓶 + 绯红菌。
- 性欲抑制药水：精液瓶 + 诡异菌。
- 萤石粉制作强效版，红石制作延长版。
- 可继续制成喷溅、滞留药水和药箭。

### 6.6 马用液体收集器

对方块表面使用，在命中位置放置收集实体。位置需要足够空间。它用于马相关收集动画，不是护甲，也不能放入饰品槽。

## 7. Trinkets 饰品与防护用品

打开物品栏中的 Trinkets 面板，将物品拖入对应槽：

- `V`：阴道槽，仅性别包含女性时可用。
- `A`：肛门槽，所有允许性别均可用。
- `D`：男性器官槽，仅性别包含男性时可用。

默认内容包注册：

- 阴道防护器：放 V 槽，阻止 V 类插入；8 点耐久。
- 肛门防护器：放 A 槽，阻止 A 类插入；8 点耐久。
- 阴道/肛门双重防护器：可从 V 或 A 槽装备，但同时占用 V+A，阻止两类插入；8 点耐久。
- 避孕隔膜：放 V 槽，阻止受孕；触发一次对应效果后损坏。
- 铁制肛塞：默认未显式填写 `trinkets_slots`，当前加载器会回退为 V/A 两槽均可装备。它把体液自然流失降至 25%，但把玩家性欲增长提高到 3 倍。

防护器属于互斥组，不能同时穿戴多个；双重防护器也不能与单槽防护器叠加。

## 8. 污渍、破损皮肤和护甲脱落

动画可分别积累 V/A/M 三处体表污渍。`messSystemEnabled` 控制系统总开关。

破损皮肤有 0–4 阶段。普通伤害默认有 10% 概率增加破损，破损越严重，对附近生物的性欲光环越强。

修复方法：

- 靠近织布机，打开物品栏并点击修复按钮。
- 开启 `allowCraftingTableSkinRepair` 后，工作台也能作为修复站。
- `destroyedSkinTintHex` 可指定回退破损皮肤颜色，例如 `"D8A078"`；空字符串表示自动取色。

管理员命令：

```text
/needsofnature skin mess clean [玩家]
/needsofnature skin mess set <V值> <A值> <M值> [玩家]
/needsofnature skin ripped info
/needsofnature skin ripped set <0-4> [玩家]
/needsofnature skin ripped reset [玩家]
```

## 9. 如何安全修改配置

配置文件：

```text
游戏版本目录/config/needsofnature.json
游戏版本目录/config/animationframework.json
```

推荐流程：

1. 完全退出游戏和内置服务器。
2. 备份两个 JSON。
3. 使用 VS Code、Notepad++ 等纯文本编辑器，以 UTF-8 保存。
4. JSON 的字符串必须有双引号；布尔值只能写 `true`/`false`；最后一项后不能多逗号。
5. 修改后先用 JSON 校验器或编辑器检查语法。
6. 重新启动游戏。服务端玩法配置由房主/服务器决定，连接远程服务器时客户端本地值可能被服务端同步覆盖。

错误示例：

```json
{
  "pregnancyEnabled": True,
  "pregnancyChancePercent": 100,
}
```

正确示例：

```json
{
  "pregnancyEnabled": true,
  "pregnancyChancePercent": 100
}
```

## 10. NeedsOfNature 配置字段说明

下列默认值来自仓库的 Forge/Connector 实测样例。

### 10.1 性欲与扫描

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `initialEnergyMax` | 90 | 生物首次初始化时随机性欲的上限。 |
| `energyGainRate` | 1.0 | 全局自然增长倍率。 |
| `nearAnimationEnergyGainMult` | 3.0 | 生物目睹附近动画时的增长倍率。 |
| `playerEnergyAuraMultLow/Mid/High` | 2/5/10 | 玩家性欲处于 100–149、150–199、200 时对附近生物的倍率。 |
| `playerEnergyAuraRadiusMin` | 6 | 玩家性欲 100 时的光环半径。 |
| `playerEnergyAuraRadiusMax` | 20 | 玩家性欲 200 时的光环半径；中间线性变化。 |
| `playerEnergyAuraPulseTicks` | 20 | 光环应用间隔；20 tick 约 1 秒。 |
| `destroyedSkinAuraMultStage1` | 2.0 | 破损第 1 阶段的额外吸引倍率。 |
| `destroyedSkinAuraMultStage2` | 5.0 | 破损第 2 阶段的额外吸引倍率。 |
| `destroyedSkinAuraMultStage3` | 20.0 | 破损第 3 阶段的额外吸引倍率。 |
| `destroyedSkinAuraMultStage4` | 50.0 | 破损第 4 阶段的额外吸引倍率。 |
| `normalDamageDestroyedSkinChancePercent` | 10 | 普通受伤增加破损的概率。 |
| `scanBudgetPerTick` | 32 | 每 tick 最多处理的扫描工作量；提高会更及时但更耗性能。 |
| `lastDefeatedEnabled` | true | 启用最近落败后的补充流程。 |
| `lastDefeatedEnergyThreshold` | 70 | 触发该流程所需最低性欲。 |
| `lastDefeatedSearchRadius` | 6 | 搜索半径。 |
| `lastDefeatedCooldownSeconds` | 120 | 冷却秒数。 |

### 10.2 动画时长与匹配概率

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `loopProgressSeconds` | 15 | 普通循环阶段的全局目标时长。 |
| `peakLoopProgressSeconds` | 7 | 高潮循环阶段的全局目标时长。 |
| `maleMaleChancePercent` | 20 | 男性-男性候选组合概率。 |
| `femaleFemaleChancePercent` | 20 | 女性-女性候选组合概率。 |
| `entityEntityChancePercent` | 20 | 生物-生物组合概率。 |
| `multiActorJoinChancePercent` | 75 | 多演员加入概率。 |
| `blockAnimationsWhileRidingLivingEntities` | true | 骑乘活体时阻止动画。 |
| `blockAnimationsWhileRidingVehicles` | false | 坐船/矿车等载具时是否阻止。 |
| `disabledAnimationPacks` | [] | 禁用的包 ID，例如 `"author:pack"`。 |
| `disabledAnimations` | [] | 禁用的完整动画 ID，例如 `"animationframework:wolfmplayer"`。 |

### 10.3 性别与原版繁殖

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `playerGender` | `female` | 本地/默认玩家性别：`male`、`female`、`both`。 |
| `allowPlayerGenderChangeAnytime` | true | 允许以后修改性别。 |
| `requirePlayerGenderSelectionOnJoin` | false | 首次进入时是否强制选择。 |
| `allowedStartingGenderMask` | 7 | 起始性别位掩码；7 表示三类都允许。 |
| `genderMaleChancePercent` | 48 | 新生物为男性的概率。 |
| `genderFemaleChancePercent` | 47 | 新生物为女性的概率。 |
| `genderBothChancePercent` | 5 | 新生物同时具有两类性别标签的概率；三项应合计 100。 |
| `genderSpawnByEntity` | {} | 按实体覆盖性别分布。 |
| `vanillaOverridesEnabled` | true | 启用原版行为覆盖。 |
| `useAnimationBreeding` | true | 原版繁殖改用动画流程。 |
| `requireMaleFemaleForBreeding` | true | 原版繁殖要求一雄一雌。 |
| `femaleCowOnlyMilking` | true | 只有雌性牛可挤奶。 |
| `convertMaleOnlyVInjectionsToA` | true | 仅男性接收者遇到 V 定义时转换为 A。 |

### 10.4 系统开关与死亡保留

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `pregnancyEnabled` | true | 怀孕总开关。 |
| `liquidTankEnabled` | true | 体液储槽总开关。 |
| `messSystemEnabled` | true | 体表污渍总开关。 |
| `destroyedSkinSystemEnabled` | true | 破损皮肤总开关。 |
| `keepMessAfterDeath` | false | 死亡后保留污渍。 |
| `keepRippedSkinAfterDeath` | false | 死亡后保留破损。 |
| `keepLiquidTankAfterDeath` | false | 死亡后保留体液和来源。 |
| `keepPregnancyAfterDeath` | false | 死亡后继续妊娠。 |
| `allowCraftingTableSkinRepair` | false | 允许工作台修复皮肤。 |
| `armorShedEffectEnabled` | true | 动画开始时显示护甲脱落视觉效果。 |

### 10.5 主动遭遇与挣脱

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `attackCreativePlayers` | false | 是否攻击创造模式玩家。 |
| `attackEscapeHits` | 12 | 成功挣脱所需有效操作次数。 |
| `attackDecayPerSecond` | 0.2 | 挣脱进度每秒衰减量。 |
| `attackEscapeDamageDifficultyPercent` | 100 | 挣脱伤害难度百分比。 |
| `attackEscapeInvulnerabilitySeconds` | 3 | 挣脱后的无敌时间。 |
| `attackEscapeAnimationProtectionSeconds` | 3 | 挣脱后的动画保护时间。 |
| `postEscapeGatherMaxMobs` | 8 | 挣脱后最多纳入流程的附近生物数。 |
| `attackOutcomeFailsafeThreshold` | 3 | 攻击结果异常时的保险阈值。 |

### 10.6 怀孕

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `pregnancyChancePercent` | 5 | 基础受孕概率，0–100。 |
| `pregnancyDurationMinutes` | 3 | 妊娠持续的现实分钟数。 |
| `pregnancyChanceByEntity` | {} | 按内射生物 ID 覆盖概率，例如 `{"minecraft:wolf": 100}`。 |
| `pregnancyOffspringCountByEntity` | {} | 按实体覆盖后代数量范围。 |
| `pregnancyEggDefaultHatchSeconds` | 60 | 蛋的默认孵化秒数。 |
| `pregnancyEggEnabledByEntity` | {} | 按实体控制是否使用蛋。 |
| `pregnancyEggHatchSecondsByEntity` | {} | 按实体覆盖孵化时间。 |
| `pregnancyAutoTameMobs` | true | 可驯服后代自动归属玩家。 |
| `pregnancyFriendlyMobsIgnorePlayers` | false | 敌对后代是否忽略所有玩家。 |
| `entityProfilesByEntity` | {} | 完整实体生产档案覆盖；优先用于高级定制。 |

### 10.7 体液与被灌满惩罚

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `liquidTankCapacityMl` | 200 | 最大容量。 |
| `liquidDecayPerSecond` | 1.0 | 每秒自然流失 ml。 |
| `liquidPuddleDespawnSeconds` | 40 | 地面体液粒子的存在时间。 |
| `peakXpPerMl` | 0.0666667 | 每 ml 高潮体液对应的经验换算。 |
| `filledStageOneSpeedMult` | 0.8 | 30% 以上移动倍率。 |
| `filledStageTwoSpeedMult` | 0.7 | 50% 以上移动倍率。 |
| `filledStageThreeSpeedMult` | 0.6 | 80% 以上移动倍率。 |
| `filledStageOneJumpMult` | 0.9 | 第一阶段跳跃倍率。 |
| `filledStageTwoJumpMult` | 0.75 | 第二阶段跳跃倍率。 |
| `filledStageThreeJumpMult` | 0.6 | 第三阶段跳跃倍率。 |
| `liquidGainByEntity` | {} | 按实体覆盖每次增加的 ml。 |
| `liquidColorByEntity` | {} | 按实体覆盖颜色，如 `"#51E04C"`。 |

### 10.8 HUD、声音与调试

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `uiEnergyOffsetX` / `uiEnergyOffsetY` | 0/0 | 性欲进度条横向/纵向偏移。 |
| `uiEnergyHeartOffsetX` / `uiEnergyHeartOffsetY` | 0/0 | 心形状态横向/纵向偏移。 |
| `uiAttackOffsetX` / `uiAttackOffsetY` | 0/0 | 挣脱条横向/纵向偏移。 |
| `uiPromptOffsetX` / `uiPromptOffsetY` | 0/0 | 操作提示横向/纵向偏移。 |
| `uiLiquidOffsetX` / `uiLiquidOffsetY` | 0/0 | 体液条横向/纵向偏移。 |
| `uiEnergyVisible` | true | 显示性欲进度。 |
| `uiEnergyHeartVisible` | true | 显示心形状态。 |
| `uiAttackVisible` | true | 显示挣脱条。 |
| `uiPromptVisible` | true | 显示提示。 |
| `uiLiquidVisible` | true | 显示体液条。 |
| `actionSoundVolumePercent` | 100 | 动作音量百分比。 |
| `debugChatMode` | `setup_errors` | 调试聊天级别。正常游玩保留此值。 |
| `debugSpinMode` | false | 模型调试旋转模式。正常游玩必须关闭。 |

### 10.9 女性模型联动

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `syncFemaleGenderModWithNonGender` | true | 与兼容的女性模型模组同步性别。 |
| `destroyedSkinFemaleGenderOverridesEnabled` | true | 破损阶段覆盖女性模型参数。 |
| `femaleGenderDestroyedLow` | 对象 | 破损 0–1 阶段的胸部尺寸、位置、旋转与物理参数。 |
| `femaleGenderDestroyedHigh` | 对象 | 破损 2–4 阶段参数。 |
| `destroyedSkinTintHex` | `""` | 回退破损皮肤颜色；空值自动取色。 |

两个对象内字段：`breastSize`、`separation`、`height`、`depth`、`rotation`、`breastPhysics`、`dualPhysics`、`intensity`、`momentum`。

### 10.10 Buttplug.io / Intiface

`intifaceEnabled` 默认为 `false`。不使用外设时保持关闭即可，日志中的“integration is unavailable”不会影响游戏。

| 字段组 | 默认值/说明 |
|---|---|
| `intifaceServerUrl` | `ws://127.0.0.1:12345` |
| `intifaceAutoConnectOnWorldJoin` | 加入世界自动连接。 |
| `intifaceStopOnDisconnect` | 断开时停止输出。 |
| `intifaceReactiveImpactStrengthPercent` / `intifaceReactiveImpactDurationMs` | 普通冲击强度 60%、170 ms。 |
| `intifacePeakReactiveImpactStrengthPercent` / `intifacePeakReactiveImpactDurationMs` | 高潮冲击 20%、500 ms。 |
| `intifaceCooldownMs` | 输出冷却 80 ms。 |
| `intifaceAnimationBaselineStrengthPercent` | 动画基线 1%。 |
| `intifaceOscillatorRegularSpeedPercent` / `intifaceOscillatorPeakSpeedPercent` | 振荡器普通/高潮速度 100%/50%。 |
| `intifaceStrokerMinDistancePercent` / `intifaceStrokerMaxDistancePercent` | 行程范围 0%–100%。 |
| `intifaceStrokerRegularMoveDurationMs` / `intifaceStrokerPeakMoveDurationMs` | 普通/高潮移动时间 500/300 ms。 |
| `intifaceEnergizedOneBasePercent` / `intifaceEnergizedOnePulsePercent` | 第一档基线/脉冲 1%/5%。 |
| `intifaceEnergizedTwoBasePercent` / `intifaceEnergizedTwoPulsePercent` | 第二档基线/脉冲 5%/10%。 |
| `intifaceEnergizedThreeBasePercent` / `intifaceEnergizedThreePulsePercent` | 第三档基线/脉冲 10%/20%。 |

### 10.11 外部内容包管理

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `externalNoNPackLoadOrder` | [] | 外部包加载顺序列表。 |
| `disabledAnimationPacks` | [] | 按 `pack.mcmeta` 中的动画包 ID 禁用整个包。 |
| `disabledAnimations` | [] | 按完整动画 ID 禁用单个定义。 |
| `acknowledgedStartupWarnings` | 列表 | 已确认的启动警告；不要随意复制别人的列表来隐藏真实错误。 |

## 11. Animation Framework 配置

`config/animationframework.json`：

| 字段 | 默认值 | 说明 |
|---|---:|---|
| `forceVanillaEntityTextures` | false | 强制使用原版实体贴图，忽略内容包贴图覆盖。 |
| `debugChatMode` | `SETUP_ERRORS` | AFW 调试消息级别。 |
| `debugDamageBehavior` | `STOP_ON_DAMAGE` | 调试启动动画受到伤害时的策略。 |
| `debugIgnoreAttackers` | false | 调试动画是否忽略攻击者。 |
| `anchorAtLastSelected` | false | 调试选择多个演员时是否锚定最后选择者。 |
| `blockSearchRadius` | 3 | 寻找床、墙、台面等方块条件的半径。 |
| `autoSwitchThirdPersonOnAnimationStart` | true | 动画开始自动切到第三人称。 |

## 12. 常用配置模板

### 只看动画，关闭惩罚

```json
{
  "pregnancyEnabled": false,
  "liquidTankEnabled": false,
  "messSystemEnabled": false,
  "destroyedSkinSystemEnabled": false,
  "attackCreativePlayers": false,
  "filledStageOneSpeedMult": 1.0,
  "filledStageTwoSpeedMult": 1.0,
  "filledStageThreeSpeedMult": 1.0,
  "filledStageOneJumpMult": 1.0,
  "filledStageTwoJumpMult": 1.0,
  "filledStageThreeJumpMult": 1.0
}
```

只把这些字段改进现有文件，不要用片段覆盖整个 JSON。

### 百分百快速怀孕测试

```json
{
  "pregnancyEnabled": true,
  "pregnancyChancePercent": 100,
  "pregnancyDurationMinutes": 1,
  "pregnancyEggDefaultHatchSeconds": 10
}
```

### 状态死亡后保留

```json
{
  "keepMessAfterDeath": true,
  "keepRippedSkinAfterDeath": true,
  "keepLiquidTankAfterDeath": true,
  "keepPregnancyAfterDeath": true
}
```

### 降低主动遭遇频率

```json
{
  "energyGainRate": 0.2,
  "nearAnimationEnergyGainMult": 1.0,
  "playerEnergyAuraMultLow": 1.0,
  "playerEnergyAuraMultMid": 1.0,
  "playerEnergyAuraMultHigh": 1.0
}
```

## 13. 默认内容包是什么

`needs_of_nature_default_packv1.3.0.zip` 不是普通材质包。它包含：

```text
pack.mcmeta                         包信息、ID、作者和版本
pack.png                            包图标
data/animationframework/afw_animdefs/
                                     动画匹配和阶段定义
data/needsofnature/non_entity_profiles/
                                     怀孕后代、生产模式与蛋配置
data/needsofnature/non_liquid_gains/
                                     各生物体液量与颜色
data/needsofnature/recipe/           饰品配方
data/trinkets/tags/item/legs/        V/A/D 饰品标签
assets/animationframework/geckolib/  原版包的 GeckoLib 5 模型/动画源布局
assets/needsofnature/non_accessory_items/
                                     数据驱动饰品定义
assets/needsofnature/textures/       贴图、污渍、蛋和破损资源
assets/minecraft/optifine/cem/       原包附带的 CEM 资源
```

本移植版把默认包需要的 GeckoLib 4 `geo/` 与 `animations/` 副本内置进 Animation Director JAR。默认 ZIP 必须保持原始字节结构，不要使用 PowerShell `Compress-Archive` 重新压缩；实测它会让 Connector 显示包已加载，但丢失饰品贴图或全部 AFW 定义。

## 14. 自制或修改内容包

### 14.1 安全做法

1. 复制默认 ZIP 作为备份，不要直接改唯一副本。
2. 解压到一个独立文件夹。
3. 修改文件后使用 7-Zip 等工具打包，确保 `pack.mcmeta`、`assets/`、`data/` 位于 ZIP 根目录，而不是多套一层文件夹。
4. 换一个文件名放入 `游戏版本目录/needsofnature/`。
5. 为自制包设置唯一 `animationframework.id`。
6. 启动后检查加载数量和 setup warning。

允许一层包装目录，但正式发布时应把内容直接放在根目录。

### 14.2 `pack.mcmeta`

建议以默认包为模板，只改动画包元数据：

```json
{
  "pack": {
    "pack_format": 15,
    "description": "我的 NeedsOfNature 内容包"
  },
  "animationframework": {
    "id": "myname:mypack",
    "name": "我的动画包",
    "author": "作者名",
    "version": "1.0.0",
    "description": "内容说明"
  }
}
```

`id` 必须是唯一的命名空间 ID，不能与默认包重复。

### 14.3 动画定义

位置：

```text
data/<命名空间>/afw_animdefs/<动画名>.json
```

最小示例：

```json
{
  "actors": [
    {
      "entity_types": ["minecraft:player"],
      "actor_tags": ["gender.female"],
      "activity": "passive",
      "receiver": true
    },
    {
      "entity_types": ["minecraft:wolf"],
      "actor_tags": ["gender.male"],
      "activity": "active",
      "injector": "V"
    }
  ],
  "content_tags": ["doggy"],
  "weight": 1.0,
  "stages": [
    {
      "stage": 1,
      "loop": true,
      "cycle_seconds": 0.5,
      "allow_join": true,
      "escapable": true
    },
    {
      "stage": 2,
      "loop": true,
      "cycle_seconds": 0.5,
      "speed": 0.5,
      "allow_join": false,
      "escapable": false,
      "non_peak": true
    }
  ]
}
```

常用顶层字段：

- `actors`：演员约束列表。
- `animation_tags`：功能标签，如 `manualPeak`、`fillBottle`。
- `content_tags`：姿势/内容分类。
- `required_union_tags`：组合匹配要求。
- `weight`：同等候选中的随机权重。
- `speed`：默认播放速度。
- `block_requirements`：墙、台面、支撑或指定方块条件。
- `water`：水中要求。
- `position_anchor_actor`：定位锚点演员。
- `manual_peak`：手持物触发规则。
- `liquid_gain_multiplier`：此动画体液量倍率。

演员字段：

- `label`：显式演员键。
- `entity_types`：允许的实体 ID。
- `entity_variant`：实体变体。
- `actor_tags` / `actor_tags_any`：性别等标签。
- `age`：`adult` 或 `baby`；默认 `adult`。
- `activity`：`active` 或 `passive`。
- `injector`：`V`、`A`、`M` 或 `false`。
- `receiver`：是否为体液接收者。
- `prop_left` / `prop_right`：默认左右手物品 ID。

阶段字段：

- `stage`：从 1 开始的阶段号。
- `use_stage`：复用另一阶段的动画资源。
- `loop`：是否循环。
- `cycle_seconds`：作者动画循环长度，必须尽量匹配 GeckoLib 文件。
- `speed`：阶段速度。
- `allow_join`：是否允许其他演员加入。
- `cycle_midpoint_offset_seconds`：循环中点偏移。
- `props`：按演员键覆盖左右手道具。
- `escapable`：此阶段能否挣脱。
- `non_peak`：此阶段不执行高潮结算。

### 14.4 动画资源命名

定义 ID 由路径决定。例如：

```text
data/mypack/afw_animdefs/example.json
```

定义 ID 为 `mypack:example`，阶段资源 ID 为 `mypack:example.p1`、`mypack:example.p2`。

本 1.20.1 移植使用 GeckoLib 4，实际运行资源必须提供：

```text
assets/mypack/animations/afw/example/example.p1_player.animation.json
assets/mypack/animations/afw/example/example.p1_wolf.animation.json
```

原 1.21/GeckoLib 5 包的源布局是：

```text
assets/mypack/geckolib/animations/afw/example/...
```

默认包能只保留后者，是因为本项目已把它的 GeckoLib 4 副本编进 JAR。新制作的外部包不能依赖这个默认包专用副本，必须自行提供 GeckoLib 4 的 `assets/<命名空间>/animations/` 和 `assets/<命名空间>/geo/` 资源；若还要兼容原版 1.21，可同时保留两套目录。

### 14.5 手持物专用手动动画

```json
{
  "animation_tags": ["manualPeak"],
  "manual_peak": {
    "held_items": ["minecraft:carrot", "minecraft:golden_carrot"],
    "priority": 100,
    "prop_from_held_item": true
  }
}
```

`priority` 越高越先匹配；`prop_from_held_item` 会把匹配到的物品发送给客户端，并挂到 `propright` 骨骼。

### 14.6 生物怀孕档案

位置：

```text
data/needsofnature/non_entity_profiles/*.json
```

示例：

```json
{
  "entries": [
    {
      "entity": "minecraft:spider",
      "pregnancy_chance_percent": 25,
      "offspring_min": 1,
      "offspring_max": 3,
      "birth_entity": "minecraft:spider",
      "birth_mode": "egg",
      "egg": {
        "start_size": 0.5,
        "end_size": 1.0,
        "texture": "mypack:textures/entity/pregnancy_egg/spider.png",
        "health": 30
      },
      "gender_spawn": {
        "male_chance": 100,
        "female_chance": 0,
        "both_chance": 0
      }
    }
  ]
}
```

`birth_mode` 可为 `direct` 或 `egg`。

### 14.7 体液量与颜色

位置：

```text
data/needsofnature/non_liquid_gains/*.json
```

```json
{
  "mixed_color": "F2EBBF",
  "entries": [
    {
      "entity": "minecraft:wolf",
      "gain_ml": 30,
      "color": "#F2EBBF"
    }
  ]
}
```

`color` 可省略；颜色使用 6 位 RGB 十六进制。

### 14.8 数据驱动饰品

位置固定为：

```text
assets/needsofnature/non_accessory_items/*.json
```

饰品 ID 目前必须使用 `needsofnature` 命名空间。示例：

```json
{
  "id": "my_protector",
  "max_count": 1,
  "max_durability": 8,
  "show_in_item_group": true,
  "trinkets_slots": ["legs/v"],
  "occupies_slots": ["legs/v"],
  "blocks_injector_types": ["V"],
  "exclusive_group": "protector",
  "protection_durability_cost": 1,
  "item_texture": "needsofnature:item/my_protector"
}
```

槽位可写 `legs/v`、`legs/a`、`legs/d`，也可简写 `v`、`a`、`d`。若完全省略 `trinkets_slots`，当前加载器回退为 V 和 A 两槽。

可用效果包括体液流失、玩家性欲增长、容量、体液增加量、灌满惩罚、受孕概率/持续时间、污渍、破损伤害、挣脱难度和性欲光环等倍率。

## 15. 内容包加载、覆盖与禁用

- 外部 NoN 包优先于模组基础资源。
- 同名资源由更高优先级包覆盖。
- `externalNoNPackLoadOrder` 控制多个外部包的相对顺序。
- `disabledAnimationPacks` 按 `animationframework.id` 禁用整个包。
- `disabledAnimations` 按定义 ID 禁用单个动画。
- 修改服务端数据后最稳妥的方法是完全重启游戏；`/reload` 只适合纯数据修改，不能重新注册启动期饰品物品。

饰品定义属于启动期注册数据：新增或删除饰品后必须完全重启，且删除已经进入存档的注册物可能造成缺失映射，务必先备份世界。

## 16. 常见故障

### 按 M 没反应

- 玩家不在地面、正在骑乘或已有动画。
- 按键冲突。
- 性别没有匹配定义。
- 默认包位置错误，日志显示 `Loaded 0 AFW definition(s)`。

### 动画出现红色方块

模型或动画资源没进入 GeckoLib 4 缓存。检查 `assets/<namespace>/geo/` 与 `assets/<namespace>/animations/` 路径、文件名和 JSON 是否含 UTF-8 BOM。

### 胡萝卜动画不显示胡萝卜

确保使用包含 `AfwBoneItemGeoLayer` 的最新 Animation Director JAR，并删除旧 JAR；Forge/Connector 下还要让 `.connector` 缓存重建。

### 饰品无法装备

- 截图中的花粉、增幅剂、抑制剂、锁定器不是穿戴装备。
- V 槽要求玩家性别包含女性，D 槽要求包含男性。
- 双重防护器会占用 V+A，已有单槽防护器时无法再装备。
- 确认 Trinkets 已加载，日志没有“Trinkets is not installed”。

### 饰品紫黑贴图

默认包 ZIP 被错误重打包或资源根目录多套层级。恢复仓库中原始 ZIP。

### 改配置后无效

- 修改了错误实例的 `config`。
- 游戏仍在运行，退出时把旧值写回。
- 连接远程服务器，服务端配置覆盖客户端。
- JSON 语法错误，模组回退默认值或重建文件。

### 日志出现 Buttplug.io 不可用

这是可选外设功能。`intifaceEnabled=false` 时可忽略。

## 17. 分享与版权说明

这是非官方 1.20.1 逆向移植。原作、模型、动画与默认资源的权利归原作者 **L1Z0**、Incarnadine 及各自权利人所有。分享代码或构建时应保留原作者声明、原发布页链接和本项目的非官方说明：

<https://www.loverslab.com/files/file/49713-minecraft-needs-of-nature-nsfw-mod-data-driven-sex-animation-mod-for-minecraft-fabric-12111/>

不要把本指南当作重新授权；分发前仍需遵守原发布页许可、平台规则和当地法律。
