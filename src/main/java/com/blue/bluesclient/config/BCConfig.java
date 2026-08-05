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

    public static final List<IConfigBase> LIST;
    public static final ConfigStringList TileEntityEspList = ofStringList("透视容器列表", ImmutableList.of("minecraft:chest"));

    public static final List<ConfigHotkey> KEYBIND;
    public static final ConfigHotkey OpenWindow= ofHotkey("打开菜单","B,C","打开模组配置界面");
    public static final ConfigHotkey AlwaysSneakToggle= ofHotkey("切换一直潜行","","开启一直潜行");

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
                TileEntityEspDistance
        );
        LIST = ImmutableList.of(
                TileEntityEspList
        );
        KEYBIND = ImmutableList.of(
                OpenWindow,
                AlwaysSneakToggle
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
