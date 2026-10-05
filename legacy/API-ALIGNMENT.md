# RLCombat / Better Survival API 对齐记录

对照日期：2026-10-05  
用途：后续双版本兼容时对齐类名、字段名、方法签名。本文档只记录，不代表已改代码。

## 对照样本

| 角色 | 文件 | 说明 |
|---|---|---|
| 旧 RLCombat | `legacy/RLCombat-1.12.2-2.0.8.jar` | 隔壁整合包；**无** `ConfigurationHandler.server` |
| 新 RLCombat | `libs/RLCombat-1.12.2-2.2.4.jar` | 当前 BluesClient `compileOnly` |
| Better Survival | `legacy/better_survival-1.5.3.jar` | 与 Gradle `bettersurvival_version=260360:5729552` 视为同一条 1.5.3 线 |

原始 `javap -p` 输出见同目录 `javap-raw.txt`。

探测规则（后续实现）：`Class.forName("bettercombat.mod.util.ConfigurationHandler").getField("server")` 成功 → 新版；`NoSuchFieldException` → 2.0.8。

---

## 1. 版本判定与配置

### 1.1 配置结构

| | 2.0.8 | 2.2.4 |
|---|---|---|
| 结构 | 全部顶层 `public static` | `public static final ServerConfig server` + `ClientConfig client` |
| 配置系统 | 旧 `Configuration config` + `init(File)` | `@Config` 嵌套类 |

**禁止**在 2.0.8 路径写 `ConfigurationHandler.server`（会 `NoSuchFieldError`）。

### 1.2 字段名（极易写错）

| 含义 | 2.2.4（现有代码） | 2.0.8（必须用这个拼写） |
|---|---|---|
| 副手攻击开关 | `server.enableOffhandAttack` | **`enableOffHandAttack`**（Hand 的 **H 大写**） |
| 副手伤害系数 | `server.offhandEfficiency` | **`offHandEfficiency`** |
| 弱化副手 | `server.weakerOffhand` | `weakerOffhand` |
| 穿可穿过方块挥击 | `server.swingThroughPassableBlocks` | **不存在** |
| 满能量才能打 | `server.requireFullEnergy` | `requireFullEnergy` |
| 随机暴击 | `server.randomCrits` | `randomCrits` |

2.0.8 **没有** `swingThroughPassableBlocks`。副手射线不要调用 `ReachFixUtil.*IgnorePassable`（该类在 2.0.8 也不存在）。

### 1.3 物品 / 实体白名单方法

| 含义 | 2.2.4 | 2.0.8 |
|---|---|---|
| 副手可用物品 | `isItemAttackUsableOffhand(Item)` | **`isItemAttackUsable(Item)`** |
| 可攻击实体 | `isEntityAttackableOffhand(Entity)` | **`isEntityAttackable(Entity)`** |

`RLCombatHandler.isClientNunchakuOffhand` 当前调用 Offhand 版；2.0.8 必须改名，否则 `NoSuchMethodError`。

---

## 2. BluesClient 当前调用点 → 2.0.8 能否直调

文件：`src/main/java/com/blue/bluesclient/event/forge/RLCombatHandler.java`

| 现有符号 | 2.2.4 | 2.0.8 | 硬引用进 2.0.8 进程的后果 |
|---|---|---|---|
| `ConfigurationHandler.server.enableOffhandAttack` | 有 | **无 server** | `NoSuchFieldError: server`（已实测崩溃） |
| `ConfigurationHandler.server.swingThroughPassableBlocks` | 有 | 无 | 同上 / 即便拆了 server 也无此字段 |
| `ConfigurationHandler.isItemAttackUsableOffhand` | 有 | 无此方法名 | `NoSuchMethodError` |
| `EventHandlersClient.checkItemstacksChanged()` | 有 | **无** | `NoSuchMethodError` |
| `EventHandlersClient.onMouseRightClick()` | 有 | **有（签名相同）** | 可共用 |
| `EventHandlersClient.shouldAttack(Entity, Player)` | private 有 | **private 有，签名相同** | Invoker 两边都能挂，但 mixin 类不要绑死新版独有成员 |
| `Helpers.clearOldModifiers(living, stack, bool, bool, bool)` | 3 个 boolean | 只有 `(living, stack)` | `NoSuchMethodError` |
| `Helpers.addNewModifiers(..., 3 bool)` | 同上 | 只有两参数 | `NoSuchMethodError` |
| `Helpers.execNullable(T, Function, R)` | 有 | **有** | 可共用（`NunchakuConfigProvider` 用到） |
| `ReachFixUtil.pointedObject(...)` | 有 | **类不存在** | `NoClassDefFoundError` |
| `ReachFixUtil.pointedObjectIgnorePassable(...)` | 有 | **类不存在** | 同上 |
| `EventHandlers.OFFHAND_COOLDOWN` | 有 | **字段名是 `TUTO_CAP`** | `NoSuchFieldError: OFFHAND_COOLDOWN` |
| `coh.getTicksSinceLastSwing()` | 有 | **方法是 `getOffhandCooldown()`** | `NoSuchMethodError` |
| `CapabilityOffhandCooldown` 类本身 | 有 | 有（实现不同） | 类名可共用，**方法/cap 常量不能混** |

