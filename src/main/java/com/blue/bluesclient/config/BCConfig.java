package com.blue.bluesclient.config;

import com.blue.bluesclient.BluesClient;
import com.blue.bluesclient.feat.everythingnunchaku.NunchakuConfigProvider;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import net.minecraft.util.text.TextFormatting;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.blue.bluesclient.config.ConfigFactory.*;

public class BCConfig implements IConfigHandler {
    private static final BCConfig Instance = new BCConfig();

    private BCConfig() {
    }

    @Override
    public void save() {
        IConfigHandler.super.save();
        NunchakuConfigProvider.initClientNunchakus();
    }

    public static BCConfig getInstance() {
        return Instance;
    }

    @Override
    public String getModName() {
        return BluesClient.NAME;
    }

    @Override
    public String getConfigFileName() {
        return BluesClient.MODID + ".json";
    }

    @Override
    public Map<String, List<? extends IConfigBase>> getConfigsPerCategories() {
        LinkedHashMap<String, List<? extends IConfigBase>> map = new LinkedHashMap<>();
        map.put("功能开关", VALUE);
        map.put("列表", LIST);
        map.put("快捷键设置", KEYBIND);
        map.put("杂项", MISCELLANEOUS);
        map.put("调试", DEBUG);
        return map;
    }

    public static final List<IConfigBase> VALUE;
    public static final ConfigBoolean AlwaysSneak = ofLocalizedBoolean("alwayssneak","常驻潜行专精", false, "将潜行与移动分离，常驻潜行且仍旧可以自由移动和开箱");
    public static final ConfigBoolean AutoUnlockPlus = ofLocalizedBoolean("autounlockplus", "自动开锁+", false, "使用世界种子强解开锁密码并自动一次性打开\n如果是服务器中，必须有种子才可解\n枚举法开锁请使用DebrisClient");
    public static final ConfigBoolean AutoUnlockInstant = ofLocalizedBoolean("autounlockinstant", "自动开锁+++", false);
    public static final ConfigString LockWorldSeed = ofLocalizedString("lockworldseed", "开锁世界种子", "", "如果服务器中，输入种子才可以解出密码");

    public static final ConfigBoolean ToolTipScroller = ofLocalizedBoolean("tooltipscroller", "物品信息拖动", false, "Shift+滚轮上下拖动物品的属性信息界面");
    public static final ConfigBoolean TileEntityEsp = ofLocalizedBoolean("tileentityesp", "容器透视", false, "对寄生虫大楼有奇效");
    public static final ConfigInteger TileEntityEspDistance = ofLocalizedInteger("tileentityespdistance", "容器透视距离", 64, 1, 256);
    public static final ConfigInteger TileEntityEspTransparency = ofLocalizedInteger("tileentityesptransparency", "容器透视透明度", 100, 0, 100, "大量的白色方框有点眼花，自选适当调低透明度");
    public static final ConfigBoolean TileEntityEspSkipOpened = ofLocalizedBoolean("tileentityespskipopened", "容器透视取消已打开", false);
    public static final ConfigBoolean EverythingNunchaku = ofLocalizedBoolean("everythingnunchaku", "万物双截棍:仅武器", false, "让武器都使用双截棍的自动满蓄力攻击\n功能搬运自cdstk，特别鸣谢");
    public static final ConfigBoolean EverythingNunchakuAllowAll = ofLocalizedBoolean("everythingnunchakuallowall", "万物双截棍:所有物品", false, "忽略白名单，任意主手物品都可长按连打");
    public static final ConfigBoolean RLCombatOffhand = ofLocalizedBoolean("rlcombatoffhand", "万物双截棍:开启副手", false, "开启副手攻击\n" + TextFormatting.YELLOW + "Tips:可攻击的副手由服务端限制，例如双截棍在默认服务器中无法生效\n单人档需修改配置才能生效" + TextFormatting.RESET);
    public static final ConfigBoolean RLCombatEntityBlacklist = ofLocalizedBoolean("rlcombatentityblacklist", "万物双截棍:攻击实体过滤", true, "副手攻击过滤实体\n这只会阻止双截棍方式的连击效果，单点右键导致的攻击由RLCombat自身管理");
    public static final ConfigBoolean RLCombatOffhandNunchaku = ofLocalizedBoolean("rlcombatoffhandnunchaku", "万物双截棍:副手动画不依赖主手", false, "副手攻击动画例如双截棍的旋转也会生效");
    public static final ConfigBoolean FastRightClick = ofLocalizedBoolean("fastrightclick", "右键连点", false, "比tweakeroo更好的地方在于切换界面就会自动关闭");
    public static final ConfigBoolean FastLeftClick = ofLocalizedBoolean("fastleftclick", "左键连点", false, "比tweakeroo更好的地方在于切换界面就会自动关闭，并兼容RLCombat\n如果安装了其他的万物双截棍，则该连点器无法生效");

