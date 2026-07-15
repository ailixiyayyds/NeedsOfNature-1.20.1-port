# 中文开发交接说明

## 1. 源码分层

本工程保留三份不同用途的代码：

| 层级 | 目录 | 用途 | 是否修改 |
|---|---|---|---|
| 原始反编译 | `decompiled/` | CFR 输出、Intermediary 名称、还原证据 | 通常不修改 |
| 映射参考 | `mapped-src/` | Yarn 1.21.11 可读名称、对照原版逻辑 | 通常不修改 |
| 1.20.1 实现 | `animationdirector/src/main/`、`needsofnature/src/main/` | 当前可构建、可发布代码 | 所有开发在这里进行 |

“同步逆向工程”指确保修复进入第三层，并在文档中记录它与原始逻辑的差异；不是把修复复制回冻结快照。

## 2. 模块依赖关系

```text
NeedsOfNature
  ├─ Animation Director / Animation Framework
  ├─ Fabric API
  ├─ GeckoLib 4
  ├─ Trinkets（可选玩法集成）
  └─ Buttplug4j（编译期可选，运行时动态安装）
```

Animation Director 负责动画定义、演员集合、服务端权威状态、客户端时间线和 GeckoLib 替换渲染。NeedsOfNature 负责能量、匹配、液体、怀孕、饰品、HUD、皮肤以及玩法网络包。

## 3. 网络层

1.20.1 端使用 `Identifier` 作为频道，并通过 `PacketByteBuf` 手写编解码。公共入口位于两个模块各自的 `network` 包中。

修改数据包时必须保持：

- 频道 ID 不变，除非明确进行协议升级；
- 写入与读取字段顺序完全一致；
- VarInt/普通 Int、UUID、枚举和可空字段编码一致；
- C2S 在服务端线程执行，S2C 在客户端线程执行；
- 皮肤和遮罩上传继续执行 64 KiB 上限及 PNG 校验。

## 4. GeckoLib 4 渲染

1.21 原代码基于 GeckoLib 5 和渲染状态队列；1.20.1 端改为直接拦截 `EntityRenderDispatcher`，并立即调用 GeckoLib 4 renderer。

主要类：

- `AfwGeckoReplacedRender`：解析模型/贴图并执行替换渲染；
- `AfwActorAnimatable`：维护当前 actor 的渲染上下文；
- `AfwActorGeoModel`：返回本次动画、模型和贴图资源；
- `AfwBoneTextureOverrides`：从 geo JSON 加载骨骼、发光层、locator 和透明设置；
- `AfwGeckoResourceResolver`：解析动画/模型文件并提供缺失模型回退。

资源元数据允许缺省。`getBoneTextures`、`getEmissiveTextures`、`getLocators` 等查询可能没有结果，调用方必须将其视为空集合。本规则用于避免 Forge/Connector 下实际动画开始后才出现的渲染空指针。

## 5. Forge + Connector 兼容点

- Fabric GeckoLib 的 `GeoItem.makeRenderer` 在 Forge GeckoLib 中不存在。马用液体收集器通过反射选择 Fabric provider，并为 Forge 提供 `IClientItemExtensions` 动态代理。
- Forge 会在 vanilla 资源包管理器构造后继续添加 Provider，因此 NeedsOfNature 注入后的 Provider Set 必须保持可变。
- Connector 会将 Minecraft 类型重映射成 SRG/Mojmap。跨模块调试 Mixin 使用方法名匹配和 `require=0`，防止可选调试功能阻止启动。
- 原生 Forge GeckoLib 与 Connector 加载的 Fabric 模组共存；不要同时放入 Fabric 和 Forge 两枚 GeckoLib JAR。

## 6. 配置与外部内容包

配置文件：

```text
config/animationframework.json
config/needsofnature.json
```

外部 NoN 包目录：

```text
游戏目录/needsofnature/
```

默认包同时包含客户端资产和服务端数据。首次进入世界时 NeedsOfNature 会触发一次服务端资源重载，使生成的 Trinkets 标签和默认动画定义立即生效。

## 7. 汉化维护

语言文件：

```text
animationdirector/src/main/resources/assets/animationframework/lang/
needsofnature/src/main/resources/assets/needsofnature/lang/
```

当前英文/中文键数分别为 102 和 681。新增键时应检查：

- 两种语言键集合一致；
- `%s`、`%d` 等格式占位符数量和顺序一致；
- JSON 使用 UTF-8、无注释、无尾逗号；
- `NoN`、`AFW`、GeckoLib、Trinkets 等专有名词保持一致。

## 8. 最近运行时修复

- 移除失败的继承 Shadow，改为类型安全桥接 DataTracker。
- 修正 LivingEntity damage、Mob attack、TargetPredicate、initialize、Camera、HandledScreen 和 GameRenderer 的 1.20.1 Mixin 描述符。
- 修正静态 Brewing 回调和可取消的物品栏鼠标注入。
- 重建破损皮肤合成、染色和 PNG 输出路径。
- 增加 Fabric/Forge 双端 GeoItem 渲染入口。
- 保持 Forge 资源包 Provider 集合可变。
- 将可选 debug-spin Mixin 改为非强制。
- 修复未定义发光贴图时 `getEmissiveTextures(model) == null` 导致的玩家替换渲染崩溃。
- 外部 ZIP 内容包现在提供 GeckoLib 5 → GeckoLib 4 虚拟路径兼容：`geckolib/models/**` 作为 `geo/**` 暴露，`geckolib/animations/**` 作为 `animations/**` 暴露。
- AFW 动画解析器统一返回 GeckoLib 4 实际缓存键；马用液体收集器内置模型同时提供 `geo/entity/horse_liquid_collector.geo.json`，避免实体创建后渲染阶段崩溃。

## 9. 提交前检查

```powershell
./gradlew.bat clean build --no-daemon --console=plain
```

然后至少完成一次：

1. 主菜单启动；
2. 默认包加载；
3. 进入世界；
4. 按 `M` 开始动画；
5. 等待一次阶段切换；
6. 停止动画并保存退出；
7. 搜索日志中的 `Mixin apply failed`、`NoSuchMethodError`、`Rendering entity in world` 和 `Minecraft has crashed`。

## 10. 不应提交到 GitHub 的内容

- `build/`、`.gradle/`、`run/`；
- 本机 `libs/` 下载缓存和拆包内容；
- 原始第三方 JAR、Minecraft 客户端文件；
- PCL 启动脚本、访问令牌、账号信息；
- 存档、崩溃报告和个人配置；
- 大型临时编译日志。
