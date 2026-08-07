package com.blue.bluesclient.event.forge;

import com.blue.bluesclient.config.BCConfig;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageDisplayHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingHurt(LivingHurtEvent event){
        if (!BCConfig.DamageDisplay.getBooleanValue())return;

    }
}
