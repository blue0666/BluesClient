package com.blue.bluesclient.feat.nohiddenflag;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TooltipListener {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!BCConfig.NoHiddenFlag.getBooleanValue()) return;
        if (!GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)) return;
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || !stack.hasTagCompound()) return;
        ResourceLocation registryName = stack.getItem().getRegistryName();
        if (registryName == null) return;
        if (registryName.getNamespace().equals("bountifulbaubles")) return;

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey("HideFlags")) return;

        ItemStack copy = stack.copy();
        NBTTagCompound copyTag = copy.getTagCompound();
        copyTag.removeTag("HideFlags");

        EntityPlayer player = event.getEntityPlayer();
        ITooltipFlag flag = event.getFlags();
        List<String> revealed = copy.getTooltip(player, flag);

        event.getToolTip().clear();
        event.getToolTip().addAll(revealed);
    }
}