    public static final List<IConfigBase> LIST;
    public static final ConfigStringList TileEntityEspList = ofLocalizedStringList("tileentityesplist", "透视容器列表", ImmutableList.of("minecraft:chest"));
    public static final ConfigStringList SneakOpenBlockList = ofLocalizedStringList("sneakopenblocklist", "潜行开箱列表",
            ImmutableList.of(
                    "minecraft:chest",
                    "minecraft:ender_chest",
                    "minecraft:shulker_box",
                    "charm:crate",
                    "variedcommodities:crate",
                    "variedcommodities:barrel",
                    "rustic:vase",
                    "rustic:barrel",
                    "rustic:cabinet"
            ));
    public static final ConfigStringList NunchakuItemClassWhitelist = ofLocalizedStringList("nunchakuitemclasswhitelist",
            "双截棍:物品类白名单",
            ImmutableList.of(
                    "net.minecraft.item.ItemSword",
                    "net.minecraft.item.ItemAxe",
                    "net.minecraft.item.ItemSpade",
                    "net.minecraft.item.ItemPickaxe",
                    "net.minecraft.item.ItemHoe",
                    "com.mujmajnkraft.bettersurvival.items.ItemBattleAxe",
                    "com.mujmajnkraft.bettersurvival.items.ItemDagger",
                    "com.mujmajnkraft.bettersurvival.items.ItemHammer",
                    "com.mujmajnkraft.bettersurvival.items.ItemNunchaku",
                    "dev.satyrn.wolfarmor.item.ItemWolfArmor",
                    "com.lycanitesmobs.core.item.equipment.ItemEquipment"
            ));
    public static final ConfigStringList NunchakuItemIDWhitelist = ofLocalizedStringList("nunchakuitemidwhitelist", "万物双截棍:物品ID白名单", ImmutableList.of("minecraft:diamond_sword"));
    public static final ConfigStringList NunchakuItemIDBlacklist = ofLocalizedStringList("nunchakuitemidblacklist", "万物双截棍:物品ID黑名单", ImmutableList.of("minecraft:stick"));
    public static final ConfigStringList NunchakuEntityBlacklist = ofLocalizedStringList("nunchakuentityblacklist",
            "万物双截棍:实体右键攻击黑名单",
            ImmutableList.of(
                    "minecraft:horse",
                    "minecraft:armor_stand",
                    "minecraft:villager",
                    "minecraft:item_frame"
            ));

    public static final List<ConfigHotkey> KEYBIND;
    public static final ConfigHotkey OpenWindow = ofLocalizedHotkey("openwindow", "打开菜单", "B,C", "打开模组配置界面");
    public static final ConfigHotkey AlwaysSneakToggle = ofLocalizedHotkey("alwayssneaktoggle", "切换一直潜行", "", "开启一直潜行");
    public static final ConfigHotkey EverythingNunchakuToggle = ofLocalizedHotkey("everythingnunchakutoggle", "切换万物双截棍", "", "开启万物双截棍");
    public static final ConfigHotkey FlightToggle = ofLocalizedHotkey("flighttoggle", "切换飞行调试", "");
    public static final ConfigHotkey FastRightClickToggle = ofLocalizedHotkey("fastrightclicktoggle", "切换右键连点", "");
    public static final ConfigHotkey FastLeftClickToggle = ofLocalizedHotkey("fastleftclicktoggle", "切换左键连点", "");

    public static final List<IConfigBase> MISCELLANEOUS;
    public static final ConfigBoolean WitherSpawnerEsp = ofLocalizedBoolean("witherspawneresp", "凋零刷怪笼警告", false, "透视警告凋零刷怪笼");
    public static final ConfigBoolean GorgonSpawnerEsp = ofLocalizedBoolean("gorgonspawneresp", "蛇发女妖刷怪笼警告", false, "透视警告美杜莎刷怪笼");
    public static final ConfigBoolean ElderGuardianSpawnerEsp = ofLocalizedBoolean("elderguardianspawneresp", "远古守卫者刷怪笼警告", false, "透视警告远古守卫者刷怪笼");
    public static final ConfigBoolean BlockFakeLibrarianTrade = ofLocalizedBoolean("blockfakelibrariantrade", "禁用伪人村民交互", false, "拦截与伪人村民右键导致的爆炸与debuff\n高亮白袍村民请使用DebrisClient");
    public static final ConfigBoolean DisableIronSkinRenderer = ofLocalizedBoolean("disableironskinrenderer", "禁用钢铁皮肤渲染", false, "这个效果太丑陋了\n不是我喜欢的效果，直接禁用");
    public static final ConfigBoolean DisableCompassHUD = ofLocalizedBoolean("disablecompasshud", "禁用指南针手持坐标", false, "指南针拿着的时候很挡视野，例如1.5幸运的彩蛋物品\n这里将其禁用，因为直接看F3的坐标就行了");
    public static final ConfigBoolean NoHiddenFlag = ofLocalizedBoolean("nohiddenflag", "显示隐藏标签", false, "光标对准物品后按Shift，显示隐藏标签与潘多拉诅咒附魔\n对诅咒物品有奇效\n注：可能与DebrisClient的额外显示互相覆写");
    public static final ConfigBoolean NoSRPBiomeFog = ofLocalizedBoolean("nosrpbiomefog", "禁用寄生虫群系迷雾", false);
    public static final ConfigBoolean NoSRPBlockSpread = ofLocalizedBoolean("nosrpblockspread", "禁用寄生虫方块污染", false, "彻底禁用寄生虫群系的任何方块污染动作\n这一事件能给游戏带来巨额计算压力\n" + TextFormatting.YELLOW + "仅单人游戏中生效" + TextFormatting.RESET);
    public static final ConfigBoolean HighlightRustMob = ofLocalizedBoolean("highlightrustmob", "高亮生锈精英怪", false, "高亮警告生锈精英怪\n对后期玩家也有极大的威胁");
    public static final ConfigBoolean GammaOverride = ofLocalizedBoolean("gammaoverride", "伽马覆写", false);

