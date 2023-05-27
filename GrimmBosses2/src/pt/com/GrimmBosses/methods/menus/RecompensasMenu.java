package pt.com.GrimmBosses.methods.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmBosses.methods.BPlayer;
import pt.com.GrimmBosses.methods.Recompensa;
import pt.com.GrimmBosses.utils.ItemBuilder;
import pt.com.GrimmBosses.utils.zBUtils;

public class RecompensasMenu {
	void open(Player p, int page) {
		BPlayer bp = new BPlayer(p);
		Inventory inv = Bukkit.createInventory(null, 9 * 6, "§7Bosses - Recompensas§" + page);
		List<String> lore = new ArrayList<>();
		int inicio = (page - 1) * 21;
		int fim = page * 21;
		int i = 0;
		int slot = 9;
		Map<Recompensa, Double> recompensas = bp.getRecompensas();

		lore = Arrays.asList("§eAtual §f➵ §a" + page);
		if (recompensas.size() > fim)
			inv.setItem(53, new ItemBuilder(Material.ARROW).setLore(lore).setName("§aPróxima Página").toItemStack());
		inv.setItem(45, new ItemBuilder(Material.ARROW).setName("§cVoltar").toItemStack());
		if (page > 1)
			inv.setItem(45, new ItemBuilder(Material.ARROW).setLore(lore).setName("§cPágina Anterior").toItemStack());

		lore = Arrays.asList("§7Você não tem nenhuma ", "§7recompensa por coletar! ", "", "§cClique para fechar!");
		if (recompensas.size() == 0)
			inv.setItem(22, new ItemBuilder(Material.WEB).setLore(lore).setName("§c§lVAZIO").toItemStack());
		for (Recompensa r : recompensas.keySet()) {
			if (i < inicio) {
				i++;
				continue;
			}
			if (i >= fim)
				break;
			slot = nextSlot(slot);
			ItemStack iconeFinal = new ItemBuilder(r.getIcone().clone()).addLoreLine("")
					.addLoreLine(" §7Quantia §f➵ §ax" + zBUtils.Formatar(bp.getRecQuantia(r))).toItemStack();
			inv.setItem(slot, iconeFinal);
			i++;
		}

		lore = Arrays.asList("§7Ao clicar você irá coletar ", "§7as recompensas pendentes! ", "",
				"§aClique para coletar! ");
		inv.setItem(49, new ItemBuilder(Material.ENDER_CHEST).setLore(lore).setName("§aColetar").toItemStack());

		p.openInventory(inv);
	}

	private Integer nextSlot(int i) {
		switch (i) {
		case 16:
			return 19;
		case 25:
			return 28;
		case 34:
			return null;

		default:
			return i + 1;
		}
	}

	public static Integer getPage(Inventory inv) {
		if (!inv.getName().contains("§7Bosses - Recompensas§"))
			return null;
		else
			return Integer.parseInt(inv.getName().replace("§7Bosses - Recompensas§", ""));
	}

	public RecompensasMenu(Player p, int page) {
		open(p, page);
	}
}
