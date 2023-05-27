package pt.com.GrimmBosses.methods;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BossKillEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Boss boss;
	private Player killer;

	public BossKillEvent(Boss boss, Player killer) {
		this.boss = boss;
		this.killer = killer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Boss getBoss() {
		return boss;
	}

	public Player getKiller() {
		return killer;
	}

	public BPlayer getBPlayer() {
		return new BPlayer(killer);
	}
}
