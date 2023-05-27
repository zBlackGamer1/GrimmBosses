package pt.com.GrimmBosses.methods;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import pt.com.GrimmBosses.Main;

public class Matadora {
	private ItemStack item;

	public ItemStack getItem() {
		return this.item;
	}

	public Matadora(ItemStack item) {
		this.item = item;
	}

	public static Boolean isMatadora(ItemStack item) {
		if (item == null)
			return false;
		if (item.getType() != Material.DIAMOND_SWORD)
			return false;
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
		return tag.hasKey("grimmmatadora");
	}

	public static Integer getDano(ItemStack item) {
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
		return tag.getInt("grimmmatadora");
	}

	public static Matadora getByName(String s) {
		return Main.cache.allMatadoras.get(s);
	}
}
