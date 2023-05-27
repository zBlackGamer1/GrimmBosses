package pt.com.GrimmBosses.methods.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.methods.BPlayer;
import pt.com.GrimmBosses.utils.ItemBuilder;
import pt.com.GrimmBosses.utils.zBUtils;

public class PrincipalMenu {
	void open(Player p) {
		BPlayer bp = new BPlayer(p);
		Inventory inv = Main.cache.MainMenu;
		List<String> lore = new ArrayList<>();

		lore = Arrays.asList("§7Veja as suas informações ", "§7referentes aos bosses!", "",
				"§e Bosses mortos: §7" + zBUtils.Formatar(bp.getMortos()) + " ",
				"§e Dano causado: §7" + Main.formatter.formatNumber(bp.getDano()) + "§c ❤ ", "");
		ItemStack stats = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		inv.setItem(Main.cache.statsSlot, new ItemBuilder(stats).setLore(lore).setSkullOwner(p.getName())
				.setName("§aSuas informações").toItemStack());

		p.openInventory(inv);
	}

	public PrincipalMenu(Player p) {
		open(p);
	}
}