    public static final List<ConfigBase> DEBUG;
    public static final ConfigBoolean DamageDisplayHurt = ofLocalizedBoolean("damagedisplayhurt", "伤害调试:受击", false, "在左下角实时显示玩家受到的伤害来源与类型，包括非<伤害>的血量变化\n" + TextFormatting.YELLOW + "服务器中不生效" + TextFormatting.RESET);
    public static final ConfigBoolean DamageDisplayAttack = ofLocalizedBoolean("damagedisplayattack", "伤害调试:攻击", false, "在左下角实时显示玩家造成的伤害\n原始伤害：ForgeHooks.onLivingHurt 进入总线之前\n实际伤害：ForgeHooks.onLivingDamage 整次总线之后\n" + TextFormatting.YELLOW + "服务器中不生效" + TextFormatting.RESET);
    public static final ConfigBoolean NoDamageFallOff = ofLocalizedBoolean("nodamagefalloff", "禁用伤害衰减", false, "禁用德雷版本的伤害衰减系统\n" + TextFormatting.YELLOW + "服务器中不生效" + TextFormatting.RESET);
    public static final ConfigBoolean FlightDebug = ofLocalizedBoolean("flightdebug", "飞行调试", false, "飞行的药水指环Plus");
    public static final ConfigBoolean UpdateSuppressionBlock = ofLocalizedBoolean("updatesuppressionblock", "模拟更新抑制", false, "将潜影盒命名为<更新抑制器>或者<updatesuppressor>，会由比较器抛出异常\n" + TextFormatting.YELLOW + "如果你不知道什么是更新抑制，请不要动这个" + TextFormatting.RESET);
    public static final ConfigBoolean MixinDebug = ofLocalizedBoolean("mixindebug", "附魔调试", false, "让致命一击必定暴击");

    static {
        VALUE = ImmutableList.of(
                AlwaysSneak,
                ToolTipScroller,
                AutoUnlockPlus,
                AutoUnlockInstant,
                LockWorldSeed,
                TileEntityEsp,
                TileEntityEspDistance,
                TileEntityEspTransparency,
                TileEntityEspSkipOpened,
                EverythingNunchaku,
                EverythingNunchakuAllowAll,
                RLCombatOffhand,
                RLCombatEntityBlacklist,
                RLCombatOffhandNunchaku,
                FastRightClick,
                FastLeftClick
        );
        LIST = ImmutableList.of(
                TileEntityEspList,
                SneakOpenBlockList,
                NunchakuItemIDWhitelist,
                NunchakuItemIDBlacklist,
                NunchakuEntityBlacklist
        );
        KEYBIND = ImmutableList.of(
                OpenWindow,
                AlwaysSneakToggle,
                EverythingNunchakuToggle,
                FlightToggle,
                FastRightClickToggle,
                FastLeftClickToggle
        );
        MISCELLANEOUS = ImmutableList.of(
                WitherSpawnerEsp,
                GorgonSpawnerEsp,
                ElderGuardianSpawnerEsp,
                BlockFakeLibrarianTrade,
                DisableIronSkinRenderer,
                DisableCompassHUD,
                NoHiddenFlag,
                NoSRPBiomeFog,
                NoSRPBlockSpread,
                HighlightRustMob,
                GammaOverride
        );
        DEBUG = ImmutableList.of(
                DamageDisplayHurt,
                DamageDisplayAttack,
                NoDamageFallOff,
                FlightDebug,
                UpdateSuppressionBlock,
                MixinDebug
        );
        Instance.load();
    }
}
