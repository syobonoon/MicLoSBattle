package net.syobonoon.plugin.miclosbattle;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BattleCommand implements TabExecutor {
    BukkitTask task;
    JavaPlugin plugin;
    Timer timer;
    private int dist = 150;
    private int seconds = 420;
    private double spread_max = dist / 2 - 5;
    private double spread_ave = spread_max / 2;
    private String[] args_send = new String[4];

    public BattleCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isSuccess = false;
        if (command.getName().equalsIgnoreCase("bstart")) {
            isSuccess = bstart(sender, args);
        }
        else if (command.getName().equalsIgnoreCase("breload")) {
            isSuccess = breload(sender, args);
        }
        else if (command.getName().equalsIgnoreCase("bstop")) {
            isSuccess = bstop(sender, args);
        }
        return isSuccess;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return null;
        return null;
    }

    @SuppressWarnings("deprecation")
    private boolean bstart(CommandSender sender, String[] args) { //<リングの大きさ> <秒数>
        if(MicLoSBattle.gameflag) {
            sender.sendMessage(ChatColor.RED + "すでにゲームが開始されています");
            return false;
        }
        if(args.length != 0 && args.length != 2) {
            sender.sendMessage(ChatColor.RED + "parameter error");
            return false;
        }

        Player p = (Player)sender;
        if(args.length == 2){
            dist = Integer.valueOf(args[0]);
            spread_max = dist / 2 - 5;
            seconds = Integer.valueOf(args[1]);
        }

        args_send[0] = String.valueOf(p.getLocation().getX());
        args_send[1] = String.valueOf(p.getLocation().getZ());
        args_send[2] = String.valueOf(spread_ave);
        args_send[3] = String.valueOf(spread_max);

        //スケジューラー起動
        timer = new Timer(plugin, 0, sender, args_send, dist, seconds);
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, timer, 0L, 20L);

        timer.setTask(task);

        return true;
    }

    private boolean bstop(CommandSender sender, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "parameter error");
            return false;
        }
        //現在ゲーム中かチェック
        if (!MicLoSBattle.gameflag) {
            sender.sendMessage(ChatColor.RED + "ゲーム中ではないので実行できません");
            return false;
        }
        MicLoSBattle.gameflag = false;

        return true;
    }

    private boolean breload(CommandSender sender, String[] args) {
        if (args.length != 0) return false;
        MicLoSBattle.config.load_config();
        return true;
    }
}
