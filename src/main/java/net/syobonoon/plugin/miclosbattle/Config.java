package net.syobonoon.plugin.miclosbattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Config {
    private final Plugin plugin;
    private FileConfiguration config = null;
    private List<Player> survivors;
    private Inventory inv = null;
    private final static String CHAMPION_MESSAGE = "CHAMPION!!";
    private final static double MAX_HP = 20.0;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        load_config();
    }

    public void load_config() {
        plugin.saveDefaultConfig();
        if (config != null) {
            plugin.reloadConfig();
            plugin.getServer().broadcastMessage(ChatColor.GREEN+"MicLoSBattle reload completed");
        }
        config = plugin.getConfig();
    }

    //テストメッセージを送る関数
    public void sendTestMessage(String test_str) {
        plugin.getServer().broadcastMessage(ChatColor.AQUA+test_str);
    }

    //全体メッセージを送る関数
    public void entireMessage(String str) {
        plugin.getServer().broadcastMessage(str);
    }

    //Titleを表示させる関数
    public void entireTitle(String str, ChatColor color, List<Player> online_players) {

        for (Player online_player : online_players) {
            if (online_player == null) continue;
            online_player.sendTitle(color+str, null, 0, 20, 0);
        }
    }

    //チャンピオンと表示させる関数
    public void displayChampion(Player p) {
        if (p == null) return;
        p.sendTitle(ChatColor.YELLOW + CHAMPION_MESSAGE, null, 0, 60, 0);
        entireMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.YELLOW + "がCHAMPIONになりました！");
    }

    //音を鳴らす関数
    public void entireSound(Sound se, float volume, float pitch, List<Player> online_players) {
        for (Player online_player : online_players) {
            if (online_player == null) continue;
            online_player.playSound(online_player.getLocation(), se, volume, pitch);
        }
    }

    //効果を消す関数
    public void entireClearEffect(List<Player> online_players) {
        for (Player online_player : online_players) {
            if (online_player == null) continue;

            for(PotionEffect effect : online_player.getActivePotionEffects()){
                online_player.removePotionEffect(effect.getType());
            }
        }
    }

    //効果を与える関数
    public void entireGiveEffect(PotionEffectType effect, int sec, int level, List<Player> online_players) {
        for (Player online_player : online_players) {
            if (online_player == null) continue;
            online_player.addPotionEffect(new PotionEffect(effect, sec, level));
        }
    }

    //全員の体力を満タンにする関数
    public void entireMaxHealth(List<Player> online_players) {
        for (Player online_player : online_players) {
            if (online_player == null) continue;
            online_player.getPlayer().setHealth(MAX_HP);
        }
    }

    //ゲームモードを変更する関数
    public void entireAdventureMode(GameMode gamemode, List<Player> online_players) {
        for (Player online_player : online_players) {
            if (online_player == null) continue;
            online_player.setGameMode(gamemode);
        }
    }

    //アイテムを渡す関数
    public void entireGiveItem(List<Player> online_players) {
        for (Player online_player : online_players) {
            if (online_player == null) continue;
            online_player.getInventory().setContents(this.inv.getContents());
        }
    }

    //全員を一か所にワープさせる関数
    public void entireWarp(Location loc_p, List<Player> online_players) {
        loc_p.setY(loc_p.getY() + 1);

        for (Player online_player : online_players) {
            if (online_player == null) continue;
            online_player.teleport(loc_p);
        }
    }

    public Inventory getInventoryItem() {
        return this.inv;
    }

    public List<Player> getSurvivors(){
        return this.survivors;
    }

    public void setInventoryItem(Inventory inv) {
        this.inv = inv;
    }

    public void setInitSurvivors(List<Player> players) {
        this.survivors = new ArrayList<Player>(players);
    }

    public void setRemoveSurvivors(Player p) {
        this.survivors.remove(survivors.indexOf(p));
    }
}
