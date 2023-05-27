package pt.com.GrimmBosses.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.utils.CustomHead;
import pt.com.GrimmBosses.utils.ItemBuilder;
import pt.com.GrimmBosses.utils.zBUtils;

public class TOP {
	Inventory inv;
	Long atualizando = (long) (60000 * 5);
	Map<String, Double> todos = new HashMap<>();
	List<Entry<String, Double>> valores;

	public void StartAtualizador() {
		atualizar();
		new BukkitRunnable() {

			@Override
			public void run() {
				atualizando -= 1000;
				if (atualizando <= 0) {
					atualizar();
					atualizando = (long) (60000 * 5);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 20L);
	}

	void atualizar() {
		todos.clear();
		for (String nick : Main.cache.stats.keySet()) {
			BPlayer bp = new BPlayer(nick);
			todos.put(nick, bp.getDano());
		}
		Stream<Entry<String, Double>> streamOrdenada = todos.entrySet().stream()
				.sorted((x, y) -> y.getValue().compareTo(x.getValue()));
		valores = streamOrdenada.collect(Collectors.toList());

		int position = 1;
		int slot = 10;
		Inventory in = Bukkit.createInventory(null, 5 * 9, "§7Bosses - TOP§5");
		setIndisponiveis(in);
		for (Entry<String, Double> entry : valores) {
			if (position <= 10) {
				@SuppressWarnings("deprecation")
				String name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
				Double dano = new BPlayer(entry.getKey()).getDano();

				if (slot == 15)
					slot = 20;
				else
					slot++;
				in.setItem(slot, getItem(position, name, dano));

			}
			position++;
		}
		in.setItem(39, VoltarItem());
		if (in.getItem(11) == null)
			inv.setItem(11, Indisponivel());
		inv = in;
	}

	ItemStack getItem(Integer position, String name, Double value) {
		ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta m = (SkullMeta) i.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add("§b ▶ §7Posição: §f" + position + "º   ");
		lore.add("§b ▶ §7Jogador: §f" + name + "   ");
		lore.add("§b ▶ §7Dano: §f" + Main.formatter.formatNumber(value) + "   ");
		m.setLore(lore);
		m.setDisplayName("§e" + name);
		m.setOwner(name);
		i.setItemMeta(m);
		return i;
	}

	ItemStack VoltarItem() {
		ItemStack i = new ItemStack(Material.ARROW);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName("§cVoltar");
		i.setItemMeta(m);
		return i;
	}

	ItemStack Indisponivel() {
		ItemStack i = CustomHead.setSkull(
				"http://textures.minecraft.net/texture/3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025");
		SkullMeta m = (SkullMeta) i.getItemMeta();
		m.setDisplayName("§cNenhum");
		i.setItemMeta(m);
		return i;
	}

	void setIndisponiveis(Inventory in) {
		in.setItem(11, Indisponivel());
		in.setItem(12, Indisponivel());
		in.setItem(13, Indisponivel());
		in.setItem(14, Indisponivel());
		in.setItem(15, Indisponivel());
		in.setItem(20, Indisponivel());
		in.setItem(21, Indisponivel());
		in.setItem(22, Indisponivel());
		in.setItem(23, Indisponivel());
		in.setItem(24, Indisponivel());
	}

	public void Open(Player p) {
		BPlayer bp = new BPlayer(p);
		List<String> lore = new ArrayList<>();
		lore = Arrays.asList("§7Veja as suas informações ", "§7referentes aos bosses!", "",
				"§e Bosses mortos: §7" + zBUtils.Formatar(bp.getMortos()) + " ",
				"§e Dano causado: §7" + Main.formatter.formatNumber(bp.getDano()) + "§c ❤ ", "");
		ItemStack stats = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		ItemStack sfinal = new ItemBuilder(stats).setLore(lore).setSkullOwner(p.getName()).setName("§aSuas informações")
				.toItemStack();

		inv.setItem(40, sfinal);
		inv.setItem(41, TimeItem());
		p.openInventory(inv);
	}

	ItemStack TimeItem() {
		ItemStack i = new ItemStack(Material.WATCH);
		ItemMeta m = i.getItemMeta();
		List<String> lore = new ArrayList<>();
		int min = (int) (atualizando / 60000);
		int sec = (int) ((atualizando / 1000) - (min * 60));
		lore.add("§b ▶ §7Atualizando em: §f" + min + "m " + sec + "s.   ");
		m.setDisplayName("§eTempo");
		m.setLore(lore);
		i.setItemMeta(m);
		return i;
	}
}
