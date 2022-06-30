package net.syobonoon.plugin.miclosbattle;

import org.bukkit.plugin.java.JavaPlugin;

public class MicLoSBattle extends JavaPlugin {
    public static Config config;
    public static boolean gameflag = false;

    @Override
    public void onEnable() {
        config = new Config(this);
        new BattleEventsListener(this);
        getCommand("bstart").setExecutor(new BattleCommand(this));
        getCommand("bstop").setExecutor(new BattleCommand(this));
        getCommand("breload").setExecutor(new BattleCommand(this));
        getLogger().info("onEnable");

    }
}