### 副手冷却换算（若做 2.0.8 副手）

```text
2.2.4: cooledStr = clamp((getTicksSinceLastSwing() + 0.5F) / cooldown, 0, 1)
        cap = EventHandlers.OFFHAND_COOLDOWN

2.0.8:  cooledStr 需按 getOffhandCooldown() / getOffhandBeginningCooldown() 自行对齐
        cap = EventHandlers.TUTO_CAP
        不要假设与新版同一套「距上次挥击 tick」语义，移植前要对照 2.0.8 的 tick() 实现。
```

第一阶段建议：2.0.8 **不实现副手万物**，避免冷却语义踩坑。

---

## 3. BetterSurvivalHandler（nunchaku 识别 mixin）

| | 2.0.8 | 2.2.4 |
|---|---|---|
| 全名 | `bettercombat.mod.util.BetterSurvivalHandler` | `bettercombat.mod.compat.BetterSurvivalHandler` |
| 方法 | `public static boolean isNunchaku(Item)` | 同名同签名 |

当前 mixin：`@Mixin(BetterSurvivalHandler.class)` 编的是 **compat 包**。  
2.0.8 **没有** `bettercombat.mod.compat.BetterSurvivalHandler`（已 javap 确认找不到）。

若要在 2.0.8 上也改 `isNunchaku`：另写 legacy mixin，`targets = "bettercombat.mod.util.BetterSurvivalHandler"`，**不要** import compat 类。  
主手长按连打主要靠 BS `ModClientHandler`，不依赖这个 mixin。

---

## 4. Better Survival 1.5.3（主手连打）

Gradle 编译的 BS 与 `legacy/better_survival-1.5.3.jar` 同一代。主手 Mixin 的注入点两边一致。

### `ModClientHandler.onClientTick` 实际 INVOKE（reobf jar）

顺序要点：

1. `ItemStack.func_77973_b()` → `getItem()`
2. `instanceof ItemNunchaku`
3. 冷却 `EntityPlayerSP.func_184825_o(0.5F)` → `getCooledAttackStrength`
4. 射线 `meldexun.reachfix.hook.client.EntityRendererHook.pointedObject(Entity, EntityPlayer, EnumHand, World, float)`  
   （**不是** RLCombat 的 `ReachFixUtil`）
5. 若 `BetterSurvival.isRLCombatLoaded`：  
   `RLCombatCompat.attackEntityFromClient(RayTraceResult, EntityPlayer)`
6. 否则：  
   `PlayerControllerMP.func_78764_a(EntityPlayer, Entity)` → `attackEntity`

现有 `ModClientHandler_Mixin` 两条 Wrap 的 target 字符串与 1.5.3 **一致**。legacy 不必另找 INVOKE。  
2.0.8 上长按失效，优先查：整类 mixin 是否因其它 inject 失败、或 `getItem` Redirect 未挂上；不是「1.5.3 没这条调用」。

### `RLCombatCompat`（1.5.3）

```
com.mujmajnkraft.bettersurvival.integration.RLCombatCompat
public static void attackEntityFromClient(RayTraceResult, EntityPlayer)
```

legacy mixin 可用纯字符串 `@At`，**不要** `import RLCombatCompat`（方案 A：编译只认新 RLC，这个类在 BS 上编也过，但为隔离仍建议字符串）。

### `ItemNunchaku$1`（旋转动画 getter）

reobf 方法名：`func_185085_a(ItemStack, World, EntityLivingBase)`（MCP：`apply`）。

当前 mixin：`method = "apply", remap = false`。  
在 **已反混淆的开发环境** 能对上 `apply`；在 **整合包 reobf BS** 上字面量 `apply` 可能对不上 `func_185085_a`。后续兼容时应用 `remap=true` 的 `apply` 或同时注入 `func_185085_a`。此条与 RLC 2.0.8/2.2.4 无关，是 BS 混淆差。

