package pt.com.GrimmBosses.methods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.kirelcodes.miniaturepets.mob.MobContainer;
import com.kirelcodes.miniaturepets.utils.APIUtils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.utils.CustomConfig;
import pt.com.GrimmBosses.utils.CustomHead;
import pt.com.GrimmBosses.utils.ItemBuilder;
import pt.com.GrimmBosses.utils.NBTAPI;
import pt.com.GrimmBosses.utils.zBUtils;

public class Cache {
	public World plotWorld;
	public Map<Location, Boss> allBosses;
	public List<BossType> BossTypes;

	public Map<String, Map<StatType, Double>> stats;
	public Map<String, Map<Recompensa, Double>> recompensas;

	public Inventory MainMenu;
	public Map<Integer, MenuCategorias> SlotsCorrespondence;
	public Integer statsSlot;

	public Map<String, Matadora> allMatadoras;

	public Cache() {
		this.allBosses = new HashMap<>();
		this.BossTypes = new ArrayList<>();
		this.recompensas = new HashMap<>();
		this.stats = new HashMap<>();
		this.allMatadoras = new HashMap<>();
		this.plotWorld = Bukkit.getWorld(Main.getInstance().getConfig().getString("plotworld"));
		
		carregarTypes();
		carregarMenu();
		carregarMatadoras();
	}

	void carregarTypes() {
		CustomConfig bosses = Main.getInstance().bossescfg;
		for (String s : bosses.getConfig().getKeys(false)) {
			String name = s;

			net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(getItemInCfg(s));
			NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
			tag.setString("grimmboss", name);
			stack.setTag(tag);
			ItemStack its = CraftItemStack.asCraftMirror(stack);

			List<Recompensa> ldr = new ArrayList<>();
			Integer HP = bosses.getInt(s + ".HP");
			MobContainer mc = getByFile(bosses.getString(s + ".skin-file"));
			if (mc == null) {
				zBUtils.ConsoleMsg("§cATENÇÃO! N§o foi possivel carregar o boss " + s.toLowerCase()
						+ " (Arquivo de pet inexistente).");
				return;
			}
			for (String recompensa : bosses.getConfig().getConfigurationSection(s + ".Recompensas").getKeys(false)) {
				String r = bosses.getString(s + ".Recompensas." + recompensa + ".Recompensa");
				Double chance = bosses.getDouble(s + ".Recompensas." + recompensa + ".Chance") * 10;
				if (r.contains("CMD:")) {
					Recompensa rec = new Recompensa(name + recompensa, getItemInCfg(s + ".Recompensas." + recompensa),
							r.replace("CMD: ", "").replace("CMD:", "").replace("/", ""), chance.intValue());
					ldr.add(rec);
				} else {
					ItemStack i = getItemInCfg(s + ".Recompensas." + recompensa);
					Recompensa rec = new Recompensa(name + recompensa, i, i, chance.intValue());
					ldr.add(rec);
				}
			}
			BossTypes.add(new BossType(name, its, ldr, HP, mc, bosses.getStringList(s + ".Holograma"),
					bosses.getString(s + ".Holograma-pos")));
		}
	}

	public static MobContainer getByFile(String fileName) {
		Plugin pl = Bukkit.getServer().getPluginManager().getPlugin("MiniaturePets");
		File BASE_DIR = new File(pl.getDataFolder() + "/pets/");
		if (!fileName.endsWith(".mpet"))
			fileName = fileName + ".mpet";
		File file = new File(BASE_DIR, fileName);
		return APIUtils.loadContainer(file);
	}

	ItemStack getItemInCfg(String name) {
		CustomConfig bosses = Main.getInstance().bossescfg;
		String s = name + ".Item.";
		String inicial = bosses.getString(s + "ID");
		if (inicial.contains("https://textures.minecraft.net/texture/"))
			inicial = bosses.getString(s + "ID").replace("https://textures.minecraft.net/texture/", "");
		ItemStack base = new ItemStack(Material.DIRT);
		if (inicial.contains("SKULL:"))
			base = CustomHead.setSkull(
					"https://textures.minecraft.net/texture/" + inicial.replace("SKULL:", "").replace(" ", ""));
		else
			base = zBUtils.getItemByID(inicial);

		return new ItemBuilder(base).setName(bosses.getString(s + "Name").replace("&", "§"))
				.setLore(zBUtils.replaceList(bosses.getStringList(s + "Lore"), "&", "§")).toItemStack();
	}

	void carregarMenu() {
		CustomConfig cfg = Main.getInstance().menucfg;
		SlotsCorrespondence = new HashMap<>();
		Inventory inv = Bukkit.createInventory(null, 9 * 4, "§7Bosses - Principal§d");

		for (String s : cfg.getConfig().getConfigurationSection("Menu-Principal").getKeys(false)) {
			if (s.equalsIgnoreCase("stats")) {
				statsSlot = cfg.getInt("Menu-Principal." + s + ".slot");
				continue;
			}
			ItemStack item;
			if (cfg.getString("Menu-Principal." + s + ".ID").contains("SKULL:")) {
				item = CustomHead.setSkull("https://textures.minecraft.net/texture/"
						+ cfg.getString("Menu-Principal." + s + ".ID").replace("SKULL: ", ""));
			} else {
				item = zBUtils.getItemByID("Menu-Principal." + s + ".ID");
			}
			ItemStack itemFinal = new ItemBuilder(item)
					.setName(cfg.getString("Menu-Principal." + s + ".display").replace("&", "§"))
					.setLore(zBUtils.replaceList(cfg.getStringList("Menu-Principal." + s + ".lore"), "&", "§"))
					.toItemStack();
			inv.setItem(cfg.getInt("Menu-Principal." + s + ".slot"), itemFinal);
			SlotsCorrespondence.put(cfg.getInt("Menu-Principal." + s + ".slot"),
					MenuCategorias.valueOf(s.toUpperCase()));
		}
		MainMenu = inv;
	}

	void carregarMatadoras() {
		CustomConfig cfg = Main.getInstance().matadorascfg;
		for (String s : cfg.getConfig().getKeys(false)) {
			ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
					.setName(cfg.getString(s + ".Display").replace("&", "§"))
					.setLore(zBUtils.replaceList(cfg.getStringList(s + ".Lore"), "&", "§")).toItemStack();
			NBTAPI nbt = NBTAPI.getNBT(item);
			nbt.setInt("grimmmatadora", cfg.getInt(s + ".Dano"));
			Matadora m = new Matadora(nbt.getItem());
			allMatadoras.put(s, m);
		}
	}
}
