package pt.com.GrimmBosses.methods.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmBosses.methods.BossType;
import pt.com.GrimmBosses.methods.Recompensa;
import pt.com.GrimmBosses.utils.ItemBuilder;
import pt.com.GrimmBosses.utils.zBUtils;

public class VerRecompensas {
	void open(Player p, BossType type) {
		Inventory inv = Bukkit.createInventory(null, 9 * 6, "§d§7Bosses - " + type.getName());
		List<String> lore = new ArrayList<>();
		int slot = 9;
		int i = 0;

		lore = Arrays.asList("§7Este boss não ", "§7tem qualquer recompensa! ", "");
		if (type.getRecompensas().size() == 0)
			inv.setItem(22, new ItemBuilder(Material.WEB).setLore(lore).setName("§c§lNADA").toItemStack());
		for (Recompensa r : type.getRecompensas()) {
			if (i >= 28)
				break;
			slot = nextSlot(slot);
			Double chance = (double) (r.getChance() / 10);
			ItemStack iconeFinal = new ItemBuilder(r.getIcone().clone()).addLoreLine("")
					.addLoreLine("§e§lCHANCE: §f" + zBUtils.FormatarChances(chance) + "%").toItemStack();
			inv.setItem(slot, iconeFinal);
			i++;
		}
		p.openInventory(inv);
	}

	public VerRecompensas(Player p, BossType type) {
		open(p, type);
	}

	private Integer nextSlot(int i) {
		switch (i) {
		case 16:
			return 19;
		case 25:
			return 28;
		case 34:
			return 37;
		case 43:
			return null;
		default:
			return i + 1;
		}
	}
}