### 可共用（1.5.3 有）

- `INunchakuCombo.isSpinning / setSpinning`
- `NunchakuComboProvider.NUNCHAKUCOMBO_CAP`
- `MessageNunchakuSpinClient(boolean)`
- `BetterSurvivalPacketHandler.NETWORK`

---

## 5. Mixin 清单（按版本 apply）

| Mixin | 2.2.4 | 2.0.8 |
|---|---|---|
| `bettercombatmod.BetterSurvivalHandler_Mixin` | apply，目标 `compat.BetterSurvivalHandler` | **不要 apply**；可选另做 `util.BetterSurvivalHandler` 字符串 mixin |
| `bettercombatmod.EventHandlersClient_Invoker` | `shouldAttack` 两边都有 | 仅 Invoker 这一条可共用；**不要**在同一接口里 Invoker `checkItemstacksChanged` |
| `mujmajnkraftsbettersurvival.ModClientHandler_Mixin` | apply | apply（注入点相同）；建议拆类以免和其它失败绑定 |
| `mujmajnkraftsbettersurvival.ItemNunchaku_Mixin` | apply | apply（注意 `apply` vs `func_185085_a`） |

`LateMixinConfig` 不能再用「包名最后一段 = modId」。`modern` / `legacy` 子包会被当成假 modId。

---

## 6. 实现时禁止混用的写法（防 bug）

```text
# 错：2.0.8
ConfigurationHandler.server.*
ConfigurationHandler.isItemAttackUsableOffhand
EventHandlersClient.checkItemstacksChanged
EventHandlers.OFFHAND_COOLDOWN
getTicksSinceLastSwing()
Helpers.clearOldModifiers(player, stack, false, true, true)
ReachFixUtil.pointedObject(...)
@Mixin(bettercombat.mod.compat.BetterSurvivalHandler.class)  // 2.0.8 无此类

# 对：2.0.8
ConfigurationHandler.enableOffHandAttack          // H 大写
ConfigurationHandler.isItemAttackUsable(item)
EventHandlers.TUTO_CAP
getOffhandCooldown() / getOffhandBeginningCooldown()
Helpers.clearOldModifiers(player, stack)          // 仅两参数
射线：EntityRendererHook.pointedObject（与 BS 主手相同）或不做穿方块
@Mixin(targets = "bettercombat.mod.util.BetterSurvivalHandler")

# 对：2.2.4（保持现有）
ConfigurationHandler.server.enableOffhandAttack   // h 小写
isItemAttackUsableOffhand
OFFHAND_COOLDOWN + getTicksSinceLastSwing
Helpers.* 五参数
ReachFixUtil
compat.BetterSurvivalHandler
```

反射读 2.0.8 开关时字段名必须是 **`enableOffHandAttack`**，写成 `enableOffhandAttack` 会 `NoSuchFieldException` 且静默当成关副手。

---

## 7. 建议施工顺序（与 API 对齐）

1. 探测 `server` 字段；Handler 拆类，2.0.8 **不要加载**含 `.server` 字节码的类。  
2. 主手：保证 `ModClientHandler` mixin 在 2.0.8 进程 apply（注入点已对齐）。  
3. `BetterSurvivalHandler` mixin 仅新版；旧版可选 util 包字符串 mixin。  
4. 副手 2.0.8：换字段名 + cap 名 + 冷却方法 + 去掉 ReachFixUtil / checkItemstacksChanged / 五参数 Helpers；或第一阶段不做。

---

## 8. 类存在性速查（RLCombat）

2.0.8 有、2.2.4 包名变了：

- `bettercombat.mod.util.BetterSurvivalHandler` → `bettercombat.mod.compat.BetterSurvivalHandler`

仅 2.2.4 有（2.0.8 不要引用）：

- `ConfigurationHandler$ServerConfig` / `$ClientConfig`
- `ReachFixUtil`
- `EventHandlersClient.checkItemstacksChanged`
- `compat.*` 整包（2.0.8 的 QualityTools 等在 `util`）

仅 2.0.8 有：

- `EventHandlers.TUTO_CAP`、`OFFHAND_CAP`、`SECONDHURTTIMER_CAP`
- `Helpers.getOffhandCooldown(Player)`
- `ConfigurationHandler.init(File)` / `createInstLists()`
