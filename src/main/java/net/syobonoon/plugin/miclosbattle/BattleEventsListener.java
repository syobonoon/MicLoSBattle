package net.syobonoon.plugin.miclosbattle;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

public class BattleEventsListener implements Listener{
    private Plugin plugin;

    public BattleEventsListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;

    }

    //ゲーム中に死んだ人をスペクターモードに変更
    @EventHandler
    public void onDeathEvent(PlayerDeathEvent e) {
        if(!MicLoSBattle.gameflag) return;
        Player p = e.getEntity().getPlayer();

        p.setGameMode(GameMode.SPECTATOR);
        p.getInventory().clear();
        MicLoSBattle.config.setRemoveSurvivors(p);
    }

    //ブロックの破壊を無効にする
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(!MicLoSBattle.gameflag) return;
        e.setCancelled(true);
    }

    //TNT無効
    @EventHandler
    public void explodeEvent(EntityExplodeEvent e){
        e.setCancelled(true);
    }

    //落下ダメージを無効にする
    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if(!MicLoSBattle.gameflag) return;

        if(!(e.getEntity() instanceof Player)) return;
        if(e.getCause() != DamageCause.FALL) return;
        e.setCancelled(true);
    }
}

