package pt.com.GrimmBosses.methods.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.methods.BossType;
import pt.com.GrimmBosses.utils.ItemBuilder;

public class VerBosses {
	void open(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 9 * 6, "§7Bosses - Visualizar§" + page);
		List<String> lore = new ArrayList<>();
		int inicio = (page - 1) * 15;
		int fim = page * 15;
		int i = 0;
		int slot = 10;
		List<BossType> types = Main.cache.BossTypes;
		lore = Arrays.asList("§eAtual §f➵ §a" + page);
		if (types.size() > fim)
			inv.setItem(53, new ItemBuilder(Material.ARROW).setLore(lore).setName("§aPróxima Página").toItemStack());
		inv.setItem(45, new ItemBuilder(Material.ARROW).setName("§cVoltar").toItemStack());
		if (page > 1)
			inv.setItem(45, new ItemBuilder(Material.ARROW).setLore(lore).setName("§cPágina Anterior").toItemStack());

		lore = Arrays.asList("§7Ainda não foi criado ", "§7nenhum boss! ", "");
		if (types.size() == 0)
			inv.setItem(22, new ItemBuilder(Material.WEB).setLore(lore).setName("§c§lNADA").toItemStack());
		for (BossType type : types) {
			if (i < inicio) {
				i++;
				continue;
			}
			if (i >= fim)
				break;
			slot = nextSlot(slot);
			lore = Arrays.asList("", "  §cHP ❤ §e" + type.getLife(), "", "§eClique para ver recompensas!");
			ItemStack iconeFinal = new ItemBuilder(type.getItemToSpawn().clone()).setLore(lore).toItemStack();
			inv.setItem(slot, iconeFinal);
			i++;
		}
		p.openInventory(inv);
	}

	public VerBosses(Player p, Integer page) {
		open(p, page);
	}

	private Integer nextSlot(int i) {
		switch (i) {
		case 15:
			return 20;
		case 24:
			return 29;
		case 33:
			return null;

		default:
			return i + 1;
		}
	}

	public static Integer getPage(Inventory inv) {
		if (!inv.getName().contains("§7Bosses - Visualizar§"))
			return null;
		else
			return Integer.parseInt(inv.getName().replace("§7Bosses - Visualizar§", ""));
	}
}
