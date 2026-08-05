package com.blue.bluesclient.feat.blockfakevillager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class FakeVillagerRenderer {
    private static final String TEAM_NAME = "red_glow";

    public static void applyGlowing() {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        if (world == null) {
            return;
        }
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityVillager) {
                if (FakeVillagerHandler.isTrapVillager(entity)){
                    FakeVillagerRenderer.renderGlowing(entity);
                }
            }
        }
    }

    public static void renderGlowing(Entity entity) {
        if (entity == null || entity.world == null || !entity.world.isRemote) {
            return;
        }

        entity.setGlowing(true);

        ScorePlayerTeam team = ensureRedGlowTeam(entity.world);
        String entry = entity.getCachedUniqueIdString();
        Scoreboard board = entity.world.getScoreboard();

        ScorePlayerTeam current = board.getPlayersTeam(entry);
        if (current != team) {
            board.addPlayerToTeam(entry, TEAM_NAME);
        }
    }

    private static ScorePlayerTeam ensureRedGlowTeam(World world) {
        Scoreboard board = world.getScoreboard();
        ScorePlayerTeam team = board.getTeam(TEAM_NAME);
        if (team == null) {
            team = board.createTeam(TEAM_NAME);
        }
        team.setPrefix(TextFormatting.RED.toString());
        team.setColor(TextFormatting.RED);
        return team;
    }

    public static void clearGlowing() {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        if (world == null) return;

        Scoreboard board = world.getScoreboard();

        for (Entity entity : world.loadedEntityList) {
            if (!(entity instanceof EntityVillager)) continue;
            if (!FakeVillagerHandler.isTrapVillager(entity)) continue;

            entity.setGlowing(false);

            String entry = entity.getCachedUniqueIdString();
            ScorePlayerTeam team = board.getPlayersTeam(entry);
            if (team != null && TEAM_NAME.equals(team.getName())) {
                board.removePlayerFromTeam(entry, team);
            }
        }

        ScorePlayerTeam team = board.getTeam(TEAM_NAME);
        if (team != null) {
            board.removeTeam(team);
        }
    }

}
