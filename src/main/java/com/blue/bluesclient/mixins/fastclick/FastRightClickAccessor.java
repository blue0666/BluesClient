package com.blue.bluesclient.mixins.fastclick;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface FastRightClickAccessor {
    @Invoker("rightClickMouse")
    void bluesclient$rightClickMouse();
}