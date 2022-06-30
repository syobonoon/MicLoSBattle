package net.syobonoon.plugin.miclosbattle;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class Ring {
	WorldBorder wb;
	private int seconds;

	public Ring(double x, double z, int ringsize, int seconds, World world) {
		wb = world.getWorldBorder();
		wb.setCenter(x, z);
		wb.setSize(ringsize);
		this.seconds = seconds;
	}

	public void excuteRing() {
		wb.setSize(0, seconds);
	}

	public double getRingSize() {
		return this.wb.getSize();
	}
}
