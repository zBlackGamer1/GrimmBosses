package pt.com.GrimmBosses.methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.utils.zBUtils;

public class Recompensa {
	private String identifier;
	private ItemStack icone;
	private ItemStack recompensaItem;
	private String recompensaCMD;
	private Integer chance;

	public String getIdentifier() {
		return identifier;
	}

	public ItemStack getIcone() {
		return this.icone;
	}

	public Integer getChance() {
		return chance;
	}

	public Integer giveRecompensa(Player p, Double quantia) {
		if (recompensaCMD != null) {
			if (zBUtils.getEmptySlots(p) == 0) {
				return 0;
			}
			int in = 0;
			for (int i = 0; i < quantia.intValue(); i++) {
				if (zBUtils.getEmptySlots(p) == 0)
					break;
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						recompensaCMD.replace("%player%", p.getName()));
				in++;
			}
			return in;
		}
		if (recompensaItem != null) {
			if (zBUtils.getEmptySlots(p) == 0)
				return 0;
			ItemStack i = recompensaItem.clone();
			Integer q = quantia.intValue() * recompensaItem.getAmount();
			i.setAmount(q);
			if (q > zBUtils.getEmptySlots(p)) {
				q = zBUtils.getEmptySlots(p);
				i.setAmount(zBUtils.getEmptySlots(p));
			}
			p.getInventory().addItem(i);
			return q;
		}
		return null;
	}

	public Recompensa(String identifier, ItemStack icone, ItemStack rewardItem, Integer chance) {
		this.identifier = identifier;
		if (icone == null || icone.getType() == Material.AIR)
			this.icone = new ItemStack(Material.DIRT);
		else
			this.icone = icone;
		this.recompensaItem = rewardItem;
		this.recompensaCMD = null;
		this.chance = chance;
	}

	public Recompensa(String identifier, ItemStack icone, String rewardCMD, Integer chance) {
		this.identifier = identifier;
		if (icone == null || icone.getType() == Material.AIR)
			this.icone = new ItemStack(Material.DIRT);
		else
			this.icone = icone;
		this.recompensaCMD = rewardCMD.replace("/", "");
		this.recompensaItem = null;
		this.chance = chance;
	}

	public static Recompensa getByIdentifier(String identifier) {
		for (BossType type : Main.cache.BossTypes) {
			for (Recompensa r : type.getRecompensas())
				if (r.getIdentifier().equalsIgnoreCase(identifier))
					return r;
		}
		return null;
	}
}
