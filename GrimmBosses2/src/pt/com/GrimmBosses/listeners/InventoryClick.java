package pt.com.GrimmBosses.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.methods.BPlayer;
import pt.com.GrimmBosses.methods.BossType;
import pt.com.GrimmBosses.methods.MessagesCfg;
import pt.com.GrimmBosses.methods.Recompensa;
import pt.com.GrimmBosses.methods.menus.PrincipalMenu;
import pt.com.GrimmBosses.methods.menus.RecompensasMenu;
import pt.com.GrimmBosses.methods.menus.VerBosses;
import pt.com.GrimmBosses.methods.menus.VerRecompensas;
import pt.com.GrimmBosses.utils.zBUtils;

public class InventoryClick implements Listener {
	@EventHandler
	void Principal(InventoryClickEvent e) {
		if (!e.getInventory().getName().equals("§7Bosses - Principal§d"))
			return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		switch (e.getSlot()) {
		case 11:
			p.closeInventory();
			new RecompensasMenu(p, 1);
			break;

		case 12:
			p.closeInventory();
			p.chat("/bossesloja");
			break;

		case 14:
			new VerBosses(p, 1);
			break;

		case 15:
			p.closeInventory();
			Main.top.Open(p);
			break;

		default:
			break;
		}
	}

	@EventHandler
	void TOP(InventoryClickEvent e) {
		if (!e.getInventory().getName().equals("§7Bosses - TOP§d"))
			return;
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		if (e.getSlot() == 36) {
			p.closeInventory();
			new PrincipalMenu(p);
		}
	}

	@EventHandler
	void Recompensas(InventoryClickEvent e) {
		if (!e.getInventory().getName().contains("§7Bosses - Recompensas§"))
			return;
		e.setCancelled(true);
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
			return;
		Player p = (Player) e.getWhoClicked();
		int page = RecompensasMenu.getPage(e.getInventory());
		switch (e.getSlot()) {
		case 22:
			p.closeInventory();
			break;

		case 45:
			if (page == 1) {
				p.closeInventory();
				new PrincipalMenu(p);
			} else {
				new RecompensasMenu(p, page - 1);
			}
			break;

		case 49:
			if (zBUtils.getEmptySlots(p) == 0) {
				p.closeInventory();
				p.sendMessage(MessagesCfg.msgInvFull);
				return;
			}
			BPlayer bp = new BPlayer(p);
			int coletados = 0;
			if (bp.getRecompensas().size() > 0) {
				p.closeInventory();
				Boolean b = false;
				Map<Recompensa, Double> mapClone = new HashMap<>();
				for (Recompensa rec : bp.getRecompensas().keySet())
					mapClone.put(rec, bp.getRecQuantia(rec));
				for (Recompensa rec : mapClone.keySet()) {
					Integer i = rec.giveRecompensa(p, bp.getRecQuantia(rec));
					if (i == null)
						bp.removeRecompensa(rec, Double.MAX_VALUE);
					else
						bp.removeRecompensa(rec, i);
					if (i == null || i != 0)
						coletados++;
					if (zBUtils.getEmptySlots(p) == 0) {
						b = true;
						break;
					}
				}
				bp.refreshMap();
				if (coletados != 0)
					p.sendMessage(MessagesCfg.msgColetados.replace("%quantia%", coletados + ""));
				if (b)
					p.sendMessage(MessagesCfg.msgParaColetar);
			}
			break;

		case 53:
			new RecompensasMenu(p, page + 1);
			break;

		default:
			break;
		}
	}

	@EventHandler
	void Ver(InventoryClickEvent e) {
		if (!e.getInventory().getName().contains("§7Bosses - Visualizar§"))
			return;
		e.setCancelled(true);
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
			return;
		Player p = (Player) e.getWhoClicked();
		int page = VerBosses.getPage(e.getInventory());
		switch (e.getSlot()) {
		case 45:
			if (page == 1) {
				p.closeInventory();
				new PrincipalMenu(p);
			} else {
				new VerBosses(p, page - 1);
			}
			break;

		case 53:
			new VerBosses(p, page + 1);
			break;

		default:
			BossType type = BossType.getByItemToSpawn(e.getCurrentItem());
			new VerRecompensas(p, type);
			break;
		}
	}

	@EventHandler
	void Ver1(InventoryClickEvent e) {
		if (!e.getInventory().getName().contains("§d§7Bosses - "))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	void TOPClick(InventoryClickEvent e) {
		if (!e.getInventory().getName().equalsIgnoreCase("§7Bosses - TOP§5"))
			return;
		e.setCancelled(true);
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
			return;
		Player p = (Player) e.getWhoClicked();
		switch (e.getSlot()) {
		case 39:
			p.closeInventory();
			new PrincipalMenu(p);
			break;

		default:
			break;
		}
	}
}
