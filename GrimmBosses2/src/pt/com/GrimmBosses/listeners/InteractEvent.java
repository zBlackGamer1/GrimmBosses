package pt.com.GrimmBosses.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.methods.Boss;
import pt.com.GrimmBosses.methods.BossType;
import pt.com.GrimmBosses.methods.MessagesCfg;

public class InteractEvent implements Listener {
	@EventHandler
	void onInteract(PlayerInteractEvent e) {
		if (e.isCancelled()) return;
		if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(!e.getClickedBlock().getWorld().getName().equalsIgnoreCase(Main.cache.plotWorld.getName())) return;
		if (!BossType.isItemToSpawn(e.getItem())) return;
		e.setCancelled(true);
		Player p = e.getPlayer();
		Boolean mudou = false;
		Location loc = getBestLoc(e.getClickedBlock().getLocation());
		Location loc1 = e.getClickedBlock().getLocation().clone().add(0, 1, 0);
		if (loc.getY() != loc1.getY()) mudou = true;
		if (hasNearbyEntities(loc)) {
			p.sendMessage(MessagesCfg.msgNearbyBoss);
			return;
		}
		Boss b = new Boss(BossType.getBossToSpawn(e.getItem()), loc.clone().add(0.5, 0, 0.5),
				e.getPlayer().getName().toLowerCase());
		b.spawnBoss();
		removeItem(e.getPlayer());
		p.sendMessage(MessagesCfg.msgSpawnedBoss.replace("%tipo%", b.getType().getName()));
		if (mudou)
			p.sendMessage(MessagesCfg.msgTPBoss);
	}

	boolean hasNearbyEntities(Location loc) {
		int i = 0;
		for (Entity e : loc.getWorld().getNearbyEntities(loc, 5.0, 5.0, 5.0)) {
			if (e.getType() == EntityType.DROPPED_ITEM) continue;
			if (e.getType() == EntityType.ARROW) continue;
			if (e.getType() == EntityType.PLAYER) continue;
			if (e.getType() == EntityType.ITEM_FRAME) continue;
			i++;
		}
		if (i == 0) return false;
		else return true;
	}

	Location getBestLoc(Location loc) {
		for (int i = 0; i < 256; i++) {
			if (loc.clone().add(0, i, 0).getBlock().getType() == Material.AIR) {
				return loc.clone().add(0, i, 0);
			}
		}
		return loc;
	}

	void removeItem(Player p) {
		int amount = p.getItemInHand().getAmount();
		if (amount == 1)
			p.getInventory().removeItem(p.getItemInHand());
		else
			p.getItemInHand().setAmount(amount - 1);
	}
}
