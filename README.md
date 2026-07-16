# NeedsOfNature 1.20.1 逆向移植工程

这是从 `Animation Director 1.3.1` 与 `NeedsOfNature 1.3.1` 二进制文件重建并降级到 Minecraft 1.20.1 的双模块工程。

当前目标是让两枚原生 Fabric 模组同时支持：

- Fabric Loader 1.20.1；
- Forge 47.4.21 + Sinytra Connector；
- Java 17；
- GeckoLib 4.8.4；
- 中文界面与配置提示。

> 本项目及默认内容涉及成人向玩法。仅应在符合法律、平台规则、服务器规则且所有参与者明确同意的环境中使用。

## 当前状态

- 两个模块均能完成 `clean build`。
- Fabric 1.20.1 客户端和世界冒烟测试已通过。
- Forge 47.4.21 + Connector beta.49 已能启动、加载默认包、进入世界并同步网络状态。
- 已内置简体中文：NeedsOfNature 681 个语言键，Animation Director 102 个语言键。
- 已修复 Forge/Connector 下的 GeckoLib 渲染、资源包集合、Mixin 描述符和物品渲染兼容问题。
- 最新修复已兼容原版内容包使用的 GeckoLib 5 资源目录：GeckoLib 4 所需的 `geo`、`animations` 资源只编入模组 JAR，外部默认包保留原 GeckoLib 5 目录和数据定义，避免 Forge/Connector 让外部包的同名资源覆盖有效 JAR 资源。玩家 `.f/.m` 性别模型会在最终资源解析前选择。
- 所有编入 JAR 的 GeckoLib 4 JSON 均移除了 UTF-8 BOM；GeckoLib 4/Gson 会把 BOM 当成第 1 行第 1 列的非法字符。
- 外部默认包保持原始 ZIP 字节结构，并通过项目自带的 ZIP 资源读取器同时提供客户端贴图和服务端数据；不要用 PowerShell `Compress-Archive` 重新打包，否则 Forge/Connector 下可能出现饰品贴图缺失和 AFW 定义数量为 0。
- 手持胡萝卜或金胡萝卜按 `M` 会选择专用 `player_manual_peak-carrot` 动画，并带有右手道具显示兜底；动画播放期间人物身体/头部朝向固定，转动视角只改变镜头。

详细历史见 [PORTING_STATUS.md](PORTING_STATUS.md)，协作约定见 [docs/DEVELOPMENT_ZH.md](docs/DEVELOPMENT_ZH.md)，玩法说明见 [docs/GAMEPLAY_ZH.md](docs/GAMEPLAY_ZH.md)。

## 目录结构

```text
.
├─ animationdirector/        Animation Director / Animation Framework 可构建源码
├─ needsofnature/            NeedsOfNature 可构建源码
├─ decompiled/               CFR 原始反编译快照，只读参考
├─ mapped-src/               1.21.11 Yarn 命名后的逆向快照，只读参考
├─ dev-assets/               本地测试用默认内容包、示例配置与玩法文档
├─ docs/                     中文开发交接文档
├─ tools/                    反编译和映射辅助工具/资料
├─ build.gradle              双模块公共构建配置
├─ gradle.properties         版本号与 Gradle 参数
└─ settings.gradle           子模块声明
```

### 哪个目录才是“最新源码”

只修改以下两个目录：

```text
animationdirector/src/main/
needsofnature/src/main/
```

`decompiled/` 保存最原始的 CFR 输出，`mapped-src/` 保存映射后的 1.21 参考代码。二者是逆向证据和对照基线，不同步回写 1.20.1 修复。所有已完成的移植、Forge 兼容、中文和运行时修复均已进入 `src/main`。

## 开发环境

- Windows 10/11 或 Linux
- JDK 17（构建目标固定为 Java 17）
- Git
- 网络可访问 Fabric、Maven Central、Ladysnake 和 GeckoLib Maven 仓库

项目依赖现在使用 Maven 坐标解析，不需要提交本机 `libs/*.jar`。

## 构建

Windows：

```powershell
./gradlew.bat clean build --no-daemon --console=plain
```

Linux/macOS：

```bash
./gradlew clean build --no-daemon --console=plain
```

输出文件：

```text
animationdirector/build/libs/animationdirector-1.20.1-1.3.1-port.1.jar
needsofnature/build/libs/needsofnature-1.20.1-1.3.1-port.1.jar
```

只构建单模块：

```powershell
./gradlew.bat :animationdirector:build
./gradlew.bat :needsofnature:build
```

## 本地运行

Fabric Loom 开发客户端：

```powershell
./gradlew.bat :needsofnature:runClient
```

实际玩法依赖默认内容包。将：

```text
dev-assets/needsofnature/needs_of_nature_default_packv1.3.0.zip
```

复制到运行目录：

```text
run/needsofnature/
```

