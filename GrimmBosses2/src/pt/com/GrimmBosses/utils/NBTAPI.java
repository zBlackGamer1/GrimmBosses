package pt.com.GrimmBosses.utils;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class NBTAPI {
	private NBTTagCompound nbt;
	private net.minecraft.server.v1_8_R3.ItemStack stack;

	public void setDouble(String key, Double value) {
		this.nbt.setDouble(key, value);
	}

	public void setString(String key, String value) {
		this.nbt.setString(key, value);
	}

	public void setInt(String key, Integer value) {
		this.nbt.setInt(key, value);
	}

	public Double getDouble(String key) {
		return this.nbt.getDouble(key);
	}

	public String getString(String key) {
		return this.nbt.getString(key);
	}

	public Integer getInt(String key) {
		return this.nbt.getInt(key);
	}

	public Boolean hasKey(String key) {
		return nbt.hasKey(key);
	}

	public ItemStack getItem() {
		stack.setTag(nbt);
		return CraftItemStack.asCraftMirror(stack);
	}

	public NBTTagCompound getNBT() {
		return this.nbt;
	}

	public NBTAPI(ItemStack item) {
		this.stack = CraftItemStack.asNMSCopy(item);
		this.nbt = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
	}

	public static NBTAPI getNBT(ItemStack item) {
		return new NBTAPI(item);
	}
}
