package pt.com.GrimmBosses.methods;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import pt.com.GrimmBosses.Main;

public class BPlayer {
	private String name;
	private Map<Recompensa, Double> recompensas;

	public Boolean isOwner(Boss boss) {
		if (boss.getOwner().equalsIgnoreCase(name))
			return true;
		else
			return false;
	}

	public Double getMortos() {
		if (!Main.cache.stats.containsKey(this.name))
			return 0.0;
		Map<StatType, Double> map;
		if (!Main.cache.stats.containsKey(this.name))
			map = new HashMap<>();
		else
			map = Main.cache.stats.get(this.name);

		if (map.containsKey(StatType.KILLED))
			return map.get(StatType.KILLED);
		else
			return 0.0;
	}

	public Double getDano() {
		if (!Main.cache.stats.containsKey(this.name))
			return 0.0;
		Map<StatType, Double> map;
		if (!Main.cache.stats.containsKey(this.name))
			map = new HashMap<>();
		else
			map = Main.cache.stats.get(this.name);

		if (map.containsKey(StatType.DAMAGE))
			return map.get(StatType.DAMAGE);
		else
			return 0.0;
	}

	public void addMortos() {
		Map<StatType, Double> map;
		if (!Main.cache.stats.containsKey(this.name))
			map = new HashMap<>();
		else
			map = Main.cache.stats.get(this.name);
		map.put(StatType.KILLED, getMortos() + 1);
		Main.cache.stats.put(this.name, map);
	}

	public void addDano(Integer dano) {
		Map<StatType, Double> map;
		if (!Main.cache.stats.containsKey(this.name))
			map = new HashMap<>();
		else
			map = Main.cache.stats.get(this.name);
		map.put(StatType.DAMAGE, getDano() + dano);
		Main.cache.stats.put(this.name, map);
	}

	public Map<Recompensa, Double> getRecompensas() {
		return this.recompensas;
	}

	public void addRecompensa(Recompensa recompensa, double quantia) {
		if (recompensas.containsKey(recompensa))
			recompensas.put(recompensa, recompensas.get(recompensa) + quantia);
		else
			recompensas.put(recompensa, quantia);
	}

	public void removeRecompensa(Recompensa recompensa, double quantia) {
		if (!recompensas.containsKey(recompensa))
			return;
		if (quantia >= recompensas.get(recompensa))
			recompensas.remove(recompensa);
		else
			recompensas.put(recompensa, recompensas.get(recompensa) - quantia);
	}

	public void refreshMap() {
		Main.cache.recompensas.put(name, recompensas);
	}

	public Double getRecQuantia(Recompensa recompensa) {
		if (!recompensas.containsKey(recompensa))
			return 0.0;
		return recompensas.get(recompensa);
	}

	public String getName() {
		return this.name;
	}

	public BPlayer(Player p) {
		this.name = p.getName().toLowerCase();
		if (Main.cache.recompensas.containsKey(this.name))
			this.recompensas = Main.cache.recompensas.get(this.name);
		else
			this.recompensas = new HashMap<>();
	}

	public BPlayer(String name) {
		this.name = name.toLowerCase();
		if (Main.cache.recompensas.containsKey(this.name))
			this.recompensas = Main.cache.recompensas.get(this.name);
		else
			this.recompensas = new HashMap<>();
	}
}