默认包不会放在普通 `resourcepacks` 目录；NeedsOfNature 会把它同时作为客户端资源包和服务端数据包加载。

`dev-assets/config-examples/` 保存了 Forge/Connector 实机测试通过的配置样例。复制前请先备份自己的 `config/animationframework.json` 与 `config/needsofnature.json`；这些样例不是强制配置。

## 运行依赖

### 原生 Fabric

- Fabric Loader 0.16.14
- Fabric API 0.92.9+1.20.1
- GeckoLib Fabric 4.8.4
- Trinkets 3.7.2（饰品系统可选，但默认包的 5 件饰品需要）

### Forge + Sinytra Connector（已测试组合）

- Minecraft 1.20.1
- Forge 47.4.21
- Sinytra Connector 1.0.0-beta.49+1.20.1
- Forgified Fabric API 0.92.6+1.11.14+1.20.1
- GeckoLib Forge 4.8.4
- Trinkets 3.7.2（由 Connector 加载）

Forge 实例必须使用 Forge 版 GeckoLib。不要为了这两个 Fabric 模组把整合包现有的 Forge GeckoLib 替换成 Fabric JAR；项目已经在运行时桥接了两边不同的物品渲染入口。

## 关键移植内容

- 将 Minecraft 1.21 网络 `CustomPayload`/`PacketCodec` 全量迁移到 Fabric 1.20.1 `Identifier + PacketByteBuf`。
- 将 GeckoLib 5 render state/command queue 改为 GeckoLib 4 直接渲染生命周期。
- 将 1.21 数据组件迁移到 1.20.1 NBT、PotionUtil 和旧配方 API。
- 重写 23 个 C2S/S2C 数据包，同时保留频道 ID、字段顺序和皮肤上传大小限制。
- 迁移玩家/生物 DataTracker、NBT、粒子、HUD、资源包、Trinkets、怀孕、液体和破损皮肤系统。
- 对 Forge/Connector 增加 GeckoLib `GeoItem` 渲染桥、可变资源包 Provider 集合及非强制调试 Mixin。
- 重建 CFR 未能恢复的破损皮肤生成函数。
- 增加 783 个简体中文语言条目，并校验所有 `%s` 等占位符。

## 代码提交约定

1. 不修改 `decompiled/` 和 `mapped-src/`，除非是在重新生成整套逆向快照。
2. 功能修复写入对应模块的 `src/main`。
3. 网络协议变更必须同时检查 C2S、S2C、注册点和字段顺序。
4. Mixin 变更必须在 Fabric 与 Forge/Connector 两边检查目标描述符。
5. GeckoLib 相关集合与资源查找必须允许“未配置”状态，避免直接对可空返回值调用 `.stream()`。
6. 提交前至少运行一次 `clean build`；渲染或网络变更还必须进入世界复测。
7. 新增英文语言键时同步更新 `zh_cn.json`，并保持格式占位符一致。

## 推荐测试清单

- 客户端进入主菜单，无 Mixin apply failure。
- 默认内容包加载出 38 个 AFW 定义和 5 个饰品。
- 创建/进入世界后客户端与集成服务器均完成资源重载。
- 按 `M` 触发手动动画，玩家替换模型、普通贴图、发光层和粒子无崩溃。
- 验证动画开始/停止、阶段切换、摄像机和输入锁定。
- 验证液体装瓶、怀孕、饰品槽、污渍与破损皮肤同步。
- Forge/Connector 下检查没有 `NoSuchMethodError`、资源包不可变集合异常或映射后的 Mixin 描述符失败。

## 已知的非本项目警告

- 车万女仆在未安装 Patchouli 时会报告 `patchouli:guide_book` 战利品表解析失败；它不会阻止世界启动，与本项目崩溃无关。
- Buttplug.io Java 库是可选项，未安装时会自动禁用外设输出，不影响普通玩法。
- Forge 旧世界在新增注册内容后可能输出 Expected/Got 数字 ID 警告；应先备份存档，再判断是否存在实际缺失对象。

## 许可与来源

原版 `NeedsOfNature 1.3.1` 与 `Animation Director 1.3.1` 的作者均为 **L1Z0**（作者字段已从原版 JAR 的 `fabric.mod.json` 核对）。原始发布页：

- [Minecraft Needs of Nature — 原始发布页](https://www.loverslab.com/files/file/49713-minecraft-needs-of-nature-nsfw-mod-data-driven-sex-animation-mod-for-minecraft-fabric-12111/)

本仓库是社区维护的非官方 Minecraft 1.20.1 逆向移植与中文化工程，不代表原作者，也不是原作者发布的官方版本。原作名称、代码、资源及相关权利归原作者 **L1Z0** 及其各自权利人所有；使用和再分发前请同时遵守原发布页的许可、平台规则及当地法律。

提交代码时不要把无法确认许可的第三方模组 JAR、Minecraft 文件、个人配置、启动令牌或游戏日志加入仓库。
