# NeedsOfNature 1.20.1 中文安装与玩法教程

适用版本：Minecraft 1.20.1，Forge 47.4.21 + Sinytra Connector。

本教程对应的中文构建包含两个模组：

- `NeedsOfNature`：玩法、能量、液体储槽、怀孕、污渍、破损皮肤、饰品等系统。
- `Animation Director / Animation Framework`：负责动画匹配、演员定位、阶段切换和网络同步。

> 模组包含成人向内容。请只在符合当地法律、服务器规则以及所有参与者明确同意的环境中使用。

## 一、安装

压缩包只包含上述两个模组及其配置/内容包，不包含第三方依赖。Forge + Connector 环境还需要自行准备：

- Sinytra Connector（测试版本：`1.0.0-beta.49+1.20.1`）
- Connector 对应的 Forgified Fabric API
- GeckoLib Forge 4.8.4
- Trinkets 3.7.2（需要饰品槽和防护用品时必须安装）

安装方法：

1. 关闭 Minecraft。
2. 把压缩包内的 `mods`、`config`、`needsofnature` 三个文件夹复制到当前游戏版本目录。
3. 允许合并文件夹并覆盖旧版的两个同名模组 JAR。
4. 不要把默认内容包放进普通 `resourcepacks` 文件夹；它应位于游戏目录下的 `needsofnature` 文件夹。
5. 启动游戏，并在“选项 → 语言”中选择“简体中文”。

正确加载时，日志应能看到类似内容：

```text
[NoN] Registered 5 startup data-driven accessory items.
[AFW] Loaded 38 AFW definition(s) from datapacks
```

## 二、第一次进入世界

进入世界后，模组会根据配置确定玩家性别：男性、女性或两者。该选择影响可匹配动画、模型和部分玩法条件。当前随包配置默认是女性，并允许以后修改。

Forge/Connector 版暂时建议直接编辑配置文件：

- `config/needsofnature.json`
- `config/animationframework.json`

编辑前先退出游戏，保存时保持标准 JSON 格式。不要删除引号、逗号或大括号。

## 三、最基本的玩法循环

### 1. 生物性欲（代码中仍叫 Energy）

生物会随时间积累性欲。附近正在播放的性交动画、玩家自身较高的性欲和衣物/皮肤破损等因素，会让周围生物更快发情。达到匹配条件后，生物会尝试寻找合适的玩家、生物、场地和动画。

HUD 上出现的能量、进度或提示条用于显示当前状态。若不需要某一部分，可在 `needsofnature.json` 中修改 `ui...Visible` 项。

### 2. 动画匹配

默认内容包提供 38 套动画定义，覆盖玩家与多种原版生物。一次动画能否开始取决于：

- 参与者类型和性别标签是否匹配；
- 生物能量是否达到要求；
- 周围是否有足够空间或指定方块；
- 玩家是否骑乘、已处于动画中或受到保护；
- 对应动画是否在配置中禁用。

动画一般会自动经历普通阶段和高潮阶段。默认普通阶段约 15 秒，高潮阶段约 7 秒，可通过 `loopProgressSeconds` 和 `peakLoopProgressSeconds` 调整。

### 3. 手动动画

默认按键 `M` 为“开始手动高潮”。可在“选项 → 控制”里的“自然需求”分类修改。

- 空手时会根据玩家性别选择对应的手动动画。
- 女性角色手持胡萝卜或金胡萝卜时，会优先使用带手持道具的动画。
- 若按键无反应，检查玩家是否正在骑乘、动画资源是否加载、当前性别是否有匹配定义，以及按键是否冲突。

Animation Framework 的调试按键默认全部未绑定。普通游玩不需要设置；它们主要用于动画包制作和故障排查。

## 四、液体储槽

玩家默认拥有 200 ml 容量的液体储槽。不同生物和动画会产生不同数量、颜色的液体；储槽越满，移动速度和跳跃能力受到的影响越明显。

主要操作：

- 手持玻璃瓶按使用键，可在满足条件时把储槽内容装瓶。
- 不同来源混合后会生成“混合液体瓶”；单一来源可能生成对应生物的液体瓶。
- 储槽内容会按 `liquidDecayPerSecond` 逐渐衰减。
- 潜行、处于水中以及装备特定饰品会影响衰减速度。
- 默认死亡后不保留储槽；可修改 `keepLiquidTankAfterDeath`。

