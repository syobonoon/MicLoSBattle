package net.syobonoon.plugin.miclosbattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

class Timer extends BukkitRunnable{
	int time;
	SpreadPlayers spreadplayer;
	Ring ring;
	CommandSender sender;
	Player p;
	World world;
	Block chest;
	Chest chest_inv;
	BlockState chest_state;
	String[] args;
	Location loc_p;
	JavaPlugin plugin;
	BukkitTask task;
	private int dist;
	private int seconds;
	List<Player> online_players = new ArrayList<>(Bukkit.getOnlinePlayers());;
	private String [] startmessage_list = {"3","2","1","START!"};
	private final static int MAX_BATTLE_TIME = 1800;
	private final static int SPEED_LEVEL = 2;
	private final static int JUMP_LEVEL = 3;


	public Timer(JavaPlugin plugin ,int i, CommandSender sender, String[] args, int dist, int seconds) {
		this.time = i;
		this.plugin = plugin;
		this.sender = sender;
		this.args = args;
		this.dist = dist;
		this.seconds = seconds;

		p = (Player)sender;
		world = p.getWorld();

		this.loc_p = p.getLocation();
		chest = world.getBlockAt(loc_p);
		chest_state = chest.getState();

		spreadplayer = new SpreadPlayers(sender, args, online_players);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		//初期チェック
		if(time == 0) {
			if(!spreadplayer.spreadcheck() || (!(chest_state instanceof Chest)) && MicLoSBattle.config.getInventoryItem() == null) {
				MicLoSBattle.config.entireMessage(ChatColor.RED + "ゲームの開始に失敗しました");
				MicLoSBattle.gameflag = false;
				plugin.getServer().getScheduler().cancelTask(task.getTaskId());
				return;
			}
			MicLoSBattle.config.entireMessage(ChatColor.GREEN + "ゲームの開始に成功しました");
			MicLoSBattle.gameflag = true;
			MicLoSBattle.config.setInitSurvivors(online_players);
		}

		if(1 <= time && time <= 3){ //3, 2, 1
			MicLoSBattle.config.entireTitle(startmessage_list[time-1], ChatColor.WHITE, online_players);
			//MicLoSBattle.config.entireSound(Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F, online_players);
		}else if(time == 4) { //start
			MicLoSBattle.config.entireTitle(startmessage_list[time-1], ChatColor.GREEN, online_players);
			//MicLoSBattle.config.entireSound(Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 2.0F, online_players);


			//リングのインスタンスを生成
			ring = new Ring(Double.valueOf(args[0]), Double.valueOf(args[1]), dist, seconds, world);

			//全員をアドベンチャーモードに変更
			MicLoSBattle.config.entireAdventureMode(GameMode.ADVENTURE, online_players);

			//全員の体力を満タンにする
			MicLoSBattle.config.entireMaxHealth(online_players);

			//全員のエフェクトを消す
			MicLoSBattle.config.entireClearEffect(online_players);

			//プレイヤーを散開させる
			spreadplayer.excutespread();

			//プレイヤーに速度とジャンプ力を与える
			MicLoSBattle.config.entireGiveEffect(PotionEffectType.SPEED, MAX_BATTLE_TIME*20, SPEED_LEVEL, online_players);
			MicLoSBattle.config.entireGiveEffect(PotionEffectType.JUMP, MAX_BATTLE_TIME*20, JUMP_LEVEL, online_players);

			//指定の位置にあるチェストの中身をすべてのプレイヤーに与える
			//下のブロックがチェストの場合、invを更新
			if(chest_state instanceof Chest) {
				chest_state = chest.getState();
				chest_inv = (Chest)chest_state;
				Inventory inv = chest_inv.getBlockInventory();
				MicLoSBattle.config.setInventoryItem(inv);
			}

			MicLoSBattle.config.entireGiveItem(online_players);

			//リング収縮
			ring.excuteRing();

		}else if(time >= MAX_BATTLE_TIME || MicLoSBattle.gameflag == false){ //指定時間以上、ゲームフラグがfalseならば終了
			ring.wb.reset();
			MicLoSBattle.config.entireAdventureMode(GameMode.CREATIVE, online_players);
			MicLoSBattle.config.entireClearEffect(online_players);
			MicLoSBattle.config.entireWarp(loc_p, online_players);
			MicLoSBattle.gameflag = false;
			MicLoSBattle.config.entireMessage(ChatColor.RED + "ゲームが終了しました");
			plugin.getServer().getScheduler().cancelTask(task.getTaskId());//自分自身を止める
		}else if(MicLoSBattle.config.getSurvivors().size() <= 1 && time >= 6) { //生き残ってるプレイヤーが1人以下になったらチャンピオンと表示して終了
			if(MicLoSBattle.config.getSurvivors().size() == 1) MicLoSBattle.config.displayChampion(MicLoSBattle.config.getSurvivors().get(0));

			ring.wb.reset();
			MicLoSBattle.config.entireAdventureMode(GameMode.CREATIVE, online_players);
			MicLoSBattle.config.entireClearEffect(online_players);
			MicLoSBattle.config.entireWarp(loc_p, online_players);
			MicLoSBattle.gameflag = false;
			MicLoSBattle.config.entireMessage(ChatColor.RED + "ゲームが終了しました");
			plugin.getServer().getScheduler().cancelTask(task.getTaskId());//自分自身を止める
		}

		time++;
	}

	public BukkitTask getTask() {
		return this.task;
	}

	public void setTask(BukkitTask task) {
		this.task = task;
	}
}
