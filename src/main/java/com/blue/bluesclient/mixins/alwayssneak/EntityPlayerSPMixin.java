package com.blue.bluesclient.mixins.alwayssneak;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin {
    @Redirect(
            method = "onLivingUpdate",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/util/MovementInput;sneak:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private boolean bluesFlightDescendByRealKey(MovementInput input) {
        if (BCConfig.AlwaysSneak.getBooleanValue()) {
            return Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
        }
        return input.sneak;
    }
}
