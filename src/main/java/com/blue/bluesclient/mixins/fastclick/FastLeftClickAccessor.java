package com.blue.bluesclient.mixins.fastclick;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface FastLeftClickAccessor {
    @Invoker("clickMouse")
    void bluesclient$leftClickMouse();

    @Accessor("leftClickCounter")
    void bluesclient$setLeftClickCounter(int value);
}
