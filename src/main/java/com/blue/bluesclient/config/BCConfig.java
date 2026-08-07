package com.blue.bluesclient.config;

import com.blue.bluesclient.BluesClient;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.blue.bluesclient.config.ConfigFactory.*;

public class BCConfig implements IConfigHandler {
    private static final BCConfig Instance = new BCConfig();
    private BCConfig() {
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
        map.put("列表",LIST);
        map.put("快捷键设置",KEYBIND);
        map.put("杂项",MISCELLANEOUS);
        return map;
    }

    public static final List<IConfigBase> VALUE;
    public static final ConfigBoolean AlwaysSneak = ofBoolean("常驻潜行专精",false,"让玩家的潜行专精一直生效，不需要常按，且仍旧可以自由移动甚至开箱");
    public static final ConfigBoolean DamageDisplay = ofBoolean("伤害显示",false,"在左下角实时显示伤害来源与类型");
    public static final ConfigBoolean TileEntityEsp = ofBoolean("容器透视",false,"透视方块实体类型，可用于寄生虫大楼找箱子，只透视方块实体而不是所有方块是因为这样能大幅提高性能");
    public static final ConfigInteger TileEntityEspDistance = ofInteger("容器透视距离",64,1,256);
    public static final ConfigBoolean EverythingNunchaku = ofBoolean("万物双截棍:仅武器",false,"让武器攻击方式都像双截棍一样，副手也可以生效\n功能搬运自cdstk，特别鸣谢");
    public static final ConfigBoolean EverythingNunchakuAllowAll = ofBoolean("万物双截棍:所有物品", false, "忽略白名单，任意主手物品都可长按连打");
    public static final ConfigBoolean RLCombatOffhand = ofBoolean("万物双截棍:开启副手", false,"开启副手攻击\nTips:可攻击的副手由服务端限制，例如双截棍原版，在默认服务器中无法生效\n单人档需修改配置才能生效");
    public static final ConfigBoolean RLCombatEntityBlacklist = ofBoolean("万物双截棍:攻击实体过滤", true, "副手攻击过滤实体\n这只会阻止双截棍方式的连击效果，单点右键导致的攻击由RLCombat自身管理");
    public static final ConfigBoolean RLCombatOffhandNunchaku = ofBoolean("万物双截棍:副手动画不依赖主手", false, "副手攻击动画例如双截棍的旋转也会生效");

    public static final List<IConfigBase> LIST;
    public static final ConfigStringList TileEntityEspList = ofStringList("透视容器列表", ImmutableList.of("minecraft:chest"));
    public static final ConfigStringList NunchakuItemClassWhitelist = ofStringList(
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
    public static final ConfigStringList NunchakuItemIDWhitelist = ofStringList("万物双截棍:物品ID白名单", ImmutableList.of("minecraft:diamond_sword"));
    public static final ConfigStringList NunchakuItemIDBlacklist = ofStringList("万物双截棍:物品ID黑名单", ImmutableList.of("minecraft:stick"));
    public static final ConfigStringList NunchakuEntityBlacklist = ofStringList(
            "万物双截棍:实体右键攻击黑名单",
            ImmutableList.of(
                    "minecraft:horse",
                    "minecraft:armor_stand",
                    "minecraft:villager",
                    "minecraft:item_frame"
            ));

    public static final List<ConfigHotkey> KEYBIND;
    public static final ConfigHotkey OpenWindow= ofHotkey("打开菜单","B,C","打开模组配置界面");
    public static final ConfigHotkey AlwaysSneakToggle= ofHotkey("切换一直潜行","","开启一直潜行");
    public static final ConfigHotkey EverythingNunchakuToggle = ofHotkey("切换万物双截棍","","开启万物双截棍");

    public static final List<IConfigBase> MISCELLANEOUS;
    public static final ConfigBoolean WitherSpawnerEsp = ofBoolean("凋零刷怪笼警告",false,"透视警告凋零刷怪笼");
    public static final ConfigBoolean GorgonSpawnerEsp = ofBoolean("美杜莎刷怪笼警告",false,"透视警告美杜莎刷怪笼");
    public static final ConfigBoolean ElderGuardianSpawnerEsp = ofBoolean("远古守卫者刷怪笼警告",false,"透视警告远古守卫者刷怪笼");
    public static final ConfigBoolean BlockFakeLibrarianTrade = ofBoolean("阻止伪人村民交互",false,"拦截与伪人村民右键导致的爆炸与debuff");
    public static final ConfigBoolean DisableIronSkinRenderer = ofBoolean("禁用玩家钢铁皮肤渲染效果",false);
    public static final ConfigBoolean DisableCompassHUD = ofBoolean("禁用指南针手持坐标HUD",false,"指南针拿着的时候很挡视野，例如1.5幸运的彩蛋物品\n这里将其禁用，因为直接看F3的坐标就行了");
    public static final ConfigBoolean NoHiddenFlag = ofBoolean("完全显示隐藏标签",false,"光标对准物品后按Shift，完全显示隐藏属性彩蛋物品的实际效果\n对诅咒物品有奇效\n注：可能与DebrisClient的额外显示互相覆写");

    static {
        VALUE = ImmutableList.of(
                AlwaysSneak,
                DamageDisplay,
                TileEntityEsp,
                TileEntityEspDistance,
                EverythingNunchaku,
                EverythingNunchakuAllowAll,
                RLCombatOffhand,
                RLCombatEntityBlacklist,
                RLCombatOffhandNunchaku
        );
        LIST = ImmutableList.of(
                TileEntityEspList,
                NunchakuItemIDWhitelist,
                NunchakuItemIDBlacklist,
                NunchakuEntityBlacklist
        );
        KEYBIND = ImmutableList.of(
                OpenWindow,
                AlwaysSneakToggle,
                EverythingNunchakuToggle
        );
        MISCELLANEOUS = ImmutableList.of(
                WitherSpawnerEsp,
                GorgonSpawnerEsp,
                ElderGuardianSpawnerEsp,
                BlockFakeLibrarianTrade,
                DisableIronSkinRenderer,
                DisableCompassHUD,
                NoHiddenFlag
        );
        Instance.load();
    }
}
