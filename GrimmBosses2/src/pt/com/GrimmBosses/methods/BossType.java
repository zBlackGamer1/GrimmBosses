package pt.com.GrimmBosses.methods;

import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.kirelcodes.miniaturepets.mob.MobContainer;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.utils.zBUtils;

public class BossType {
	private String name;
	private ItemStack itemToSpawn;
	private List<Recompensa> recompensas;
	private Integer life;
	private MobContainer container;
	private List<String> hololines;
	private Integer variableLine;
	private String variableLineText;

	private Double xPos;
	private Double yPos;
	private Double zPos;

	public BossType(String name, ItemStack itemToSpawn, List<Recompensa> recompensas, Integer life,
			MobContainer mobcontainer, List<String> hololines, String pos) {
		this.name = name;
		this.itemToSpawn = itemToSpawn;
		this.recompensas = recompensas;
		this.life = life;
		this.container = mobcontainer;
		this.xPos = Double.valueOf(pos.split(",")[0]);
		this.yPos = Double.valueOf(pos.split(",")[1]);
		this.zPos = Double.valueOf(pos.split(",")[2]);
		this.hololines = zBUtils.replaceList(zBUtils.replaceList(hololines, "&", "§"), "%max-hp%", life + "");
		int i = 0;
		for (String s : hololines) {
			if (s.contains("%hp%")) {
				variableLine = i;
				variableLineText = s;
				break;
			}
			i++;
		}
	}

	public Double getXPos() {
		return this.xPos;
	}

	public Double getYPos() {
		return this.yPos;
	}

	public Double getZPos() {
		return this.zPos;
	}

	public Integer getVariableLine() {
		return this.variableLine;
	}

	public String getVariableLineText() {
		return this.variableLineText;
	}

	public MobContainer getContainer() {
		return container;
	}

	public Integer getLife() {
		return life;
	}

	public List<Recompensa> getRecompensas() {
		return recompensas;
	}

	public ItemStack getItemToSpawn() {
		return itemToSpawn;
	}

	public String getName() {
		return name;
	}

	public List<String> getHologramLines() {
		return hololines;
	}

	public static BossType getByName(String name) {
		for (BossType type : Main.cache.BossTypes)
			if (type.getName().equalsIgnoreCase(name))
				return type;
		return null;
	}

	public static BossType getByItemToSpawn(ItemStack item) {
		if (!isItemToSpawn(item))
			return null;
		return getBossToSpawn(item);
	}

	public static Boolean isItemToSpawn(ItemStack item) {
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
		return tag.hasKey("grimmboss");
	}

	public static BossType getBossToSpawn(ItemStack itemToSpawn) {
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemToSpawn);
		NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
		return getByName(tag.getString("grimmboss"));
	}
}
