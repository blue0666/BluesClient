package com.blue.bluesclient.feat.blockesp;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public final class BlockEspRenderer {
    private BlockEspRenderer() {
    }

    public static void prerender(RenderWorldLastEvent event){
        if(BCConfig.TileEntityEsp.getBooleanValue()){BlockEspRenderer.render(event,BlockEspStore.getNormal(),1,1,1);}
        if(BCConfig.WitherSpawnerEsp.getBooleanValue()){BlockEspRenderer.render(event,BlockEspStore.getWither(),1,1,0);}
        if(BCConfig.GorgonSpawnerEsp.getBooleanValue()){BlockEspRenderer.render(event,BlockEspStore.getGorgon(),1,0,0);}
        if(BCConfig.ElderGuardianSpawnerEsp.getBooleanValue()){BlockEspRenderer.render(event,BlockEspStore.getElderguardian(),1,1,0);}
    }

    public static void render(RenderWorldLastEvent event, List<BlockPos> positions, float red, float green, float blue) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (positions.isEmpty()) {
            return;
        }

        float partialTicks = event.getPartialTicks();
        Entity view = mc.getRenderViewEntity();
        if (view == null) {
            view = mc.player;
        }

        double vx = view.lastTickPosX + (view.posX - view.lastTickPosX) * partialTicks;
        double vy = view.lastTickPosY + (view.posY - view.lastTickPosY) * partialTicks;
        double vz = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GL11.glLineWidth(3.0F);

        for (BlockPos pos : positions) {
            AxisAlignedBB bb = new AxisAlignedBB(pos)
                    .grow(0.002D)
                    .offset(-vx, -vy, -vz);

            RenderGlobal.drawSelectionBoundingBox(bb, red, green, blue, 1.0F);
        }

        GlStateManager.glLineWidth(1.0F);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}