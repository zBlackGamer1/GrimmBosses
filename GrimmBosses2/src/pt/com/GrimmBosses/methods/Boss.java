package pt.com.GrimmBosses.methods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.kirelcodes.miniaturepets.mob.Mob;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import pt.com.GrimmBosses.Main;

public class Boss {
	private BossType type;
	private Location location;
	private Integer actualLife;
	private Mob mob;
	private String owner;
	private Hologram holograma;
	public Boss(BossType type, Location loc, String owner) {
		this.type = type;
		this.location = loc;
		this.actualLife = type.getLife();
		this.owner = owner;
		Main.cache.allBosses.put(location, this);
	}

	public BossType getType() {
		return type;
	}

	public Location getLocation() {
		return location;
	}

	public Integer getActualLife() {
		return actualLife;
	}

	public Mob getMob() {
		return mob;
	}

	public void damage(Integer value) {
		if (value > actualLife) {
			this.actualLife = 0;
			return;
		}
		this.actualLife = actualLife - value;
		if (this.holograma != null) {
			TextLine tl = (TextLine) holograma.getLine(type.getVariableLine());
			tl.setText(type.getVariableLineText().replace("%hp%", actualLife + "").replace("&", "§").replace("%max-hp%",
					type.getLife() + ""));
		}
	}

	public String getOwner() {
		return this.owner;
	}

	public Hologram getHolograma() {
		return this.holograma;
	}

	public void deleteBoss(Player killer) {
		BossKillEvent evento = new BossKillEvent(this, killer);
		Bukkit.getPluginManager().callEvent(evento);
	}

	public void setLife(Integer life) {
		this.actualLife = life;
	}

	public void spawnBoss() {
		Mob mob = new Mob(this.location, type.getContainer());
		mob.getNavigator().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 6));

		final net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) mob.getNavigator()).getHandle();
		NBTTagCompound tag = nmsEntity.getNBTTag();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		nmsEntity.c(tag);
		tag.setInt("NoAI", 1);
		tag.setInt("NoGravity", 0);
		nmsEntity.f(tag);
		mob.getNavigator().setMaxHealth(1000);
		mob.getNavigator().setHealth(1000);
		mob.getNavigator().setMetadata("boss", new FixedMetadataValue(Main.getInstance(), true));
		if (this.holograma == null) {
			Hologram holo = HologramsAPI.createHologram(Main.getInstance(),
					this.location.clone().add(type.getXPos(), type.getYPos(), type.getZPos()));
			for (String s : type.getHologramLines())
				holo.appendTextLine(s.replace("%hp%", getActualLife() + ""));
			this.holograma = holo;
		}
		this.mob = mob;
	}

	public static Boss getByLocation(Location loc) {
		if (Main.cache.allBosses.containsKey(loc))
			return Main.cache.allBosses.get(loc);
		return null;
	}
}