铁塞会显著降低液体衰减，但会提高玩家能量增长速度，属于有明显取舍的装备。

## 五、饰品和防护用品

该部分需要安装 Trinkets。默认内容包会注册 5 件饰品，并生成 V、A、D 三类槽位。

### 合成表

隔膜（无序合成）：

- 黏土球 ×1
- 品红色染料 ×1

铁塞（横向有序）：

```text
钻石 | 铁粒
```

V 型防护器：

```text
铁锭 | 金粒 | 铁锭
铁锭 | 铁锭 | 铁锭
     | 铁锭 |
```

A 型防护器：

```text
铁锭 | 铜粒 | 铁锭
铁锭 | 铁锭 | 铁锭
     | 铁锭 |
```

A/V 型防护器：

```text
金锭 | 绿宝石 | 金锭
金锭 | 金锭   | 金锭
     | 金锭   |
```

用途：

- V/A 防护器阻止对应类型的动画效果，耐久耗尽后失效。
- A/V 防护器同时占用 A 和 V 槽并提供双重保护。
- 隔膜装备在 V 槽，可阻止怀孕；触发对应效果时会消耗耐久。
- 把鼠标停在饰品上可查看汉化后的属性增减说明。

## 六、怀孕系统

### 正常受孕条件

怀孕不是喝药后立刻发生。代码中的实际判定条件如下：

1. 玩家性别必须包含女性；仅男性玩家不会怀孕。
2. 必须与非玩家生物进入动画，并播放到带有“阴道内射（V injector）”标记的高潮阶段。
3. 玩家当前不能已经怀孕，也不能装备“避孕隔膜”等阻止怀孕的饰品。
4. 高潮发生时进行一次受孕判定。默认基础概率为 5%，成功后胎儿类型取自完成内射的生物。

默认妊娠持续 3 分钟。时间结束后会播放生产动画，并根据生物配置直接产下后代或生成待孵化的蛋；数量、孵化时间和是否自动驯服都可配置。

### 提高受孕概率

- “易孕药水”把受孕概率至少提高到 80%。
- “强效易孕药水 II”把受孕概率至少提高到 95%。
- 易孕药水的酿造链：`精液蜂蜜饮剂 + 鸡蛋 → 易孕药水`，再加萤石粉得到强效版本，或加红石延长持续时间。
- “精液蜂蜜饮剂”的无序配方：任意一瓶本模组精液 + 蜂蜜瓶。
- 想直接测试可使用管理员命令：`/non pregnancy set <玩家> <生物ID> [分钟] [后代数量]`。

常用配置：

- `pregnancyEnabled`：总开关。
- `pregnancyChancePercent`：基础概率。
- `pregnancyDurationMinutes`：持续时间。
- `pregnancyAutoTameMobs`：后代是否自动驯服。
- `keepPregnancyAfterDeath`：死亡后是否保留。
- `requireMaleFemaleForBreeding`：原版繁殖是否要求一雄一雌。

若不想使用该系统，把 `pregnancyEnabled` 改为 `false` 即可。

## 六点五、截图中的四件物品

从左到右通常是“催情花粉混合物”“性欲增幅剂”“某生物性欲锁定器”“性欲抑制剂”。

- 催情花粉混合物：3 朵任意小型花无序合成。它本身不能对生物使用，是制作增幅剂/抑制剂的材料。
- 性欲增幅剂：拿在手里对生物右键，每次把该生物的永久性欲增长倍率提高 `0.2`，最高 `6.0`；也会解除性欲锁定。非创造模式消耗一个。
- 性欲抑制剂：对生物右键，每次把永久性欲增长倍率降低 `0.2`，最低 `0.4`。非创造模式消耗一个。
- `<生物名> 性欲锁定器`：只对名称指定的生物种类有效。右键匹配生物后把其当前性欲压到 199 以下、清除攻击目标并锁住性欲，使其不再进入主动性交遭遇；用性欲增幅剂可解除。

