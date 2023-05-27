package pt.com.GrimmBosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import pt.com.GrimmBosses.comands.BossesCMD;
import pt.com.GrimmBosses.listeners.BossKill;
import pt.com.GrimmBosses.listeners.InteractEvent;
import pt.com.GrimmBosses.listeners.InventoryClick;
import pt.com.GrimmBosses.listeners.PlayerDamage;
import pt.com.GrimmBosses.methods.Boss;
import pt.com.GrimmBosses.methods.Cache;
import pt.com.GrimmBosses.methods.MessagesCfg;
import pt.com.GrimmBosses.methods.SQL;
import pt.com.GrimmBosses.methods.TOP;
import pt.com.GrimmBosses.utils.CustomConfig;
import pt.com.GrimmBosses.utils.NumberFormatter;
import pt.com.GrimmBosses.utils.zBUtils;

public class Main extends JavaPlugin {
	public CustomConfig bossescfg;
	public CustomConfig menucfg;
	public CustomConfig matadorascfg;
	private static Main instance;
	public static NumberFormatter formatter;
	public static Cache cache;
	public SQL sql;
	public static TOP top;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		if (!hasDependences())
			return;
		instance = this;
		try {
			Class.forName("org.sqlite.JDBC").newInstance();
		} catch (Exception e) {}
		formatter = new NumberFormatter();
		saveDefaultConfig();
		bossescfg = new CustomConfig("bosses.yml");
		bossescfg.saveDefaultConfig();
		menucfg = new CustomConfig("menu.yml");
		menucfg.saveDefaultConfig();
		matadorascfg = new CustomConfig("matadoras.yml");
		matadorascfg.saveDefaultConfig();
		loadListeners();
		loadCmds();
		cache = new Cache();
		MessagesCfg.carregarTodas();
		sql = new SQL();
		sql.Iniciar();
		top = new TOP();
		startStayAlive();
		zBUtils.ConsoleMsg("&b[§7GrimmBosses§b] &aO plugin foi iniciado.");
		top.StartAtualizador();
	}

	@Override
	public void onDisable() {
		sql.Encerrar();
		for (Hologram h : HologramsAPI.getHolograms(instance)) h.delete();
		for (Boss b : Main.cache.allBosses.values()) b.getMob().remove();
		zBUtils.ConsoleMsg("&b[§7GrimmBosses§b] &cO plugin foi encerrado.");
	}

	private void loadListeners() {
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDamage(), this);
		Bukkit.getPluginManager().registerEvents(new BossKill(), this);
	}

	private void loadCmds() {
		getCommand("bosses").setExecutor(new BossesCMD());
	}

	public static Main getInstance() {
		return instance;
	}

	void startStayAlive() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Location loc : Main.cache.allBosses.keySet()) {
					Boss boss = Boss.getByLocation(loc);
					if (boss.getMob() == null) {
						Main.cache.allBosses.get(loc).deleteBoss(null);
					} else {
						boss.getMob().getNavigator().setMaxHealth(1000);
						boss.getMob().getNavigator().setHealth(1000);
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 20L * 60L, 20L * 60L);
	}

	boolean hasDependences() {
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			Bukkit.getConsoleSender()
					.sendMessage("&b[§7GrimmBosses§b] §cDependencia 'HolographicDisplays' não encontrada!");
			return false;
		}
		if (!Bukkit.getPluginManager().isPluginEnabled("MiniaturePets")) {
			Bukkit.getConsoleSender().sendMessage("&b[§7GrimmBosses§b] §cDependencia 'MiniaturePets' não encontrada!");
			return false;
		}
		return true;
	}
}