增幅剂/抑制剂都是无序配方：`一瓶精液 + 蜂巢 + 糖 + 催情花粉混合物 + 菌类`。增幅剂使用绯红菌，抑制剂使用诡异菌。

性欲锁定器必须在 3×3 工作台合成：四角放同一种生物的精液瓶，上下放诡异菌，左右放性欲抑制剂，中间放发光浆果。成品只绑定该精液来源的生物。

## 七、污渍与破损皮肤

动画和伤害可能积累污渍或破损皮肤阶段，并显示在玩家皮肤上。

- 靠近织布机后打开物品栏，点击新增的修复按钮可修复破损皮肤。
- 若把 `allowCraftingTableSkinRepair` 改为 `true`，工作台也可用于修复。
- 包内附带三阶段遮罩和回退皮肤示例，位于 `config/needsofnature`。
- `destroyedSkinTintHex` 可指定回退皮肤颜色；留空时自动取色。
- 可通过 `messSystemEnabled` 或 `destroyedSkinSystemEnabled` 独立关闭相应系统。

## 八、遭遇和挣脱

部分高能量生物会进入主动遭遇流程。HUD 会显示挣脱进度。持续进行普通攻击操作可累计挣脱次数；默认需要 12 次有效操作，进度会随时间衰减。

相关配置：

- `attackEscapeHits`：所需次数。
- `attackDecayPerSecond`：进度衰减速度。
- `attackEscapeDamageDifficultyPercent`：挣脱难度倍率。
- `attackCreativePlayers`：是否影响创造模式玩家，默认关闭。
- `postEscapeGatherMaxMobs`：成功挣脱后最多参与追逐的附近生物数。

若不希望出现主动遭遇，可降低相关行为概率、关闭对应动画，或使用防护饰品。

## 九、马用液体收集器

手持“马用液体收集器”对方块表面使用，会在命中位置放置收集实体。它需要有足够空间，碰撞区域被占用时不会放置。该物品主要配合马相关动画和液体玩法。

## 十、推荐配置调整

只想看动画、降低玩法惩罚：

```json
"pregnancyEnabled": false,
"messSystemEnabled": false,
"destroyedSkinSystemEnabled": false,
"filledStageOneSpeedMult": 1.0,
"filledStageTwoSpeedMult": 1.0,
"filledStageThreeSpeedMult": 1.0
```

希望状态在死亡后保留：

```json
"keepMessAfterDeath": true,
"keepRippedSkinAfterDeath": true,
"keepLiquidTankAfterDeath": true,
"keepPregnancyAfterDeath": true
```

关闭可选外设联动：

```json
"intifaceEnabled": false
```

这是随包默认值。日志里的“Buttplug.io integration is unavailable”只是可选功能提示，不影响正常游玩。

## 十一、常见问题

### 显示英文

确认游戏语言为简体中文，并确认两个新 JAR 内都含有 `assets/.../lang/zh_cn.json`。不要同时保留旧版同名 JAR。

### 动画数量为 0

确认默认包位于：

```text
游戏版本目录/needsofnature/needs_of_nature_default_packv1.3.0.zip
```

它不是普通资源包，不要只放入 `resourcepacks`。

### 饰品不出现

确认 Trinkets 3.7.2 已由 Connector 成功加载。日志应显示注册了 5 个数据驱动饰品，而不是“Trinkets is not installed”。

### 进入世界后 Mixin 崩溃

确认使用压缩包内的新 NeedsOfNature JAR，并删除旧版、重复版以及 Connector 自动缓存中的过期映射。通常重新启动后 Connector 会重建缓存。

### 进入旧世界出现注册表 ID 警告

Forge 在增加或调整模组后可能重新分配数字 ID。只要对象名称仍是 `needsofnature:...` 且世界可以进入，这类“Expected/Got”提示通常只是警告。首次使用前仍建议备份存档。

## 十二、管理员命令

输入 `/needsofnature` 后按 Tab 可查看当前构建提供的子命令，包括玩家信息、性别、怀孕、污渍、破损皮肤和生物冷却调试。大多数命令需要管理员权限，普通游玩不必使用。

建议先备份世界，再用命令修改玩家持久状态。
